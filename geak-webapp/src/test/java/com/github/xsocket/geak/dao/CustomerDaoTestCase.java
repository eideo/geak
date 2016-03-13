package com.github.xsocket.geak.dao;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.xsocket.geak.entity.Customer;

public class CustomerDaoTestCase extends AbstractTestCase {
  
  @Autowired
  protected CustomerDao dao;
  
  @Test
  public void testBasicDao() {
    Customer customer = new Customer();
    customer.setTelephone("13812345678");
    customer.setName("name");
    customer.setSex("M");
    // 测试插入
    Assert.assertEquals(1, dao.insert(customer));
    Customer temp = dao.selectById(customer.getId());
    assertEquals(customer, temp);
    
    // 测试更新
    customer.setName("name2");
    customer.setTelephone(null); // 设置为null则不进行更新
    customer.setSex("F");
    Assert.assertEquals(1, dao.update(customer));
    temp = dao.selectById(customer.getId());
    customer.setTelephone("未填写");
    assertEquals(customer, temp);
    
    // 测试删除
    dao.delete(customer);
    temp = dao.selectById(customer.getId());
    Assert.assertNull(temp);
  }

  protected void assertEquals(Customer c1, Customer c2) {
    Assert.assertEquals(c1.getId(), c2.getId());
    Assert.assertEquals(c1.getName(), c2.getName());
    Assert.assertEquals(c1.getSex(), c2.getSex());
    Assert.assertEquals(c1.getTelephone(), c2.getTelephone());
  }
}
