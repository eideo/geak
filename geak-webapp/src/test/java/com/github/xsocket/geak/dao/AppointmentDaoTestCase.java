package com.github.xsocket.geak.dao;

import java.util.Date;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.xsocket.geak.entity.Appointment;
import com.github.xsocket.geak.entity.Customer;

public class AppointmentDaoTestCase extends AbstractTestCase {
  
  @Autowired
  protected AppointmentDao dao;
  
  @Autowired
  protected CustomerDao customerDao;
  
  @Test
  public void testBasicDao() {
    Customer customer = new Customer();
    customer.setTelephone("13812345678");
    customer.setName("name");
    customer.setSex("M");
    Assert.assertEquals(1, customerDao.insert(customer));
    
    Appointment appointment = new Appointment();
    appointment.setCustomer(customer);
    appointment.setCustomerCount(3);
    appointment.setDatetime(new Date());
    appointment.setState("state");
    
    // 测试插入
    Assert.assertEquals(1, dao.insert(appointment));
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
    
    customerDao.delete(customer);
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
