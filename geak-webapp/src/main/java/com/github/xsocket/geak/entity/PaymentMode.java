package com.github.xsocket.geak.entity;

/**
 * 业务支付的方式，如现金、支付宝、微信等。
 * 
 * @author MWQ
 */
public class PaymentMode extends AbstractEntity {

  private static final long serialVersionUID = 3509505111252056190L;
  
  /** 单价 */
  protected Integer price;

  public Integer getPrice() {
    return price;
  }

  public void setPrice(Integer price) {
    this.price = price;
  }
}
