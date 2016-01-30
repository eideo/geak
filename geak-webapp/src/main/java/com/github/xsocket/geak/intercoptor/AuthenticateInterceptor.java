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
import com.google.common.base.Strings;

public class AuthenticateInterceptor extends HandlerInterceptorAdapter {
  
  private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticateInterceptor.class);

  public static final String COOKIE_ACCESS_TOKEN = "_geak_tk";
  
  private static final String REDIRECT_URL = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=wxb6ea60848a4abe21&redirect_uri=http://geak.weikuai01.com/&response_type=code&scope=snsapi_base&state=%s#wechat_redirect";
  
  @Autowired
  private GeakUserDao service;  

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

    // 测试环境
    //Cookie token = new Cookie(AuthenticateInterceptor.COOKIE_ACCESS_TOKEN, "2016012301");
    Cookie token = WebUtils.getCookie(request, COOKIE_ACCESS_TOKEN);
    if(token == null || Strings.isNullOrEmpty(token.getValue())) {
      // TODO 登陆后自动跳转到上次请求的页面
      LOGGER.debug("UserId Cookie不存在，需获取用户信息后才能访问应用。");
      String page = request.getParameter("page"); // appointments | orders
      response.sendRedirect(String.format(REDIRECT_URL, Strings.isNullOrEmpty(page) ? "appointments" : page));
      return false;
    } else {
      String userId = token.getValue();
      GeakUser user = service.selectById(userId);
      if(user == null) {
        LOGGER.debug("UserId为'{}'的用户不存在。", userId);
        response.sendRedirect("/error.html");
        return false;
      } else {
        // @see WebContextUtils.getAuthenticatedUser(request);
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

  @Override
  public void postHandle(HttpServletRequest request, HttpServletResponse response, 
      Object handler, ModelAndView mv) throws Exception {
    //为模型加入用户信息
    if(mv != null) {
      mv.addObject("user", request.getAttribute(GeakUser.class.getName()));
    }
  }

  @Override
  public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
      throws Exception {
    //LOGGER.debug("完成拦截");
    
  }
}
