package com.github.xsocket.geak.util;

import java.io.IOException;

import org.apache.http.client.fluent.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Strings;

public abstract class WeixinUtils {
  
  private static final Logger LOGGER = LoggerFactory.getLogger(WeixinUtils.class);
  
  public static final String CORP_ID = "wxb6ea60848a4abe21";
  
  public static final String CORP_SECRET = "jNBGmgUChixbFq2xafew1LBC5TuovjhgkrPwpIXiXvR4sB_9lqfIFBoorCsp3O2s";
  
  public static final String API_URL_GET_ACCESS_TOKEN = String
      .format("https://qyapi.weixin.qq.com/cgi-bin/gettoken?corpid=%s&corpsecret=%s", CORP_ID, CORP_SECRET);
  
  public static final String API_URL_GET_USER_INFO = "https://qyapi.weixin.qq.com/cgi-bin/user/getuserinfo?access_token=%s&code=%s";
  
  protected static String accessToken;
  
  protected static long expiredTimeMillis = 0L;

  /**
   * 获取接口访问令牌
   * @return
   */
  public synchronized static String getAccessToken() {
    if(System.currentTimeMillis() > expiredTimeMillis) {
      // 通过微信API获取新的accessToken
      LOGGER.debug("开始调用微信API - 获取AccessToken：{}", API_URL_GET_ACCESS_TOKEN);
      try {
        String content = Request.Get(API_URL_GET_ACCESS_TOKEN).execute().returnContent().asString();
        JSONObject json = JSON.parseObject(content);
        
        if(!json.containsKey("access_token")) {
          String msg = String.format("获取AccessToken失败:%s", content);
          LOGGER.debug(msg);
          throw new RuntimeException(msg);
        }
        
        accessToken = json.getString("access_token");
        // 失效时间前后预留5秒
        expiredTimeMillis = (json.getLongValue("expires_in") - 5L) * 1000L + System.currentTimeMillis();
        
      } catch (IOException e) {
        String msg = "获取AccessToken失败";
        LOGGER.debug(msg);
        throw new RuntimeException(msg, e);
      }
    }
    
    return accessToken;
  }
  
  public static String getUserId(String code) {
    if(Strings.isNullOrEmpty(code)) {
      throw new IllegalArgumentException("获取成员ID信息需要传入有效的访问码参数!");
    }
    String token = getAccessToken();
    String url = String.format(API_URL_GET_USER_INFO, token, code);
    
    LOGGER.debug("开始调用微信API - 获取UserId：{}", url);
    try {
      String content = Request.Get(url).execute().returnContent().asString();
      JSONObject json = JSON.parseObject(content);
      if(json.containsKey("UserId")) {
        return json.getString("UserId");
      } else if(json.containsKey("OpenId")) {
        String msg = "访问用户非企业成员，无法获取其UserId数据";
        LOGGER.debug(msg);
        throw new RuntimeException(msg);
      } else {
        String msg = String.format("获取UserId失败:%s", content);
        LOGGER.debug(msg);
        throw new RuntimeException(msg);
      }
    } catch (IOException e) {
      String msg = "获取UserId失败";
      LOGGER.debug(msg);
      throw new RuntimeException(msg, e);
    }
  }
}
