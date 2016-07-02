package com.github.xsocket.geak.service.impl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.github.xsocket.geak.dao.MemberDao;
import com.github.xsocket.geak.dao.MemberDepositDao;
import com.github.xsocket.geak.entity.Member;
import com.github.xsocket.geak.entity.MemberDeposit;
import com.github.xsocket.geak.service.MemberService;
import com.github.xsocket.geak.service.WechatMpService;
import com.github.xsocket.geak.util.EmojiFilter;

@Service
public class DefaultMemberService implements MemberService {
  private static final Logger LOGGER = LoggerFactory.getLogger(DefaultMemberService.class);  
  
  private static final String FORMAT_PATTERN = "yyyyMMdd";
  private static final DateFormat FORMAT = new SimpleDateFormat(FORMAT_PATTERN);
  
  private static final String STATE_NEW = "NEW";
  private static final String STATE_UNPAYED = "UNPAYED";
  private static final String STATE_PAYED = "PAYED";
  private static final String STATE_EXPIRED = "EXPIRED";
  private static final String STATE_CANCELLED = "CANCELLED";
  
  private static final String TYPE_DEPOSIT = "DEPOSIT";
  
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
    deposit.setRecordType(TYPE_DEPOSIT);
    deposit.setState(STATE_NEW);
    deposit.setTradeContent(tradeContent);
    deposit.setTradeNo("");
    deposit.setTradeType("JSAPI");
    int update = depositDao.insert(deposit);
    
    if(update != 1 || deposit.getId() <= 0) {
      throw new RuntimeException(
          String.format("Fail to CreateMemberDeposit, member:%s, amount:%d.", member.getOpenId(), amount));
    }
    
    // 生成内部订单号
    String recordNo = FORMAT.format(deposit.getBeginDate()) + String.format("%08d", deposit.getId());
    // 微信交易以'分'为单位
    // FIXME 以'分'为单位测试
    String tradeNo = service.prepayOrder(amount * 100, recordNo, tradeContent, member.getOpenId(), ip);
    
    // 更新订单装
    deposit.setRecordNo(recordNo);
    deposit.setTradeNo(tradeNo);
    deposit.setState(STATE_UNPAYED);
    depositDao.update(deposit);
    
    JSONObject config = service.getWechatPayConfig(tradeNo);
    // 订单主键
    config.put("id", deposit.getId());
    return config;
  }
  
  //完成订单
  @Override
  public MemberDeposit completeMemberDeposit(String recordNo) {
    Integer id = Integer.parseInt(recordNo.substring(FORMAT_PATTERN.length()));
    MemberDeposit deposit = depositDao.selectById(id);     

    // 已经确认过的订单，直接返回
    if(STATE_PAYED.equals(deposit.getState())) {
      return deposit;
    }
    
    deposit.setState(STATE_PAYED);
    deposit.setOverDate(new Date());
    depositDao.update(deposit);
    
    Member member = deposit.getMember();
    member.setBalance(member.getBalance() + deposit.getAmount());
    memberDao.update(member);
    
    return deposit;
  }
  
  //取消订单
  @Override
  public MemberDeposit cancelMemberDeposit(Integer id) {
    MemberDeposit deposit = depositDao.selectById(id);
    
    deposit.setState(STATE_CANCELLED);
    deposit.setOverDate(new Date());
    depositDao.update(deposit);
    
    return deposit;
  }

  @Override
  public Member loadMemberByOpenId(String openId, String accessToken) {
    Member member = memberDao.selectByOpenId(openId);
    if(member == null) {
      JSONObject json = accessToken == null ? service.getUserInfo(openId)
          : service.getUserInfoFromOAuth(openId, accessToken);
      member = new Member();
      member.setAccount("");
      member.setBalance(0);
      member.setCreatedDate(new Date());
      member.setHeadUrl(json.getString("headimgurl"));
      member.setName(EmojiFilter.filterEmoji(json.getString("nickname")));
      member.setNickname(member.getName());
      member.setOpenId(openId);
      member.setPhone("");
      member.setScore(0);
      int sex = json.getIntValue("sex");
      member.setSex(sex == 1 ? "M" : (sex == 2 ? "F" : "S"));
      member.setState("NORMAL");
      member.setSubscribeDate(new Date(json.getLongValue("subscribe_time") * 1000L));
      member.setUnionId("");
      try {
        memberDao.insert(member);
      } catch(Exception e) {
        // 因为emoji新建失败
        LOGGER.warn("Member insert fail, maybe caused by emoji, try auto fix it.", e);
        member.setNickname("极客会员");
        member.setName("极客会员");
        memberDao.insert(member);
      }
    }
    return member;
  }

  @Override
  public List<MemberDeposit> listDeposit(Member member) {
    List<MemberDeposit> list = depositDao.selectedByMember(member);
    Date now = new Date();
    for(MemberDeposit deposit : list) {
      if(STATE_PAYED.equals(deposit.getState())) {
        continue;
      }
      if(deposit.getOverDate().before(now)) {
        // 过期的支付订单
        deposit.setState(STATE_EXPIRED);
      }
    }
    return list;
  }

}
