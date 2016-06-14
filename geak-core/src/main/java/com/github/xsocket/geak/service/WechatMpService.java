package com.github.xsocket.geak.service;

import com.alibaba.fastjson.JSONObject;

public interface WechatMpService {

  /**
   * 获取接口访问令牌
   * @return
   */
  String getAccessToken();
  
  /**
   * 支付接口：统一下单
   * @return
   */
  String prepayOrder(int amount, String orderNo, String description, String openId, String ip);
  
  /**
   * 生成微信付款需要的配置参数
   * @param prepayId
   * @return
   */
  JSONObject getWechatPayConfig(String prepayId);
  
  /**
   * 获取jsapi_ticket
   * @return
   */
  String getJsapiTicket();
  
  /**
   * 获取JS接口调用的配置信息
   * @return
   */
  JSONObject getJsConfig(String url);
  
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
