package com.github.xsocket.geak.service.impl;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.UUID;

import org.apache.http.client.fluent.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.xsocket.geak.WechatException;
import com.github.xsocket.geak.service.WechatMpService;
import com.github.xsocket.geak.util.EncoderHandler;

@Service
public class DefaultWechatMpService implements WechatMpService {
  
  private static final Logger LOGGER = LoggerFactory.getLogger(DefaultWechatMpService.class);  
  
  private static Charset CHARSET_WECHAT = Charset.forName("UTF-8");
  
  @Value("${wechat.appid}")
  private String appId;
  @Value("${wechat.appsecret}")
  private String appSecret;
  

  private String accessToken;
  private long expiredTimeMillis = 0L;
  
  private String jsapiTicket;
  private long ticketExpiredTimeMillis = 0L;

  @Override
  public synchronized String getAccessToken() {
    if(System.currentTimeMillis() > expiredTimeMillis) {
      final String url = String.format(
          "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=%s&secret=%s", 
          this.appId, this.appSecret);
      
      // 通过微信API获取新的accessToken
      LOGGER.debug("Start calling Wechat API - GetAccessToken: {}", url);
      try {
        String content = Request.Get(url).execute().returnContent().asString();
        JSONObject json = JSON.parseObject(content);
        
        checkApiResult(json);
        
        accessToken = json.getString("access_token");
        // 失效时间前后预留5秒
        expiredTimeMillis = (json.getLongValue("expires_in") - 5L) * 1000L + System.currentTimeMillis();
        
      } catch (IOException e) {
        throw new RuntimeException("Fail to fetch Wechat AccessToken", e);
      }
    }
    
    return accessToken;
  }
  
  @Override
  public synchronized String getJsapiTicket() {
    if(System.currentTimeMillis() > ticketExpiredTimeMillis) {
      String accessToken = getAccessToken();
      final String url = String.format(
          "https://api.weixin.qq.com/cgi-bin/ticket/getticket?type=jsapi&access_token=%s",
          accessToken);
      
      // 通过微信API获取新的accessToken
      LOGGER.debug("Start calling Wechat API - GetJsapiTicket: {}", url);
      try {
        String content = Request.Get(url).execute().returnContent().asString();
        JSONObject json = JSON.parseObject(content);
        
        checkApiResult(json);
        
        jsapiTicket = json.getString("ticket");
        // 失效时间前后预留5秒
        ticketExpiredTimeMillis = (json.getLongValue("expires_in") - 5L) * 1000L + System.currentTimeMillis();
        
      } catch (IOException e) {
        throw new RuntimeException("Fail to fetch Wechat JsapiTicket", e);
      }
    }
    
    return jsapiTicket;
  }
  
  @Override
  public JSONObject getJsConfig(String url) {
    String ticket = this.getJsapiTicket();
    long timestamp = new Date().getTime() / 1000L;
    String nonceStr = UUID.randomUUID().toString();
    String signBefore = String.format("jsapi_ticket=%s&noncestr=%s&timestamp=%s&url=%s", 
        ticket, nonceStr, timestamp, url);
    
    String sign = EncoderHandler.encode("SHA1", signBefore);

    JSONObject configJson = new JSONObject();
    configJson.put("appId", appId);
    configJson.put("timestamp", timestamp);
    configJson.put("nonceStr", nonceStr);
    configJson.put("signature", sign);

    return configJson;
  }
  
  @Override
  public String getUserOpenIdFromOAuth(String code) {
    final String url = String.format(
        "https://api.weixin.qq.com/sns/oauth2/access_token?appid=%s&secret=%s&code=%s&grant_type=authorization_code", 
        appId, appSecret, code);
    
    LOGGER.debug("Start calling Wechat API - GetUserOpenIdFromOAuth: {}", url);
    
    try {
      String content = Request.Get(url).execute().returnContent().asString(CHARSET_WECHAT);
      JSONObject json = JSON.parseObject(content);
      checkApiResult(json);
      
      return json.getString("openid");
    } catch (IOException e) {
      throw new RuntimeException("Fail to fetch Wechat UserOpenIdFromOAuth", e);
    }
  }

  @Override
  public JSONObject getUserInfo(String openId) {
    String accessToken = getAccessToken();
    final String url = String.format(
        "https://api.weixin.qq.com/cgi-bin/user/info?access_token=%s&openid=%s", accessToken, openId);
    
    LOGGER.debug("Start calling Wechat API - GetUserInfo: {}", url);
    
    try {
      String content = Request.Get(url).execute().returnContent().asString(CHARSET_WECHAT);
      JSONObject json = JSON.parseObject(content);
      checkApiResult(json);
      
      return json;
    } catch (IOException e) {
      throw new RuntimeException("Fail to fetch Wechat UserInfo", e);
    }
  }

  @Override
  public JSONObject getUserList(String nextOpenId) {
    String accessToken = getAccessToken();
    
    StringBuilder sb = new StringBuilder(200);
    sb.append("https://api.weixin.qq.com/cgi-bin/user/get?access_token=").append(accessToken);
    if(nextOpenId != null && nextOpenId.trim().length() > 0) {
      sb.append("&next_openid=").append(nextOpenId);
    }
    
    final String url = sb.toString();
    
    LOGGER.debug("Start calling Wechat API - GetUserList: {}", url);
    
    try {
      String content = Request.Get(url).execute().returnContent().asString(CHARSET_WECHAT);
      JSONObject json = JSON.parseObject(content);
      checkApiResult(json);
      
      return json;
    } catch (IOException e) {
      throw new RuntimeException("Fail to fetch Wechat UserList", e);
    }
  }
  
  protected void checkApiResult(JSONObject json) throws WechatException {
    if(json.containsKey("errcode")) {
      int code = json.getIntValue("errcode");
      String msg = json.getString("errmsg");
      if(code == 0) {
        return;
      }
      LOGGER.debug(String.format("Wechat API Exception - errcode:[%d], errmsg:%s", code, msg));
      throw new WechatException(code, msg);
    }
  }

}
