package com.github.xsocket.geak.service.impl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.github.xsocket.geak.dao.MemberDao;
import com.github.xsocket.geak.dao.MemberDepositDao;
import com.github.xsocket.geak.entity.Member;
import com.github.xsocket.geak.entity.MemberDeposit;
import com.github.xsocket.geak.service.MemberService;
import com.github.xsocket.geak.service.WechatMpService;

@Service
public class DefaultMemberService implements MemberService {
  
  private static final DateFormat FORMAT = new SimpleDateFormat("yyyyMMdd");
  
  private static final String STATE_NEW = "NEW";
  private static final String STATE_UNPAYED = "UNPAYED";
  private static final String STATE_PAYED = "PAYED";
  //private static final String STATE_EXPIRED = "EXPIRED";
  
  @Autowired
  private MemberDao memberDao;
  
  @Autowired
  private MemberDepositDao depositDao;
  
  @Autowired
  private WechatMpService service;

  @Override
  public JSONObject createMemberDeposit(Member member, int amount, String ip) {
    if(amount <= 0) {
      throw new IllegalArgumentException("Deposit amount should be large than 0.");
    }    
    long now = System.currentTimeMillis();
    String tradeContent = String.format("极客会员充值￥%d元。", amount);
    MemberDeposit deposit = new MemberDeposit();
    deposit.setAmount(amount);
    deposit.setBeginDate(new Date(now));
    deposit.setIp(ip);
    deposit.setMember(member);
    deposit.setOverDate(new Date(now + 10 * 60 * 1000L));
    deposit.setRecordNo("");
    deposit.setState(STATE_NEW);
    deposit.setTradeContent(tradeContent);
    deposit.setTradeNo("");
    deposit.setTradeType("JSAPI");
    int update = depositDao.insert(deposit);
    
    if(update != 1 || deposit.getId() <= 0) {
      throw new RuntimeException(
          String.format("Fail to CreateMemberDeposit, member:%s, amount:%d.", member.getOpenId(), amount));
    }
    
    String recordNo = FORMAT.format(deposit.getBeginDate()) + String.format("%08d", deposit.getId());
    // 微信交易以'分'为单位
    String tradeNo = service.prepayOrder(amount * 100, recordNo, tradeContent, member.getOpenId(), ip);
    
    // 更新订单装
    deposit.setRecordNo(recordNo);
    deposit.setTradeNo(tradeNo);
    deposit.setState(STATE_UNPAYED);
    depositDao.update(deposit);
    
    return service.getWechatPayConfig(tradeNo);
  }
  
  //完成订单
  public MemberDeposit completeMemberDeposit(String tradeNo) {
    MemberDeposit deposit = depositDao.selectByTradeNo(tradeNo);
    
    deposit.setState(STATE_PAYED);
    deposit.setOverDate(new Date());
    depositDao.update(deposit);
    
    return deposit;
  }

  @Override
  public Member loadMemberByOpenId(String openId) {
    Member member = memberDao.selectByOpenId(openId);
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
      
      memberDao.insert(member);
    }
    return member;
  }

}
