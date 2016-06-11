package com.github.xsocket.geak.controller;

import java.io.IOException;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.github.xsocket.geak.entity.Member;
import com.github.xsocket.geak.interceptor.AuthenticateInterceptor;
import com.github.xsocket.geak.service.MemberService;
import com.github.xsocket.geak.service.WechatMpService;
import com.github.xsocket.geak.util.GeakUtils;

@Controller
public class HtmlController {
  
  @Autowired
  private WechatMpService wechatService;
  
  @Autowired
  private MemberService memberService;
  
  @Value("${webapp.host}")
  private String host;
  
  // 微信回调
  @RequestMapping(value = "/", method = RequestMethod.GET)
  public ModelAndView index(@RequestParam("code") String code,
      HttpServletRequest request, HttpServletResponse response) throws IOException {

    // 当前授权用户的openId
    String openId = wechatService.getUserOpenIdFromOAuth(code);
    
    Member member = memberService.loadMemberByOpenId(openId);
    GeakUtils.setCurrentMember(member);
    // 设置cookie
    Cookie token = new Cookie(AuthenticateInterceptor.COOKIE_MEMBER_OPENID, openId);
    token.setPath("/");
    token.setMaxAge(3600 * 24 * 30);
    response.addCookie(token);
    
    ModelAndView mv = new ModelAndView("index");
    mv.addObject("member", member);
    mv.addObject("config", wechatService.getJsConfig(String.format("http://%s/?code=%s&state=", host, code)));
    return mv;
  }
  
  @RequestMapping(value = "/index.html", method = RequestMethod.GET)
  public ModelAndView index(HttpServletRequest request, HttpServletResponse response) {
    ModelAndView mv = new ModelAndView("index");
    mv.addObject("config", wechatService.getJsConfig(String.format("http://%s/index.html", host)));
    return mv;
  }
}
