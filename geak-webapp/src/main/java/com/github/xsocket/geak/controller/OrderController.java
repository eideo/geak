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
import com.github.xsocket.geak.service.OrderService;

/**
 * 订单相关业务的控制器。
 * 
 * @author MWQ
 */
@Controller
public class OrderController {
  
  @Autowired
  protected OrderService service;

  /**
   * 根据相应的参数过滤预约数据
   * @param companyId 对应门店的标准(必填)
   * @param datetime 过滤预约数据的预约时间，默认显示其前后一小时的预约
   * @param business 所预约的业务
   * @param page 查询数据页码，默认为1
   * @return
   */
  @ResponseBody
  @RequestMapping(value = "/orders", method = RequestMethod.GET, produces="application/json")
  public List<Order> list(
      @RequestParam(value="company", required=true) Integer companyId,
      @RequestParam(value="datetime", required=true) Long datetime,
      @RequestParam(value="page", required=true) Integer page) {
    
    return service.query(companyId, new Date(datetime), null, page);
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
}
