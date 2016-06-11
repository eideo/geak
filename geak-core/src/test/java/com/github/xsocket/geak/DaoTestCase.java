package com.github.xsocket.geak;

import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath:spring/geak-context.xml", "classpath:spring/test/mybatis-test-context.xml" })
public abstract class DaoTestCase {

}