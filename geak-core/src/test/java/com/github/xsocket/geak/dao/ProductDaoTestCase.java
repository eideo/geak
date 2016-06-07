package com.github.xsocket.geak.dao;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.xsocket.geak.DaoTestCase;
import com.github.xsocket.geak.entity.Product;

public class ProductDaoTestCase extends DaoTestCase {
  
  @Autowired
  protected ProductDao dao;

  @Test
  public void testCrud() {
    Product product = dao.selectById(1);   
    Assert.assertNotNull(product);
    Assert.assertNotNull(product.getCompany());
    Assert.assertTrue(product.getName().equals("猎人"));
    Assert.assertTrue(product.getType().equals("密室门票"));
    Assert.assertTrue(product.getCompany().getId() == 1);
    
    product.setId(2);
    Assert.assertEquals(1, dao.insert(product));
    
    product.setName("星球探秘");
    Assert.assertEquals(1, dao.update(product));
    
    Product tmp = dao.selectById(2);  
    Assert.assertEquals(tmp.getName(), product.getName());
    
    List<Product> list = dao.selectByCompanyId(1);
    Assert.assertNotNull(list);
    Assert.assertEquals(2, list.size());
  }
}
