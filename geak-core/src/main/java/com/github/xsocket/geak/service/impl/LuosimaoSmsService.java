package com.github.xsocket.geak.service.impl;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

import org.apache.http.Consts;
import org.apache.http.NameValuePair;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.ContentType;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.github.xsocket.geak.SmsException;
import com.github.xsocket.geak.service.SmsService;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

@Service
public class LuosimaoSmsService implements SmsService {
  
  private static final Logger LOGGER = LoggerFactory.getLogger(LuosimaoSmsService.class);  
  
  private static final String API_URL = "http://sms-api.luosimao.com/v1/send.json";

  // HTTP Basic Authentication: https://en.wikipedia.org/wiki/Basic_access_authentication
  // api:key-1775f328562f04f36783e57911639e4c
  private static final String BASIC_AUTH_HEADER = "Basic YXBpOmtleS0xNzc1ZjMyODU2MmYwNGYzNjc4M2U1NzkxMTYzOWU0Yw==";
  
  private static final String SMS_SIGNATURE = " 【唯快点餐】";
  
  private static final ContentType CONTENT_TYPE = ContentType.create("application/x-www-form-urlencoded", Consts.UTF_8);

  @Override
  public JSONObject sendSms(String mobile, String message) {
    try {
      List<NameValuePair> params = new ArrayList<NameValuePair>(2);
      params.add(new BasicNameValuePair("mobile", mobile));
      params.add(new BasicNameValuePair("message", message + SMS_SIGNATURE));
      
      String param = URLEncodedUtils.format(params, Consts.UTF_8);
      
      String content = Request.Post(API_URL)
          .addHeader("Authorization", BASIC_AUTH_HEADER)
          .bodyString(param, CONTENT_TYPE)
          .execute().returnContent().asString();
      
      JSONObject json = JSONObject.parseObject(content);
      checkApiResult(json);
      
      return json;
    } catch (IOException e) {
      throw new RuntimeException("Fail to sent SMS message.", e);
    }
  }
  
  protected void checkApiResult(JSONObject json) throws SmsException {
    int code = json.getIntValue("error");
    String msg = json.getString("msg");
    if(code == 0) {
      return;
    }
    LOGGER.debug(String.format("Luosimao API Exception - errcode:[%d], errmsg:%s", code, msg));
    throw new SmsException(code, msg);
  }
  
  private static final Cache<String, Captcha> cache = CacheBuilder.newBuilder()
      .maximumSize(1000)
      .expireAfterWrite(30, TimeUnit.MINUTES)
      .build();

  @Override
  public Integer sendCaptcha(String mobile) {
    Captcha captcha = new Captcha();
    sendSms(mobile, captcha.getMsg());
    cache.put(mobile, captcha);
    return captcha.getCode();
  }

  @Override
  public Integer fetchCaptcha(String mobile) {
    Captcha captcha = cache.getIfPresent(mobile);
    return captcha == null ? null : captcha.getCode();
  }

  @Override
  public boolean verifyCaptcha(String mobile, int captcha) {
    Captcha cached = cache.getIfPresent(mobile);
    if (cached == null) {
        // 验证码已经过期
        return false;
    }
    return cached.getCode() == captcha;
  }
  
  
  static class Captcha implements Serializable {

    private static final long serialVersionUID = -5685980481774008270L;

    private static final String msgFmt = "【极客密室】验证码：%d，请于30分钟内输入使用";
    
    private final int code; // 四位数验证码
    private final long sentTime; // 发送时间
    
    public Captcha() {
        this.code = ThreadLocalRandom.current().nextInt(1000, 9999);
        this.sentTime = System.currentTimeMillis();
    }

    // 返回短信内容
    public String getMsg() {
        return String.format(msgFmt, code);
    }
    
    public int getCode() {
        return code;
    }  

    public long getSentTime() {
        return sentTime;
    }
  }

}
