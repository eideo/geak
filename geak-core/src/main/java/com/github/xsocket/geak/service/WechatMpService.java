package com.github.xsocket.geak.service;

import com.alibaba.fastjson.JSONObject;

public interface WechatMpService {

  /**
   * 获取接口访问令牌
   * @return
   */
  String getAccessToken();
  
  /**
   * 通过 oauth2 获取用户的 openid信息
   * @param code
   * @return
   */
  String getUserOpenIdFromOAuth(String code);
  
  /**
   * 获取用户基本信息
   * @param openId
   * @return
   */
  JSONObject getUserInfo(String openId);
  
  /**
   * 获取用户列表
   * @param nextOpenId
   * @return
   */
  JSONObject getUserList(String nextOpenId);
}
