package com.github.xsocket.geak.interceptor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.util.WebUtils;

import com.github.xsocket.geak.entity.User;
import com.github.xsocket.geak.service.UserService;
import com.github.xsocket.geak.util.GeakUtils;

public class AuthenticateInterceptor extends HandlerInterceptorAdapter {
  
  private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticateInterceptor.class);

  // geak_member_openid
  public static final String COOKIE_USER_ID = "_gui_";
  
  public static final String REDIRECT_URL = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=wxb6ea60848a4abe21&redirect_uri=http://geak-admin.weikuai01.com/&response_type=code&scope=snsapi_base#wechat_redirect";
     
  @Autowired
  private UserService service;

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

    // 测试环境
    //Cookie token = new Cookie(COOKIE_USER_ID, "2016012301");
    Cookie token = WebUtils.getCookie(request, COOKIE_USER_ID);
    if(token == null) {
      // TODO 登陆后自动跳转到上次请求的页面
      LOGGER.debug("Could not found user id, need oauth2 check first.");
      response.sendRedirect(REDIRECT_URL);
      return false;
    } else {
      String userId = token.getValue();
      User user = service.loadUserById(userId);
      GeakUtils.setCurrentUser(user);
      // 设置cookie
      token = new Cookie(AuthenticateInterceptor.COOKIE_USER_ID, userId);
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
      mv.addObject("user", GeakUtils.getCurrentUser());
    }
  }

  @Override
  public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
      throws Exception {
    //LOGGER.debug("完成拦截");
    
  }
}
