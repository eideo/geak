package com.github.xsocket.geak.entity;

import java.io.Serializable;
import java.util.Date;

public class MemberDeposit implements Serializable {

  private static final long serialVersionUID = -538288062612366018L;

  protected Integer id;
  protected Integer amount;
  protected String state;
  protected String recordNo;
  protected String recordType;
  protected String tradeNo;
  protected String tradeContent;
  protected String tradeType;
  protected String ip;
  protected Date beginDate;
  protected Date overDate;
  protected Member member;

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public Integer getAmount() {
    return amount;
  }

  public void setAmount(Integer amount) {
    this.amount = amount;
  }

  public String getState() {
    return state;
  }

  public void setState(String state) {
    this.state = state;
  }

  public String getRecordNo() {
    return recordNo;
  }

  public void setRecordNo(String recordNo) {
    this.recordNo = recordNo;
  }
  
  public String getRecordType() {
    return recordType;
  }

  public void setRecordType(String recordType) {
    this.recordType = recordType;
  }

  public String getTradeNo() {
    return tradeNo;
  }

  public void setTradeNo(String tradeNo) {
    this.tradeNo = tradeNo;
  }

  public String getTradeContent() {
    return tradeContent;
  }

  public void setTradeContent(String tradeContent) {
    this.tradeContent = tradeContent;
  }

  public String getTradeType() {
    return tradeType;
  }

  public void setTradeType(String tradeType) {
    this.tradeType = tradeType;
  }

  public String getIp() {
    return ip;
  }

  public void setIp(String ip) {
    this.ip = ip;
  }

  public Date getBeginDate() {
    return beginDate;
  }

  public void setBeginDate(Date beginDate) {
    this.beginDate = beginDate;
  }

  public Date getOverDate() {
    return overDate;
  }

  public void setOverDate(Date overDate) {
    this.overDate = overDate;
  }

  public Member getMember() {
    return member;
  }

  public void setMember(Member member) {
    this.member = member;
  }

}
