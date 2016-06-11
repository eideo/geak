package com.github.xsocket.geak.interceptor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.util.WebUtils;

import com.github.xsocket.geak.entity.Member;
import com.github.xsocket.geak.service.MemberService;
import com.github.xsocket.geak.util.GeakUtils;

public class AuthenticateInterceptor extends HandlerInterceptorAdapter {
  
  private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticateInterceptor.class);

  // geak_member_openid
  public static final String COOKIE_MEMBER_OPENID = "_gmo_";
  
  private static final String REDIRECT_URL = 
      "https://open.weixin.qq.com/connect/oauth2/authorize?appid=%s&redirect_uri=http://%s/&response_type=code&scope=snsapi_base#wechat_redirect";
      
  @Autowired
  private MemberService service;
  @Value("${webapp.host}")
  private String host;
  @Value("${wechat.appid}")
  private String appId;

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

    // 测试环境
    //Cookie token = new Cookie(COOKIE_MEMBER_OPENID, "oAWv4jnVwh1lunG6i9gng9z19zlg");
    Cookie token = WebUtils.getCookie(request, COOKIE_MEMBER_OPENID);
    if(token == null) {
      // TODO 登陆后自动跳转到上次请求的页面
      LOGGER.debug("Could not found member openId, need oauth2 check first.");
      response.sendRedirect(String.format(REDIRECT_URL, appId, host));
      return false;
    } else {
      String openId = token.getValue();
      Member member = service.loadMemberByOpenId(openId);
      GeakUtils.setCurrentMember(member);
      // 设置cookie
      token = new Cookie(AuthenticateInterceptor.COOKIE_MEMBER_OPENID, openId);
      token.setPath("/");
      token.setMaxAge(3600 * 24 * 30);
      response.addCookie(token);
      return true;
    }
  }

  @Override
  public void postHandle(HttpServletRequest request, HttpServletResponse response, 
      Object handler, ModelAndView mv) throws Exception {
    //为模型加入用户信息
    if(mv != null) {
      mv.addObject("member", GeakUtils.getCurrentMember());
    }
  }

  @Override
  public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
      throws Exception {
    //LOGGER.debug("完成拦截");
    
  }
}
