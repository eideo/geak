package com.github.xsocket.geak.entity;

/**
 * 订单促销记录。
 * 
 * @author MWQ
 */
public class OrderPromotion extends AbstractEntity {

  private static final long serialVersionUID = 7572331794400778620L;
  
  /** 促销数量 */
  protected Integer count;
  /** 促销计划 */
  protected PromotionPlan plan;
  
  public OrderPromotion() {
    plan = new PromotionPlan();
  }
  
  public OrderPromotion(PromotionPlan plan, Integer count) {
    this.plan = plan;
    this.count = count;
  }

  public Integer getCount() {
    return count;
  }

  public void setCount(Integer count) {
    this.count = count;
  }

  public PromotionPlan getPlan() {
    return plan;
  }

  public void setPlan(PromotionPlan plan) {
    this.plan = plan;
  }

}
