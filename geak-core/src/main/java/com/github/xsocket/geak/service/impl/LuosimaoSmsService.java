package com.github.xsocket.geak.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

}
