package com.github.xsocket.geak.dao;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.xsocket.geak.DaoTestCase;
import com.github.xsocket.geak.entity.Member;

public class MemberDaoTestCase extends DaoTestCase {
  
  @Autowired
  protected MemberDao dao;

  @Test
  public void testCrud() {
    Member member = dao.selectById(1);    
    Assert.assertNotNull(member);
    Assert.assertTrue(member.getName().equals("麻文强"));
    Assert.assertTrue(0 == member.getBalance());
    
    member.setId(2);
    member.setOpenId("asdf");
    Assert.assertEquals(1, dao.insert(member));
    
    member.setName("闫寅卓");
    Assert.assertEquals(1, dao.update(member));
    
    Member tmp = dao.selectById(2);  
    Assert.assertEquals(tmp.getName(), member.getName());
  }
}
