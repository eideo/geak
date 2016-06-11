package com.github.xsocket.geak.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HtmlController {
  
  @RequestMapping(value = "/index.html", method = RequestMethod.GET)
  public ModelAndView index(HttpServletRequest request, HttpServletResponse response) throws IOException {
    
    ModelAndView mv = new ModelAndView("index");
    //mv.addObject("user", user);
    return mv;
  }
}
