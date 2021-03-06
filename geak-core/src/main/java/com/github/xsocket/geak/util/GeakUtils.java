package com.github.xsocket.geak.util;

import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.github.xsocket.geak.entity.Member;
import com.github.xsocket.geak.entity.User;

public class GeakUtils {
  
  public static Member getCurrentMember() {
    return (Member) getCurrentRequest().getAttribute(Member.class.getName());
  }
  
  public static void setCurrentMember(Member m) {
    getCurrentRequest().setAttribute(Member.class.getName(), m);
  }
  
  public static User getCurrentUser() {
    return (User) getCurrentRequest().getAttribute(User.class.getName());
  }
  
  public static void setCurrentUser(User m) {
    getCurrentRequest().setAttribute(User.class.getName(), m);
  }
  
  public static HttpServletRequest getCurrentRequest() {
    return  ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
  }
  
  public static String getRequestIPAddress(HttpServletRequest request) {
    String ip = request.getHeader("x-forwarded-for");
    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
      ip = request.getHeader("Proxy-Client-IP");
    }
    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
      ip = request.getHeader("WL-Proxy-Client-IP");
    }
    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
      ip = request.getRemoteAddr();
      if (ip.equals("127.0.0.1")) {
        // 根据网卡取本机配置的IP
        InetAddress inet = null;
        try {
          inet = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
          e.printStackTrace();
        }
        ip = inet.getHostAddress();
      }
    }
    // 对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
    if (ip != null && ip.length() > 15) {
      if (ip.indexOf(",") > 0) {
        ip = ip.substring(0, ip.indexOf(","));
      }
    }
    return ip;
  }
}
