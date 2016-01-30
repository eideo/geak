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

import com.github.xsocket.geak.entity.Appointment;
import com.github.xsocket.geak.entity.Order;
import com.github.xsocket.geak.service.AppointmentService;
import com.google.common.base.Strings;

/**
 * 预约相关业务的控制器。
 * 
 * @author MWQ
 */
@Controller
public class AppointmentController {
  
  @Autowired
  protected AppointmentService service;

  /**
   * 根据相应的参数过滤预约数据
   * @param companyId 对应门店的标准(必填)
   * @param datetime 过滤预约数据的预约时间，默认显示其前后一小时的预约
   * @param business 所预约的业务
   * @param page 查询数据页码，默认为1
   * @return
   */
  @ResponseBody
  @RequestMapping(value = "/appointments", method = RequestMethod.GET, produces="application/json")
  public List<Appointment> list(
      @RequestParam(value="company", required=true) Integer companyId,
      @RequestParam(value="datetime", required=true) Long datetime,
      @RequestParam(value="business", required=false) String business,
      @RequestParam(value="page", required=false) Integer page) {
    
    // 不传时间，则从24小时前的预约开始显示
    if(Strings.isNullOrEmpty(business)) {
      // 不传 business 则以company为基准进行查询
      return service.query(companyId, new Date(datetime), null, business, page);
    } else {
      // 查询相关主题的近期预约
      long interval = 1L * 60 * 60 * 1000;   // 前后间隔是 1 小时
      return service.query(companyId, new Date(datetime - interval), new Date(datetime + interval), business, null);
    }
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
  @RequestMapping(value = "/appointments", method = RequestMethod.POST, produces="application/json")
  public Appointment save(@RequestBody Appointment appointment) {
    return service.save(appointment);
  }
}
