package com.github.xsocket.geak.controller;

import java.util.Date;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.xsocket.geak.entity.Appointment;
import com.github.xsocket.geak.entity.Order;
import com.github.xsocket.geak.service.AppointmentService;
import com.github.xsocket.util.DefaultPair;
import com.github.xsocket.util.Pair;
import com.google.common.base.Strings;
import com.google.common.collect.Sets;

/**
 * 预约相关业务的控制器。
 * 
 * @author MWQ
 */
@Controller
public class AppointmentController {
  
  private static final String SEPRATOR_TIME_SPANS = ",";
  private static final String SEPRATOR_TIMES = "~";
  
  @Autowired
  protected AppointmentService service;

  @ResponseBody
  @RequestMapping(value = "/appointments", method = RequestMethod.GET, produces="application/json")
  public List<Appointment> query(
      @RequestParam(value="company", required=true) Integer companyId,
      @RequestParam(value="start", required=true) Long start,
      @RequestParam(value="end", required=true) Long end,
      @RequestParam(value="timespan", required=false) String timespan,
      @RequestParam(value="business", required=false) String business) {
    
    // 处理时间片段参数
    Set<Pair<String, String>> pairs = Sets.newHashSet();
    if(!Strings.isNullOrEmpty(timespan)) {
      String[] timespanArray = timespan.split(SEPRATOR_TIME_SPANS);
      for(String times : timespanArray) {
        if(!Strings.isNullOrEmpty(times)) {
          String[] timeArray = times.split(SEPRATOR_TIMES);
          if(timeArray != null && timeArray.length == 2) {
            pairs.add(DefaultPair.newPair(timeArray[0], timeArray[1]));
          }
        }
      }
    } 
    
    return service.query(companyId, new Date(start), new Date(end), 
            pairs.isEmpty() ? null : pairs, 
            Strings.isNullOrEmpty(business) ? null : business);
  }
  
  @ResponseBody
  @RequestMapping(value = "/appointments/{id}", method = RequestMethod.GET, produces="application/json")
  public Appointment detail(@PathVariable("id") Integer id) {
    return service.query(id);
  }
  
  @ResponseBody
  @RequestMapping(value = "/appointments/{id}/confirm", method = RequestMethod.POST, produces="application/json")
  public List<Order> confirm(
      @PathVariable("id") Integer id, 
      @RequestParam(value="datetime", required=true) Long datetime) {
    return service.confirm(id, new Date(datetime));
  }
  
  @ResponseBody
  @RequestMapping(value = "/appointments/{id}/cancel", method = RequestMethod.POST, produces="application/json")
  public Appointment cancel(@PathVariable("id") Integer id) {
    return service.cancel(id);
  }
  
  @ResponseBody
  @RequestMapping(value = "/appointments", method = RequestMethod.POST, produces="application/json")
  public Appointment save(@RequestBody Appointment appointment) {
    return service.save(appointment);
  }
}
