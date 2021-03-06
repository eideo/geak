package com.github.xsocket.geak.entity;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.alibaba.fastjson.JSON;

/**
 * 极客会员实体类。
 * 
 * @author MWQ
 */
public class Member implements Serializable {

  private static final long serialVersionUID = -3676023515453857307L;
  
  private static final DateFormat FORMAT = new SimpleDateFormat("yyyyMMdd");
  
  private static final int NAME_LENGTH = 32;
  
  protected Integer id = 0;
  protected String account;
  protected String name;
  protected String nickname;
  protected String phone;

  protected String openId;
  protected String unionId;
  protected String sex = "S";
  protected String headUrl;
  protected String state;

  protected Date createdDate;
  protected Date subscribeDate;
  // 账户余额
  protected Integer balance;
  // 账户积分
  protected Integer score;

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getAccount() {
    if(subscribeDate == null) {
      return null;
    }
    return FORMAT.format(subscribeDate) + String.format("%06d", id);
  }

  public void setAccount(String account) {
    this.account = account;
  }

  public String getName() {
    if(name == null) {
      return "";
    } else if(name.length() > NAME_LENGTH) {
      return name.substring(0, NAME_LENGTH);
    }
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getNickname() {
    if(nickname == null) {
      return "";
    } else if(nickname.length() > NAME_LENGTH) {
      return nickname.substring(0, NAME_LENGTH);
    }
    return nickname;
  }

  public void setNickname(String name) {
    this.nickname = name;
  }
  
  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

  public String getOpenId() {
    return openId;
  }

  public void setOpenId(String openId) {
    this.openId = openId;
  }

  public String getUnionId() {
    return unionId;
  }

  public void setUnionId(String unionId) {
    this.unionId = unionId;
  }

  public String getSex() {
    return sex;
  }

  public void setSex(String sex) {
    this.sex = sex;
  }
  
  public String getHeadSmall() {
    if(this.headUrl != null && this.headUrl.endsWith("0")){
      int length = this.headUrl.length();
      return this.headUrl.substring(0, length - 1) + "64";
    }
    return this.headUrl;
  }

  public String getHeadUrl() {
    return headUrl;
  }

  public void setHeadUrl(String headUrl) {
    this.headUrl = headUrl;
  }

  public String getState() {
    return state;
  }

  public void setState(String state) {
    this.state = state;
  }

  public Date getCreatedDate() {
    return createdDate;
  }

  public void setCreatedDate(Date createdDate) {
    this.createdDate = createdDate;
  }

  public Date getSubscribeDate() {
    return subscribeDate;
  }

  public void setSubscribeDate(Date subscribeDate) {
    this.subscribeDate = subscribeDate;
  }

  public Integer getBalance() {
    return balance;
  }

  public void setBalance(Integer balance) {
    this.balance = balance;
  }

  public Integer getScore() {
    return score;
  }

  public void setScore(Integer score) {
    this.score = score;
  }
  
  public String toJsonString() {
    return JSON.toJSONString(this);
  }

}
