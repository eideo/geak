package com.github.xsocket.geak.service;

import java.util.List;

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
  Member loadMemberByOpenId(String openId, String accessToken);
  
  @Transactional
  JSONObject createMemberDeposit(Member member, int amount, String ip);
  
  @Transactional
  MemberDeposit completeMemberDeposit(String tradeNo);
  
  @Transactional
  MemberDeposit cancelMemberDeposit(Integer id);
  
  @Transactional(readOnly = true)
  List<MemberDeposit> listDeposit(Member member);
  
}
