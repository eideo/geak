package com.github.xsocket.geak.controller;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.xsocket.geak.entity.Member;
import com.github.xsocket.geak.entity.Order;
import com.github.xsocket.geak.service.OrderService;
import com.github.xsocket.geak.util.GeakUtils;

/**
 * 订单相关业务的控制器。
 * 
 * @author MWQ
 */
@Controller
public class MemberOrderController {
  
  @Autowired
  protected OrderService service;

  @ResponseBody
  @RequestMapping(value = "/member/orders", method = RequestMethod.GET, produces="application/json")
  public List<Order> list() {
    Member member = GeakUtils.getCurrentMember();
    return service.listMemberOrders(member.getId());
  }
  
  @ResponseBody
  @RequestMapping(value = "/member/orders/{id}", method = RequestMethod.GET, produces="application/json")
  public Order detail(@PathVariable("id") Integer id) {
    Member member = GeakUtils.getCurrentMember();
    Order order = service.loadOrder(id);
    if(member.getId().equals(order.getMember().getId())) {
      return order;
    } else {
      return new Order();
    }
  }
  
  @RequestMapping(value = "/member/orders/link/{id}", method = RequestMethod.GET)
  public void link(@PathVariable("id") Integer id, HttpServletResponse response) {
    Member member = GeakUtils.getCurrentMember();
    service.linkOrder(id, member);
    // TODO
    //return service.loadOrder(id);
  }
  
  @ResponseBody
  @RequestMapping(value = "/member/orders/unlink/{id}", method = RequestMethod.POST, produces="application/json")
  public Order unlink(@PathVariable("id") Integer id) {
    Member member = GeakUtils.getCurrentMember();
    return service.unlinkOrder(id, member);
  }
  
  @ResponseBody
  @RequestMapping(value = "/member/orders/dpay/{id}", method = RequestMethod.POST, produces="application/json")
  public Order pay(@PathVariable("id") Integer id) {
    Member member = GeakUtils.getCurrentMember();
    return service.depositPay(id, member);
  }
}





