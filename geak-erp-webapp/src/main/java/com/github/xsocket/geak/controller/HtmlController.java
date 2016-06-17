package com.github.xsocket.geak.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.github.xsocket.geak.entity.Company;
import com.github.xsocket.geak.entity.User;
import com.github.xsocket.geak.service.UserService;
import com.github.xsocket.geak.util.GeakUtils;

@Controller
public class HtmlController {
  
  @Autowired
  protected UserService service;
  
  @RequestMapping(value = "/index.html", method = RequestMethod.GET)
  public ModelAndView html(@RequestParam(value="company", required=false) Integer companyId) {
    // 在 AuthenticateInterceptor 中设置了 user 参数
    if(companyId != null) {
      User user = GeakUtils.getCurrentUser();
      List<Company> companies = service.listCompanyByUser(user.getId());
      if(companies != null && !companies.isEmpty()) {
        for(Company company : companies) {
          if(company.getId().equals(companyId)) {
            user.setCompany(company);
            // 更新所在门店
            service.modifyUser(user);
            break;
          }
        }
      }
    }
    return new ModelAndView("index");
  }
}
