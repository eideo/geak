package com.github.xsocket.geak.service;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.xsocket.geak.ServiceTestCase;

public class WechatMpServiceTestCase extends ServiceTestCase {

  @Autowired
  protected WechatMpService service;
  
  //@Test
  public void test() {
    JSONObject userList = service.getUserList(null);
    System.err.println("total:" + userList.getIntValue("total"));
    System.err.println("count:" + userList.getIntValue("count"));
    System.err.println("next:" + userList.getString("next_openid"));
    JSONArray list = userList.getJSONObject("data").getJSONArray("openid");
    String openId = null;
    for(Object openid : list) {
      openId = openid.toString();
      System.err.println("openid:" + openid);
    }
    
    System.err.println(service.getUserInfo(openId).toJSONString());
  }
}
