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

import com.github.xsocket.geak.entity.Order;
import com.github.xsocket.geak.service.OrderService;
import com.github.xsocket.util.DefaultPair;
import com.github.xsocket.util.Pair;
import com.google.common.base.Strings;
import com.google.common.collect.Sets;

/**
 * 订单相关业务的控制器。
 * 
 * @author MWQ
 */
@Controller
public class OrderController {
  
  private static final String SEPRATOR_TIME_SPANS = ",";
  private static final String SEPRATOR_TIMES = "~";
  
  @Autowired
  protected OrderService service;

  @ResponseBody
  @RequestMapping(value = "/orders", method = RequestMethod.GET, produces="application/json")
  public List<Order> list(
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
            String t2 = "之后".equals(timeArray[1]) ? "24:00" : timeArray[1];
            pairs.add(DefaultPair.newPair(timeArray[0], t2));
          }
        }
      }
    } 
    
    return service.query(companyId, new Date(start), new Date(end), 
        pairs.isEmpty() ? null : pairs, 
        Strings.isNullOrEmpty(business) ? null : business);
  }
  
  @ResponseBody
  @RequestMapping(value = "/orders/{id}", method = RequestMethod.GET, produces="application/json")
  public Order detail(@PathVariable("id") Integer id) {
    return service.query(id);
  }
  
  @ResponseBody
  @RequestMapping(value = "/orders", method = RequestMethod.POST, produces="application/json")
  public Order save(@RequestBody Order order) {
    return service.save(order);
  }
  
  @ResponseBody
  @RequestMapping(value = "/orders/{id}/entrance", method = RequestMethod.POST, produces="application/json")
  public Order entrance(
      @PathVariable("id") Integer id, 
      @RequestParam(value="datetime", required=true) Long datetime) {
    return service.entrance(id, new Date(datetime));
  }
  
  @ResponseBody
  @RequestMapping(value = "/orders/{id}/exit", method = RequestMethod.POST, produces="application/json")
  public Order exit(
      @PathVariable("id") Integer id, 
      @RequestParam(value="datetime", required=true) Long datetime) {
    return service.exit(id, new Date(datetime));
  }
  
  @ResponseBody
  @RequestMapping(value = "/orders/{id}/cancel", method = RequestMethod.POST, produces="application/json")
  public Order cancel(@PathVariable("id") Integer id) {
    return service.cancel(id);
  }
}
