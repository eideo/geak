package com.github.xsocket.geak.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class Order implements Serializable {

  private static final long serialVersionUID = 4018993698589626629L;
  
  protected Integer id;
  /** 总价 */
  protected Integer amount;
  /** 订单状态 */
  protected String state;

  /** 订单所属公司(门店) */
  protected Company company;
  /** 订单客户 */
  protected Member member;
  /** 订单客户总人数 */
  protected Integer memberCount;
  /** 订单客户类型，青年、少年、老年... */
  protected String memberType;
  /** 订单来源，搜索、老玩家、朋友介绍... */
  protected String source;
  /** 订单主要内容 */
  protected String content;
  /** 订单备注 */
  protected String note;

  /** 其他促销说明 */
  protected String promotionNote;
  /** 支付模式 */
  protected String paymentMode;
  /** 其他免费说明 */
  protected String paymentNote;
  /** 该订单参与的活动 */
  protected List<OrderPromotion> promotions;
  /** 该订单的支付记录 */
  protected List<OrderPayment> payments;
  /** 该订单的产品明显 */
  protected List<OrderProduct> products;

  /** 支付完成时间 */
  protected Date paymentDate;
  /** 进场时间 */
  protected Date entranceDate;
  /** 退场时间 */
  protected Date exitDate;
  /** 取消时间 */
  protected Date cancelledDate;
  /** 创建时间 */
  protected Date createdDate;

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

  public Company getCompany() {
    return company;
  }

  public void setCompany(Company company) {
    this.company = company;
  }

  public Member getMember() {
    return member;
  }

  public void setMember(Member member) {
    this.member = member;
  }

  public Integer getMemberCount() {
    return memberCount;
  }

  public void setMemberCount(Integer memberCount) {
    this.memberCount = memberCount;
  }

  public String getMemberType() {
    return memberType;
  }

  public void setMemberType(String memberType) {
    this.memberType = memberType;
  }

  public String getSource() {
    return source;
  }

  public void setSource(String source) {
    this.source = source;
  }

  public String getPromotionNote() {
    return promotionNote;
  }

  public void setPromotionNote(String promotionNote) {
    this.promotionNote = promotionNote;
  }

  public String getPaymentMode() {
    return paymentMode;
  }

  public void setPaymentMode(String paymentMode) {
    this.paymentMode = paymentMode;
  }

  public String getPaymentNote() {
    return paymentNote;
  }

  public void setPaymentNote(String paymentNote) {
    this.paymentNote = paymentNote;
  }

  public List<OrderPromotion> getPromotions() {
    return promotions;
  }

  public void setPromotions(List<OrderPromotion> promotions) {
    this.promotions = promotions;
  }

  public List<OrderPayment> getPayments() {
    return payments;
  }

  public void setPayments(List<OrderPayment> payments) {
    this.payments = payments;
  }
  
  public List<OrderProduct> getProducts() {
    return products;
  }

  public void setProducts(List<OrderProduct> products) {
    this.products = products;
  }

  public Date getPaymentDate() {
    return paymentDate;
  }

  public void setPaymentDate(Date payedDate) {
    this.paymentDate = payedDate;
  }

  public Date getEntranceDate() {
    return entranceDate;
  }

  public void setEntranceDate(Date entranceDate) {
    this.entranceDate = entranceDate;
  }

  public Date getExitDate() {
    return exitDate;
  }

  public void setExitDate(Date exitDate) {
    this.exitDate = exitDate;
  }

  public Date getCancelledDate() {
    return cancelledDate;
  }

  public void setCancelledDate(Date cancelledDate) {
    this.cancelledDate = cancelledDate;
  }

  public Date getCreatedDate() {
    return createdDate;
  }

  public void setCreatedDate(Date createdDate) {
    this.createdDate = createdDate;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public String getNote() {
    return note;
  }

  public void setNote(String note) {
    this.note = note;
  }

}
