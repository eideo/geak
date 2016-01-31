package com.github.xsocket.geak.intercoptor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.util.WebUtils;

import com.github.xsocket.geak.dao.GeakUserDao;
import com.github.xsocket.geak.entity.GeakUser;
import com.github.xsocket.geak.util.GeakUtils;
import com.google.common.base.Strings;

public class CurrentUserInterceptor extends HandlerInterceptorAdapter {
  
  private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticateInterceptor.class);

  public static final String COOKIE_ACCESS_TOKEN = "_geak_tk";
  
  @Autowired
  private GeakUserDao service;  

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    GeakUser geak = GeakUtils.getCurrentUser();
    if(geak == null) {
      Cookie token = WebUtils.getCookie(request, COOKIE_ACCESS_TOKEN);
      
      if(token != null && !Strings.isNullOrEmpty(token.getValue())) {
        String userId = token.getValue();
        GeakUser user = service.selectById(userId);
        if(user != null) {
          LOGGER.debug("记录当前访问用户 - {}:{}", user.getName(), user.getId());
          request.setAttribute(GeakUser.class.getName(), user);
          // 设置cookie
          token = new Cookie(AuthenticateInterceptor.COOKIE_ACCESS_TOKEN, userId);
          token.setPath("/");
          token.setMaxAge(3600 * 24 * 30);
          response.addCookie(token);
          return true;
        }
      }
    }
    return true;
  }

  @Override
  public void postHandle(HttpServletRequest request, HttpServletResponse response, 
      Object handler, ModelAndView mv) throws Exception {
    // do nothing
  }

  @Override
  public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
      throws Exception {
    //LOGGER.debug("完成拦截");
    
  }
}
