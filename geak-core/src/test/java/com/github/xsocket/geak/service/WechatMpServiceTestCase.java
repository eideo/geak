package com.github.xsocket.geak.service;

import java.util.Collections;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.xsocket.geak.ServiceTestCase;

public class WechatMpServiceTestCase extends ServiceTestCase {

  @Autowired
  protected WechatMpService service;
  
  @Test
  public void mock() {
    System.out.println("微信API暂时不测试.");
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