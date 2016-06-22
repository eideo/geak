package com.github.xsocket.geak.interceptor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.util.WebUtils;

import com.github.xsocket.geak.entity.Member;
import com.github.xsocket.geak.service.MemberService;
import com.github.xsocket.geak.util.GeakUtils;

public class CurrentMemberInterceptor extends HandlerInterceptorAdapter {
  
  //private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticateInterceptor.class);

  public static final String COOKIE_MEMBER_OPENID = AuthenticateInterceptor.COOKIE_MEMBER_OPENID;
  
  @Autowired
  private MemberService service; 

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    Member member = GeakUtils.getCurrentMember();
    if(member == null) {
      Cookie token = WebUtils.getCookie(request, COOKIE_MEMBER_OPENID);
      
      if(token != null) {
        String openId = token.getValue();
        member = service.loadMemberByOpenId(openId, null);
        GeakUtils.setCurrentMember(member);
        // 设置cookie
        token = new Cookie(AuthenticateInterceptor.COOKIE_MEMBER_OPENID, openId);
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
