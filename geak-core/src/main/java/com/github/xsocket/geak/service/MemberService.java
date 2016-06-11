package com.github.xsocket.geak.service;

import org.springframework.transaction.annotation.Transactional;

import com.github.xsocket.geak.entity.Member;

/**
 * 极客会员相关业务。
 * 
 * @author MWQ
 */
public interface MemberService {

  @Transactional(readOnly=true)
  Member loadMemberByOpenId(String openId);
  
}
