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

import com.alibaba.fastjson.JSONObject;
import com.github.xsocket.geak.entity.Member;
import com.github.xsocket.geak.interceptor.AuthenticateInterceptor;
import com.github.xsocket.geak.service.MemberService;
import com.github.xsocket.geak.service.WechatMpService;
import com.github.xsocket.geak.util.GeakUtils;

@Controller
public class HtmlController {
  
  private static final String REDIRECT_URL = 
      "https://open.weixin.qq.com/connect/oauth2/authorize?appid=%s&redirect_uri=http://%s/&response_type=code&scope=snsapi_userinfo#wechat_redirect";
      
  
  @Autowired
  private WechatMpService wechatService;
  
  @Autowired
  private MemberService memberService;

  @Value("${wechat.appid}")
  private String appId;
  @Value("${webapp.host}")
  private String host;
  
  // 微信回调
  @RequestMapping(value = "/", method = RequestMethod.GET)
  public ModelAndView index(
      @RequestParam(value="code", required=false) String code, 
      HttpServletResponse response) throws IOException {
    
    if(code == null) {
      response.sendRedirect(String.format(REDIRECT_URL, appId, host));
      return null;
    }

    // 当前授权用户的openId
    JSONObject json = wechatService.getUserOpenIdFromOAuth(code);
    
    String openId = json.getString("openid");
    String accessToken = json.getString("access_token");
    
    Member member = memberService.loadMemberByOpenId(openId, accessToken);
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
    mv.addObject("orderId", 0);
    return mv;
  }
  
  @RequestMapping(value = "/news.html", method = RequestMethod.GET)
  public ModelAndView news(HttpServletRequest request, HttpServletResponse response) {
    ModelAndView mv = new ModelAndView("news");
    return mv;
  }
}
