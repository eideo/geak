package com.github.xsocket.geak.dao;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.xsocket.geak.entity.GeakUser;

public class GeakUserDaoTestCase extends AbstractTestCase {
  
  @Autowired
  protected GeakUserDao dao;
  
  @Test
  public void testBasicDao() {
    String id = "2016012301";
    // 已有测试数据
    GeakUser user = dao.selectById(id);
    Assert.assertEquals(id, user.getId());
    Assert.assertEquals("麻文强", user.getName());
    Assert.assertEquals(1, user.getCompany().getId().intValue());
  }
}
