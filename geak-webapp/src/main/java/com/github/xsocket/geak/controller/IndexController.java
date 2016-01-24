package com.github.xsocket.geak.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class IndexController {

  @RequestMapping(value = "/", method = RequestMethod.GET)
  public ModelAndView index(@RequestParam("code") String code, @RequestParam("state") String page) {
    // TODO 通过code参数获取员工信息
    ModelAndView view = new ModelAndView(page);
    view.addObject("userId", code);
    return view;
  }
}

