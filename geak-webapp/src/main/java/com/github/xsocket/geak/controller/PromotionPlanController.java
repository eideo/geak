package com.github.xsocket.geak.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.xsocket.geak.dao.PromotionPlanDao;
import com.github.xsocket.geak.entity.PromotionPlan;

@Controller
public class PromotionPlanController {
  
  @Autowired
  protected PromotionPlanDao dao;

  @ResponseBody
  @RequestMapping(value = "/promotions", method = RequestMethod.GET, produces="application/json")
  public List<PromotionPlan> list() {
    return dao.selectAll();
  }
}
