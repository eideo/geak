package com.github.xsocket.geak.dao;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.xsocket.geak.DaoTestCase;
import com.github.xsocket.geak.entity.MemberDeposit;

public class MemberDepositDaoTestCase extends DaoTestCase {
  
  @Autowired
  protected MemberDepositDao dao;

  @Test
  public void testCrud() {
    MemberDeposit deposit = dao.selectById(1);    
    Assert.assertNotNull(deposit);
    Assert.assertNotNull(deposit.getMember());
    Assert.assertTrue(deposit.getMember().getName().equals("麻文强"));
    Assert.assertTrue(100 == deposit.getAmount());
    
    deposit.setId(2);
    Assert.assertEquals(1, dao.insert(deposit));
    
    deposit.setState("OVER");
    Assert.assertEquals(1, dao.update(deposit));
    
    MemberDeposit tmp = dao.selectById(2);  
    Assert.assertEquals(tmp.getState(), deposit.getState());
  }
}
