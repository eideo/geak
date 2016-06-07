package com.github.xsocket.geak.dao;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.xsocket.geak.DaoTestCase;
import com.github.xsocket.geak.entity.User;

public class UserDaoTestCase extends DaoTestCase {
  
  @Autowired
  protected UserDao dao;

  @Test
  public void testCrud() {
    User user = dao.selectById("1");    
    Assert.assertNotNull(user);
    Assert.assertNotNull(user.getCompany());
    Assert.assertTrue(user.getName().equals("麻文强"));
    Assert.assertTrue(user.getCompany().getId() == 1);
    
    user.setId("2");
    user.setAccount("2");
    Assert.assertEquals(1, dao.insert(user));
    
    user.setName("闫寅卓");
    Assert.assertEquals(1, dao.update(user));
    
    User tmp = dao.selectById("2");  
    Assert.assertEquals(tmp.getName(), user.getName());
  }
}
