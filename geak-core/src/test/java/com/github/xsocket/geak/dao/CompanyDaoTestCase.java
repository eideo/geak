package com.github.xsocket.geak.dao;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.xsocket.geak.DaoTestCase;
import com.github.xsocket.geak.entity.Company;

public class CompanyDaoTestCase extends DaoTestCase {
  
  @Autowired
  protected CompanyDao dao;

  @Test
  public void testCrud() {
    Company company = dao.selectById(1);
    Assert.assertNotNull(company);
    
    company.setId(123);
    Assert.assertEquals(1, dao.insert(company));
  }
}
