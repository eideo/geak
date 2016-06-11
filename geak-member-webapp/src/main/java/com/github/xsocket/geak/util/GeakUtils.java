package com.github.xsocket.geak.util;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.github.xsocket.geak.entity.Member;

public class GeakUtils {
  
  public static Member getCurrentMember() {
    return (Member) getCurrentRequest().getAttribute(Member.class.getName());
  }
  
  public static void setCurrentMember(Member m) {
    getCurrentRequest().setAttribute(Member.class.getName(), m);
  }
  
  public static HttpServletRequest getCurrentRequest() {
    return  ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
  }
}
