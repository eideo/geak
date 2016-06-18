package com.github.xsocket.geak.dao;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.xsocket.geak.DaoTestCase;
import com.github.xsocket.geak.entity.Order;

public class OrderDaoTestCase extends DaoTestCase {
  
  @Autowired
  protected OrderDao dao;

  @Test
  public void testCrud() {
    Order order = dao.selectById(1);
    Assert.assertEquals(100, order.getAmount().intValue());
    Assert.assertEquals(1, order.getPayments().size());
    Assert.assertEquals(1, order.getProducts().size());
    Assert.assertEquals(1, order.getPromotions().size());
    
    order.setAmount(200);
    Assert.assertEquals(1, dao.update(order));
    Assert.assertEquals(1, dao.insert(order));    
  }
}
