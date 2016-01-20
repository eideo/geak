package com.github.xsocket.geak.dao;

import java.util.Date;
import java.util.List;
import java.util.Random;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.xsocket.geak.entity.Business;
import com.github.xsocket.geak.entity.Company;
import com.github.xsocket.geak.entity.Customer;
import com.github.xsocket.geak.entity.Order;
import com.github.xsocket.geak.entity.OrderPayment;
import com.github.xsocket.geak.entity.OrderPromotion;
import com.github.xsocket.geak.entity.PaymentMode;
import com.github.xsocket.geak.entity.PromotionPlan;
import com.google.common.collect.Lists;

public class OrderDaoTestCase extends AbstractTestCase {
  
  @Autowired
  CompanyDao cDao;
  
  @Autowired
  BusinessDao bDao;
  
  @Autowired
  CustomerDao tDao;
  
  @Autowired
  PaymentModeDao pmDao;
  
  @Autowired
  PromotionPlanDao ppDao;
  
  @Autowired
  OrderDao dao;
  
  protected Company company;
  
  protected Customer customer;
  
  protected List<Business> businesses;
  
  protected List<PaymentMode> modes;
  
  protected List<PromotionPlan> plans;
  
  protected Random random = new Random(System.currentTimeMillis());
  
  @Before
  public void before() {
    company = cDao.selectById(1);
    customer = tDao.selectById(1);
    businesses = bDao.selectByCompany(company);
    modes = pmDao.selectAll();
    plans = ppDao.selectAll();
  }
  
  @Test
  public void testBasicDao() {
    Order order = new Order();
    order.setCustomerCount(10);
    order.setCustomerType("青年人");
    order.setState("state");
    order.setCompany(company);
    order.setCustomer(customer);
    order.setBusiness(businesses.get(0));
    order.setEntranceDatetime(new Date());
    List<OrderPayment> payments = Lists.newArrayList();
    for(PaymentMode mode : modes) {
      payments.add(new OrderPayment(mode, random.nextInt()));
    }
    order.setPayments(payments);
    List<OrderPromotion> promotions = Lists.newArrayList();
    for(PromotionPlan plan : plans) {
      promotions.add(new OrderPromotion(plan, random.nextInt()));
    }
    order.setPromotions(promotions);
    
    // 测试插入
    Assert.assertEquals(1, dao.insert(order));
    Assert.assertEquals(modes.size(), dao.insertPayments(order, payments));
    Assert.assertEquals(plans.size(), dao.insertPromotions(order, promotions));
    
    Order temp = dao.selectById(order.getId());
    assertEquals(order, temp);
    
    // 测试更新
    order.setCustomerCount(null);
    order.setCustomerType("青年人");
    order.setExitDatetime(new Date());
    Assert.assertEquals(1, dao.update(order));
    temp = dao.selectById(order.getId());
    order.setCustomerCount(10);
    assertEquals(order, temp);
    
    // 测试删除
    dao.delete(order);
    temp = dao.selectById(order.getId());
    Assert.assertNull(temp);
  }
  
  protected void assertEquals(Order o1, Order o2) {
    Assert.assertEquals(o1.getId(), o2.getId());
    Assert.assertEquals(o1.getCustomerType(), o2.getCustomerType());
    Assert.assertEquals(o1.getCustomerCount(), o2.getCustomerCount());
    Assert.assertEquals(o1.getEntranceDatetime(), o2.getEntranceDatetime());
    Assert.assertEquals(o1.getExitDatetime(), o2.getExitDatetime());
    Assert.assertEquals(o1.getState(), o2.getState());
    // Business
    Assert.assertEquals(o1.getBusiness().getId(), o2.getBusiness().getId());
    Assert.assertEquals(o1.getBusiness().getName(), o2.getBusiness().getName());
    Assert.assertEquals(o1.getBusiness().getAlias(), o2.getBusiness().getAlias());
    // Company
    Assert.assertEquals(o1.getCompany().getId(), o2.getCompany().getId());
    Assert.assertEquals(o1.getCompany().getName(), o2.getCompany().getName());
    Assert.assertEquals(o1.getCompany().getAlias(), o2.getCompany().getAlias());
    // Customer
    Assert.assertEquals(o1.getCustomer().getId(), o2.getCustomer().getId());
    Assert.assertEquals(o1.getCustomer().getName(), o2.getCustomer().getName());
    Assert.assertEquals(o1.getCustomer().getTelephone(), o2.getCustomer().getTelephone());
  }

}
