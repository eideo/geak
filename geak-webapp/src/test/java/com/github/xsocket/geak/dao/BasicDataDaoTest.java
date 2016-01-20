package com.github.xsocket.geak.dao;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.xsocket.geak.entity.PaymentMode;
import com.github.xsocket.geak.entity.PromotionPlan;

public class BasicDataDaoTest extends AbstractTestCase {
  
  @Autowired
  protected PromotionPlanDao ppDao;
  
  @Autowired
  protected PaymentModeDao pmDao;

  @Test
  public void testBaiscDataDao() {
    // 测试促销计划
    Assert.assertEquals(6, ppDao.selectAll().size());
    PromotionPlan plan = ppDao.selectById(4);
    Assert.assertEquals(4, plan.getId().intValue());
    Assert.assertEquals("通关券", plan.getName());
    
    // 测试支付渠道
    Assert.assertEquals(6, pmDao.selectAll().size());
    PaymentMode mode = pmDao.selectById(3);
    Assert.assertEquals(3, mode.getId().intValue());
    Assert.assertEquals("微信", mode.getName());
  }
}
