package com.github.xsocket.geak.service.impl;

import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.Charset;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.UUID;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.http.client.fluent.Request;
import org.apache.http.entity.StringEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;

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
  @Value("${wechat.mchid}")
  private String mchId;
  @Value("${wechat.pay.notifyurl}")
  private String notifyUrl;
  @Value("${wechat.pay.secret}")
  private String paySecret;
  

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
  public String prepayOrder(int amount, String orderNo, String description, String openId, String ip) {
    TreeMap<String, String> map = new TreeMap<String, String>();
    map.put("appid", appId);
    map.put("mch_id", mchId);
    map.put("nonce_str", UUID.randomUUID().toString().replaceAll("-", ""));
    map.put("body", description);
    map.put("out_trade_no", orderNo);
    map.put("total_fee", new Integer(amount).toString());
    map.put("spbill_create_ip", ip);
    map.put("notify_url", notifyUrl);
    map.put("trade_type", "JSAPI");
    map.put("openid", openId);

    map.put("sign", genWechatPaySign(map));
    /*
    <xml>
      <appid>wx2421b1c4370ec43b</appid>
      <body>JSAPI支付测试</body>
      <mch_id>10000100</mch_id>
      <nonce_str>1add1a30ac87aa2db72f57a2375d8fec</nonce_str>
      <notify_url>http://wxpay.weixin.qq.com/pub_v2/pay/notify.v2.php</notify_url>
      <openid>oUpF8uMuAJO_M2pxb1Q9zNjWeS6o</openid>
      <out_trade_no>1415659990</out_trade_no>
      <spbill_create_ip>14.23.150.211</spbill_create_ip>
      <total_fee>1</total_fee>
      <trade_type>JSAPI</trade_type>
      <sign>0CB01533B8C1EF103065174F50BCA001</sign>
   </xml>
   */
    StringBuilder sb = new StringBuilder();
    sb.append("<xml>");
    for(Entry<String, String> entry : map.entrySet()) {
      sb.append("<").append(entry.getKey()).append(">");
      sb.append(String.valueOf(entry.getValue()));
      sb.append("</").append(entry.getKey()).append(">");
    }
    sb.append("</xml>");
    
    LOGGER.debug("Start calling Wechat API - PrepayOrder: {}", sb.toString());
    try {
      String xml = Request.Post("https://api.mch.weixin.qq.com/pay/unifiedorder")
          .body(new StringEntity(sb.toString(), "utf-8"))
          .execute().returnContent().asString();
      
      LOGGER.debug("Wechat PrepayOrder Return: " + xml);
      /*
      <xml>
        <return_code><![CDATA[SUCCESS]]></return_code>
        <return_msg><![CDATA[OK]]></return_msg>
        <appid><![CDATA[wx2421b1c4370ec43b]]></appid>
        <mch_id><![CDATA[10000100]]></mch_id>
        <nonce_str><![CDATA[IITRi8Iabbblz1Jc]]></nonce_str>
        <sign><![CDATA[7921E432F65EB8ED0CE9755F0E86D72F]]></sign>
        <result_code><![CDATA[SUCCESS]]></result_code>
        <prepay_id><![CDATA[wx201411101639507cbf6ffd8b0779950874]]></prepay_id>
        <trade_type><![CDATA[JSAPI]]></trade_type>
      </xml>
      */
      
      DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
      DocumentBuilder db = dbf.newDocumentBuilder();
      StringReader sr = new StringReader(xml);
      InputSource is = new InputSource(sr);
      Document document = db.parse(is);
      
      Element root = document.getDocumentElement();
      String returnCode = root.getElementsByTagName("return_code").item(0).getTextContent();
      if("SUCCESS".equals(returnCode)) {
        String resuleCode = root.getElementsByTagName("result_code").item(0).getTextContent();
        if("SUCCESS".equals(resuleCode)) {
          return root.getElementsByTagName("prepay_id").item(0).getTextContent();
        }
      }
      throw new WechatException(-1, root.getElementsByTagName("return_msg").item(0).getTextContent());
    } catch (Exception e) {
      throw new RuntimeException("Fail to call Wechat PrepayOrder", e);
    }
  }
  
  @Override
  public JSONObject getWechatPayConfig(String prepayId) {
      TreeMap<String, String> map = new TreeMap<String, String>();
      map.put("appId", appId);
      map.put("timeStamp", String.valueOf(System.currentTimeMillis() / 1000));
      map.put("nonceStr", UUID.randomUUID().toString().replaceAll("-", ""));
      map.put("package", "prepay_id=" + prepayId);
      map.put("signType", "MD5");
      map.put("paySign", genWechatPaySign(map));
      
      JSONObject payJson = new JSONObject();
      for (Entry<String, String> entry : map.entrySet()) {
          payJson.put(entry.getKey(), entry.getValue());
      }
      payJson.put("timeStamp", new Integer(map.get("timeStamp")));
      return payJson;
  }
  
  public static final void main(String[] args) throws ParseException {
    System.out.println(NumberFormat.getIntegerInstance().parse("3人"));
    
    
  }
  
  /**
   * 微信加密算法
   */
  private String genWechatPaySign(TreeMap<String, String> map) {
    StringBuilder sb = new StringBuilder();
    for (Entry<String, String> entry: map.entrySet()) {
      sb.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
    }
    String signBefore = sb.append("key=").append(paySecret).toString();
    return EncoderHandler.encodeByMD5(signBefore).toUpperCase();
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
  public JSONObject getUserOpenIdFromOAuth(String code) {
    final String url = String.format(
        "https://api.weixin.qq.com/sns/oauth2/access_token?appid=%s&secret=%s&code=%s&grant_type=authorization_code", 
        appId, appSecret, code);
    
    LOGGER.debug("Start calling Wechat API - GetUserOpenIdFromOAuth: {}", url);
    
    try {
      String content = Request.Get(url).execute().returnContent().asString(CHARSET_WECHAT);
      JSONObject json = JSON.parseObject(content);
      checkApiResult(json);
      
      return json;
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
  public JSONObject getUserInfoFromOAuth(String openId, String accessToken) {
    final String url = String.format(
        "https://api.weixin.qq.com/sns/userinfo?access_token=%s&openid=%s&lang=zh_CN",
        accessToken, openId);
    
    LOGGER.debug("Start calling Wechat API - GetUserInfoFromOAuth: {}", url);
    
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
