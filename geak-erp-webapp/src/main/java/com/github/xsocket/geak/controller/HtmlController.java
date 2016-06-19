package com.github.xsocket.geak.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.github.xsocket.geak.entity.Company;
import com.github.xsocket.geak.entity.User;
import com.github.xsocket.geak.interceptor.AuthenticateInterceptor;
import com.github.xsocket.geak.service.UserService;
import com.github.xsocket.geak.util.GeakUtils;
import com.github.xsocket.geak.util.WeixinUtils;

@Controller
public class HtmlController {
  
  private static final Logger LOGGER = LoggerFactory.getLogger(HtmlController.class);
  
  @Autowired
  protected UserService service;
  
  @RequestMapping(value = "/", method = RequestMethod.GET)
  public ModelAndView index(
      @RequestParam(value="code", required=true) String code, 
      HttpServletResponse response) throws IOException {

    // 通过code参数获取员工ID
    String userId = WeixinUtils.getUserId(code);
    User user = service.loadUserById(userId);
    
    if(user == null) {
      LOGGER.debug("UserId为'{}'的用户不存在。", userId);
      response.sendRedirect("/error.html");
    } else {
      GeakUtils.setCurrentUser(user);
      // 设置cookie
      Cookie token = new Cookie(AuthenticateInterceptor.COOKIE_USER_ID, userId);
      token.setPath("/");
      token.setMaxAge(3600 * 24 * 30);
      response.addCookie(token);
    }
    
    ModelAndView mv = new ModelAndView("index");
    mv.addObject("user", user);
    return mv;
  }
  
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
