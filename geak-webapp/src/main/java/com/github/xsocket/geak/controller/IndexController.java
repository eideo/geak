package com.github.xsocket.geak.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.github.xsocket.geak.dao.GeakUserDao;
import com.github.xsocket.geak.entity.GeakUser;
import com.github.xsocket.geak.util.WeixinUtils;

@Controller
public class IndexController {
  
  // private static final Logger LOGGER = LoggerFactory.getLogger(IndexController.class);
  
  @Autowired
  protected GeakUserDao service;

  @RequestMapping(value = "/", method = RequestMethod.GET)
  public ModelAndView index(@RequestParam("code") String code, @RequestParam("state") String page) {
    // 通过code参数获取员工ID
    String userId = WeixinUtils.getUserId(code);
    GeakUser user = service.selectById(userId);
    
    // TODO 处理 user 获取失败的问题！！！
    
    ModelAndView mv = new ModelAndView(page);
    mv.addObject("user", user);
    return mv;
  }
  
  @RequestMapping(value = "/{page}.html", method = RequestMethod.GET)
  public ModelAndView html(@PathVariable("page") String page) {
    // 在 AuthenticateInterceptor 中设置了 user 参数
    return new ModelAndView(page);
  }
}

