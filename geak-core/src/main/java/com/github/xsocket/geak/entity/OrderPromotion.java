package com.github.xsocket.geak.entity;

import java.io.Serializable;

/**
 * 订单优惠记录信息。
 * 
 * @author MWQ
 */
public class OrderPromotion implements Serializable {

  private static final long serialVersionUID = 288314371505264039L;
  
  /** 优惠方式 */
  protected String mode;
  /** 优惠数量 */
  protected Integer count;
  /** 优惠说明 */
  protected String note;

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
  
  public String getNote() {
    return note;
  }

  public void setNote(String note) {
    this.note = note;
  }
}
