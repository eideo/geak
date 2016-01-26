package com.github.xsocket.geak.dao;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.xsocket.aa.entity.User;
import com.github.xsocket.geak.entity.Business;
import com.github.xsocket.geak.entity.Company;
import com.github.xsocket.geak.entity.PaymentMode;
import com.github.xsocket.geak.entity.PromotionPlan;

public class BasicDataDaoTestCase extends AbstractTestCase {
  
  @Autowired
  protected PromotionPlanDao ppDao;
  
  @Autowired
  protected PaymentModeDao pmDao;
  
  @Autowired
  protected ActionLogDao alDao;

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
  
  @Test
  public void testActionLogDao() {
    Company c = new Company();
    c.setId(1);
    c.setName("company");
    Business b = new Business();
    b.setId(2);
    b.setName("business");
    b.setCompany(c);
    User user = new User();
    user.setId("test");
    user.setName("test user");
    
    /* ActionLog 需要 Request 身份信息，暂时不测试了 */
    /*
    ActionLog log = new ActionLog();
    log.setAction(ActionLog.ACTION_DELETE);
    log.setContent(b);
    log.setUser(user);
    Assert.assertEquals(1, alDao.insert(log));
    */
    
  }
}
