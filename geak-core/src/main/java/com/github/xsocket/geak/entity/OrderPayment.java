package com.github.xsocket.geak.entity;

import java.io.Serializable;

/**
 * 订单支付记录信息。
 * 
 * @author MWQ
 */
public class OrderPayment implements Serializable {

  private static final long serialVersionUID = -4530018038564928915L;
  
  /** 支付方式 */
  protected String mode;
  /** 支付数量 */
  protected Integer count;

  public String getMode() {
    return mode;
  }

  public void setMode(String mode) {
    this.mode = mode;
  }

  public Integer getCount() {
    return count;
  }

  public void setCount(Integer count) {
    this.count = count;
  }
}
