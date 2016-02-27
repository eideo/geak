package com.github.xsocket.geak.dao;

import java.util.Date;
import java.util.List;
import java.util.Set;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.xsocket.geak.entity.Appointment;
import com.github.xsocket.geak.entity.Business;
import com.github.xsocket.geak.entity.Company;
import com.github.xsocket.geak.entity.Customer;
import com.github.xsocket.util.DefaultPair;
import com.github.xsocket.util.Pair;
import com.google.common.collect.Sets;

public class AppointmentDaoTestCase extends AbstractTestCase {
  
  @Autowired
  protected AppointmentDao dao;
  
  @Autowired
  protected CustomerDao customerDao;
  
  @Autowired
  protected BusinessDao businessDao;
  
  private Customer customer;
  
  private Company company;
  
  @Before
  public void before() {
    customer = new Customer();
    customer.setTelephone("13812345678");
    customer.setName("name");
    customer.setSex("M");
    Assert.assertEquals(1, customerDao.insert(customer));
    
    company = new Company();
    company.setId(1);
    company.setName("测试公司");
  }
  
  @After
  public void after() {
    customerDao.delete(customer);
  }
  
  public void testQuery() {
    Set<Pair<String, String>> pairs = Sets.newHashSet();
    pairs.add(DefaultPair.newPair("09:00", "10:00"));
    pairs.add(DefaultPair.newPair("13:00", "15:00"));
    pairs.add(DefaultPair.newPair("23:00", "24:00"));
    dao.selectByQuery(1, new Date(), new Date(), pairs, "1,2,3");
  }
  
  @Test
  public void testBasicDao() {
    
    List<Business> businesses = businessDao.selectByCompany(company);
    
    Appointment appointment = new Appointment();
    appointment.setCustomer(customer);
    appointment.setCustomerCount(3);
    appointment.setDatetime(new Date());
    appointment.setState("state");
    appointment.setCompany(company);
    appointment.setBusinesses(businesses);
    
    // 测试插入
    Assert.assertEquals(1, dao.insert(appointment));
    Set<Pair<Appointment, Business>> sets = Sets.newHashSet();
    for(Business b : appointment.getBusinesses()) {
      sets.add(DefaultPair.newPair(appointment, b));
    }
    // 插入关联主题
    Assert.assertEquals(businesses.size(), dao.insertRelation(sets));
    Appointment temp = dao.selectById(appointment.getId());
    assertEquals(appointment, temp);
    
    // 测试更新
    appointment.setCustomerCount(2);
    appointment.setState("state2");
    appointment.setCustomer(null);
    Assert.assertEquals(1, dao.update(appointment));
    temp = dao.selectById(appointment.getId());
    appointment.setCustomer(customer);
    assertEquals(appointment, temp);
    
    // 测试删除
    dao.delete(appointment);
    temp = dao.selectById(appointment.getId());
    Assert.assertNull(temp);
    
  }

  protected void assertEquals(Appointment a1, Appointment a2) {
    Assert.assertEquals(a1.getId(), a2.getId());
    Assert.assertEquals(a1.getCustomerCount(), a2.getCustomerCount());
    Assert.assertEquals(a1.getState(), a2.getState());
    Assert.assertEquals(a1.getDatetime(), a2.getDatetime());
    Customer c1 = a1.getCustomer();
    Customer c2 = a2.getCustomer();
    Assert.assertEquals(c1.getId(), c2.getId());
    Assert.assertEquals(c1.getName(), c2.getName());
    Assert.assertEquals(c1.getSex(), c2.getSex());
    Assert.assertEquals(c1.getTelephone(), c2.getTelephone());
  }
}
