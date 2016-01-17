package com.github.xsocket.geak.entity;

/**
 * 订单支付记录信息。
 * 
 * @author MWQ
 */
public class OrderPayment extends AbstractEntity{

  private static final long serialVersionUID = -6050684889691146502L;
  
  /** 支付方式 */
  protected PaymentMode mode;
  /** 支付金额(分) */
  protected int amount;

  public PaymentMode getMode() {
    return mode;
  }

  public void setMode(PaymentMode mode) {
    this.mode = mode;
  }

  public int getAmount() {
    return amount;
  }

  public void setAmount(int amount) {
    this.amount = amount;
  }
}
