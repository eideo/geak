package com.github.xsocket.geak.controller;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.xsocket.geak.dao.StatsDao;
import com.github.xsocket.geak.entity.Stats;

/**
 * 统计相关业务的控制器。
 * 
 * @author MWQ
 */
@Controller
public class StatsController {
  
  @Autowired
  protected StatsDao service;

  @ResponseBody
  @RequestMapping(value = "/stats/revenue", method = RequestMethod.GET, produces="application/json")
  public List<Stats> revenue(
      @RequestParam(value="start", required=false) Long start,
      @RequestParam(value="end", required=false) Long end) {
    
    return service.selectRevenue(
        start == null ? null : new Date(start), 
        end == null ? null : new Date(end));
  }
  
  @ResponseBody
  @RequestMapping(value = "/stats/status", method = RequestMethod.GET, produces="application/json")
  public List<Stats> status(
      @RequestParam(value="start", required=false) Long start,
      @RequestParam(value="end", required=false) Long end) {
    
    return service.selectStatus(
        start == null ? new Date() : new Date(start), 
        end == null ? new Date() : new Date(end));
  }
  
}
