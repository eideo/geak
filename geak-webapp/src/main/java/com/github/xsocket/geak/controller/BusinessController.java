package com.github.xsocket.geak.controller;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.xsocket.geak.dao.BusinessDao;
import com.github.xsocket.geak.entity.Business;
import com.github.xsocket.geak.entity.Company;

@Controller
public class BusinessController {
  
  @Autowired
  protected BusinessDao dao;

  @ResponseBody
  @RequestMapping(value = "/businesses", method = RequestMethod.GET, produces="application/json")
  public List<Business> list(@RequestParam(value="company") Integer companyId) {
    if(companyId == null || companyId <= 0) {
      // 参数不合理
      return Collections.emptyList();
    } else {
      Company company = new Company();
      company.setId(companyId);
      return dao.selectByCompany(company);
    }
  }
}
