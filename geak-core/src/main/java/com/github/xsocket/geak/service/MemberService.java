package com.github.xsocket.geak.service;

import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.github.xsocket.geak.entity.Member;
import com.github.xsocket.geak.entity.MemberDeposit;

/**
 * 极客会员相关业务。
 * 
 * @author MWQ
 */
public interface MemberService {

  @Transactional
  Member loadMemberByOpenId(String openId);
  
  @Transactional
  JSONObject createMemberDeposit(Member member, int amount, String ip);
  
  @Transactional
  MemberDeposit completeMemberDeposit(String tradeNo);
  
}
