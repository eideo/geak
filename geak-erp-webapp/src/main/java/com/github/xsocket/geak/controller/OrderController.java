package com.github.xsocket.geak.controller;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.xsocket.geak.entity.Order;
import com.github.xsocket.geak.entity.User;
import com.github.xsocket.geak.service.OrderService;
import com.github.xsocket.geak.util.GeakUtils;

/**
 * 订单相关业务的控制器。
 * 
 * @author MWQ
 */
@Controller
public class OrderController {
  
  @Autowired
  protected OrderService service;

  @ResponseBody
  @RequestMapping(value = "/orders", method = RequestMethod.GET, produces="application/json")
  public List<Order> list(
      @RequestParam(value="start", required=false) Long start,
      @RequestParam(value="end", required=false) Long end) {
    User user = GeakUtils.getCurrentUser();
    if(start == null) {
      start = System.currentTimeMillis();
    }
    if(end == null) {
      end = start;
    }
    return service.query(user.getCompany().getId(), new Date(start), new Date(end));
  }
  
  @ResponseBody
  @RequestMapping(value = "/orders/{id}", method = RequestMethod.GET, produces="application/json")
  public Order detail(@PathVariable("id") Integer id) {
    return service.loadOrder(id);
  }
  
  @ResponseBody
  @RequestMapping(value = "/orders", method = RequestMethod.POST, produces="application/json")
  public Order save(@RequestBody Order order) {
    return service.saveOrder(order);
  }
  
  // --------------------------------------------------
  // ------------------------- 修改订单状态
  // --------------------------------------------------
  
  @ResponseBody
  @RequestMapping(value = "/orders/{id}/entrance", method = RequestMethod.POST, produces="application/json")
  public Order entrance(@PathVariable("id") Integer id) {
    return service.entrance(id);
  }
  
  @ResponseBody
  @RequestMapping(value = "/orders/{id}/exit", method = RequestMethod.POST, produces="application/json")
  public Order exit(@PathVariable("id") Integer id) {
    return service.exit(id);
  }
  
  @ResponseBody
  @RequestMapping(value = "/orders/{id}/cancel", method = RequestMethod.POST, produces="application/json")
  public Order cancel(@PathVariable("id") Integer id) {
    return service.cancel(id);
  }
  
  @ResponseBody
  @RequestMapping(value = "/orders/{id}/pay", method = RequestMethod.POST, produces="application/json")
  public Order payed(@PathVariable("id") Integer id) {
    return service.pay(id);
  }
  
  @ResponseBody
  @RequestMapping(value = "/orders/{id}/unpay", method = RequestMethod.POST, produces="application/json")
  public Order unpayed(@PathVariable("id") Integer id) {
    return service.unpay(id);
  }
}
