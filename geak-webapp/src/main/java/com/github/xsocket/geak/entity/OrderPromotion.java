package com.github.xsocket.geak.entity;

/**
 * 订单促销记录。
 * 
 * @author MWQ
 */
public class OrderPromotion extends AbstractEntity {

  private static final long serialVersionUID = 7572331794400778620L;
  
  /** 促销数量 */
  protected int count;
  /** 促销计划 */
  protected PromotionPlan plan;

  public int getCount() {
    return count;
  }

  public void setCount(int count) {
    this.count = count;
  }

  public PromotionPlan getPlan() {
    return plan;
  }

  public void setPlan(PromotionPlan plan) {
    this.plan = plan;
  }

}
