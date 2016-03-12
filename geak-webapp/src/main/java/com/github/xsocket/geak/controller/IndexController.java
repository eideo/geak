package com.github.xsocket.geak.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.github.xsocket.geak.dao.GeakUserDao;
import com.github.xsocket.geak.entity.Company;
import com.github.xsocket.geak.entity.GeakUser;
import com.github.xsocket.geak.intercoptor.AuthenticateInterceptor;
import com.github.xsocket.geak.util.GeakUtils;
import com.github.xsocket.geak.util.WeixinUtils;

@Controller
public class IndexController {
  
  private static final Logger LOGGER = LoggerFactory.getLogger(IndexController.class);
  
  @Autowired
  protected GeakUserDao service;

  @RequestMapping(value = "/", method = RequestMethod.GET)
  public ModelAndView index(@RequestParam("code") String code, @RequestParam("state") String page,
      HttpServletRequest request, HttpServletResponse response) throws IOException {

    // 通过code参数获取员工ID
    String userId = WeixinUtils.getUserId(code);
    GeakUser user = service.selectById(userId);
    
    if(user == null) {
      LOGGER.debug("UserId为'{}'的用户不存在。", userId);
      response.sendRedirect("/error.html");
    } else {
      // @see WebContextUtils.getAuthenticatedUser(request);
      request.setAttribute(GeakUser.class.getName(), user);
      // 设置cookie
      Cookie token = new Cookie(AuthenticateInterceptor.COOKIE_ACCESS_TOKEN, userId);
      token.setPath("/");
      token.setMaxAge(3600 * 24 * 30);
      response.addCookie(token);
    }
    
    ModelAndView mv = new ModelAndView(page);
    mv.addObject("user", user);
    return mv;
  }
  
  @RequestMapping(value = "/{page}.html", method = RequestMethod.GET)
  public ModelAndView html(@PathVariable("page") String page, 
      // Integer 切换门店
      @RequestParam(value="company", required=false) Integer companyId) {
    // 在 AuthenticateInterceptor 中设置了 user 参数
    if(companyId != null) {
      GeakUser user = GeakUtils.getCurrentUser();
      List<Company> companies = user.getCompanies();
      if(companies != null && !companies.isEmpty()) {
        for(Company company : companies) {
          if(company.getId().equals(companyId)) {
            user.setCompany(company);
            // 更新所在门店
            service.update(user);
            break;
          }
        }
      }
    }
    
    return new ModelAndView(page);
  }
}

