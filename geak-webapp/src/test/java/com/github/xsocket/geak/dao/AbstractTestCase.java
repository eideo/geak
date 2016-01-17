package com.github.xsocket.geak.dao;

import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath:spring/test/mybatis-test-context.xml",
  "classpath:spring/jdbc/transaction-context.xml", "classpath:spring/jdbc/mybatis-context.xml" }) // 加载配置文件
public abstract class AbstractTestCase {

}
