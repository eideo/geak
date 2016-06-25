package com.github.xsocket.geak.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.github.xsocket.geak.entity.Company;
import com.github.xsocket.geak.entity.User;
import com.github.xsocket.geak.interceptor.AuthenticateInterceptor;
import com.github.xsocket.geak.service.SmsService;
import com.github.xsocket.geak.service.UserService;
import com.github.xsocket.geak.util.GeakUtils;
import com.github.xsocket.geak.util.WeixinUtils;
import com.google.common.base.Strings;

@Controller
public class HtmlController {
  
  private static final Logger LOGGER = LoggerFactory.getLogger(HtmlController.class);
  
  @Autowired
  protected UserService service;
  
  @Autowired
  SmsService smsService;
  
  @RequestMapping(value = "/", method = RequestMethod.GET)
  public ModelAndView index(
      @RequestParam(value="code", required=false) String code, 
      HttpServletResponse response) throws IOException {
    
    if(code == null) {
      response.sendRedirect(AuthenticateInterceptor.REDIRECT_URL);
      return null;
    }

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
  
  @RequestMapping(value = "/login.html", method = RequestMethod.GET)
  public ModelAndView toLogin() {
    return new ModelAndView("login");
  }
  
  @RequestMapping(value = "/login.html", method = RequestMethod.POST)
  public ModelAndView doLogin(
      @ModelAttribute("account") String account,
      @ModelAttribute("phone") String phone,
      @ModelAttribute("captcha") Integer captcha,
      HttpServletResponse response) {
    
    ModelAndView login = new ModelAndView("login");
    login.addObject("account", account);
    login.addObject("phone", phone);
    
    Integer _captcha = smsService.fetchCaptcha(phone);
    if(_captcha == null || !_captcha.equals(captcha)) {
      login.addObject("error", "验证码错误");
      return login;
    }
    
    User user = service.loadUserById(account);
    if(user == null) {
      login.addObject("error", "工号错误");
      return login;
    }
    
    // 员工预留手机号，则进行验证
    if(!Strings.isNullOrEmpty(user.getPhone()) && !user.getPhone().equals(phone)) {
      login.addObject("error", "手机号错误:与预留手机号不一致!");
      return login;
    }
    
    
    // 登陆认证成功
    GeakUtils.setCurrentUser(user);
    Cookie token = new Cookie(AuthenticateInterceptor.COOKIE_USER_ID, user.getId());
    token.setPath("/");
    token.setMaxAge(3600 * 24 * 1);
    response.addCookie(token);
    try {
      response.sendRedirect("/index.html");
      return null;
    } catch (IOException e) {
      LOGGER.warn("Login Error.", e);
      login.addObject("error", e.getMessage());
      return login;
    }
  }

  @ResponseBody
  @RequestMapping(value = "/login/captcha", method = RequestMethod.GET, produces="application/json")
  public Map<String, String> sendCaptcha(@RequestParam(value="phone", required=true) String phone) {
    Map<String, String> ret = new HashMap<String, String>();
    try {
      smsService.sendCaptcha(phone);
    } catch(Exception e) {
      ret.put("error", e.getMessage());
    }
    return ret;
  }
}
