package com.github.xsocket.geak.util;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.github.xsocket.geak.entity.GeakUser;

public class GeakUtils {

  public static GeakUser getCurrentUser() {
    return (GeakUser) getCurrentRequest().getAttribute(GeakUser.class.getName());
// FIXME 修改为正式情况
//    Company company = new Company();
//    company.setId(1);
//    company.setName("大南门店");
//    
//    GeakUser test = new GeakUser();
//    test.setId("1");
//    test.setName("麻文强");
//    test.setCompany(company);
//    return test;
  }
  
  public static HttpServletRequest getCurrentRequest() {
    return  ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
  }
}
