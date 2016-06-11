package com.github.xsocket.geak.service.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.github.xsocket.geak.dao.MemberDao;
import com.github.xsocket.geak.entity.Member;
import com.github.xsocket.geak.service.MemberService;
import com.github.xsocket.geak.service.WechatMpService;

@Service
public class DefaultMemberService implements MemberService {
  
  @Autowired
  private MemberDao dao;
  
  @Autowired
  private WechatMpService service;

  @Override
  public Member loadMemberByOpenId(String openId) {
    Member member = dao.selectedByOpenId(openId);
    if(member == null) {
      JSONObject json = service.getUserInfo(openId);
      member = new Member();
      member.setAccount("");
      member.setBalance(0);
      member.setCreatedDate(new Date());
      member.setHeadUrl(json.getString("headimgurl"));
      member.setName(json.getString("nickname"));
      member.setNickname(member.getName());
      member.setOpenId(openId);
      member.setPhone("");
      member.setScore(0);
      int sex = json.getIntValue("sex");
      member.setSex(sex == 1 ? "M" : (sex == 2 ? "F" : "S"));
      member.setState("NORMAL");
      member.setSubscribeDate(new Date(json.getLongValue("subscribe_time") * 1000L));
      member.setUnionId("");
      
      dao.insert(member);
    }
    return member;
  }

}
