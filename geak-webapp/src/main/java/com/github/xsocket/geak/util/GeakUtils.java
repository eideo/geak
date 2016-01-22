package com.github.xsocket.geak.util;

import com.github.xsocket.geak.entity.Company;
import com.github.xsocket.geak.entity.GeakUser;

public class GeakUtils {

  public static GeakUser getCurrentUser() {
    // FIXME 修改为正式情况
    Company company = new Company();
    company.setId(1);
    company.setName("大南门店");
    
    GeakUser test = new GeakUser();
    test.setId("1");
    test.setName("麻文强");
    test.setCompany(company);
    return test;
  }
}
