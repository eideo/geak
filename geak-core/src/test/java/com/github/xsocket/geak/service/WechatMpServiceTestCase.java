package com.github.xsocket.geak.service;

import java.util.Collections;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class WechatMpServiceTestCase /*extends ServiceTestCase*/ {

  //@Autowired
  protected WechatMpService service;
  
  //@Autowired
  protected SmsService smsService;
  
  //@Test
  public void testSms() {
    smsService.sendSms("15001276389", "登陆验证码：131454，请尽快使用。");
  }
  
  //@Test
  public void test() {
    JSONObject userList = service.getUserList(null);
    System.err.println("total:" + userList.getIntValue("total"));
    System.err.println("count:" + userList.getIntValue("count"));
    System.err.println("next:" + userList.getString("next_openid"));
    JSONArray list = userList.getJSONObject("data").getJSONArray("openid");
    // String openId = null;
    boolean start = false;
    Collections.reverse(list);
    for(Object openid : list) {
      if("oAWv4jnVwh1lunG6i9gng9z19zlg".equals(openid)) {
        start = true;
        continue;
      }
      
      if(start) {
        JSONObject user = service.getUserInfo(openid.toString());
        if("麻文强".equals(user.getString("nickname"))) {
          System.err.println(user.toJSONString());
          break;
        }
      }
    }
    
    // System.err.println(service.getUserInfo(openId).toJSONString());
    
  }
}
