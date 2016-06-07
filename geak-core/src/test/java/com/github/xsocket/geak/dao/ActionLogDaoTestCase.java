package com.github.xsocket.geak.dao;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.xsocket.geak.DaoTestCase;
import com.github.xsocket.geak.entity.ActionLog;
import com.github.xsocket.geak.entity.User;

public class ActionLogDaoTestCase extends DaoTestCase {
  
  @Autowired
  protected ActionLogDao dao;
  
  @Test
  public void testActionLogDao() {
    User user = new User();
    user.setId("1");
       
    ActionLog log = new ActionLog();
    log.setAction(ActionLog.ACTION_DELETE);
    log.setContent(user);
    log.setUser(user);
    Assert.assertEquals(1, dao.insert(log));   
  }
}
