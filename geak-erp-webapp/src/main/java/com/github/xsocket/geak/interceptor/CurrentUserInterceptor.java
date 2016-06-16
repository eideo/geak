package com.github.xsocket.geak.interceptor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.util.WebUtils;

import com.github.xsocket.geak.entity.User;
import com.github.xsocket.geak.service.UserService;
import com.github.xsocket.geak.util.GeakUtils;

public class CurrentUserInterceptor extends HandlerInterceptorAdapter {
  
  //private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticateInterceptor.class);

  public static final String COOKIE_USER_ID = AuthenticateInterceptor.COOKIE_USER_ID;
  
  @Autowired
  private UserService service; 

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    User user = GeakUtils.getCurrentUser();
    if(user == null) {
      Cookie token = WebUtils.getCookie(request, COOKIE_USER_ID);
      
      if(token != null) {
        String userId = token.getValue();
        user = service.loadUserById(userId);
        GeakUtils.setCurrentUser(user);
        // 设置cookie
        token = new Cookie(COOKIE_USER_ID, userId);
        token.setPath("/");
        token.setMaxAge(3600 * 24 * 30);
        response.addCookie(token);
        return true;
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
