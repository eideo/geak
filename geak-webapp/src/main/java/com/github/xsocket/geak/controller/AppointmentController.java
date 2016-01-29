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
import com.github.xsocket.geak.service.AppointmentService;

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
      @RequestParam(value="datetime", required=false) Long datetime,
      @RequestParam(value="business", required=false) Integer[] business,
      @RequestParam(value="page", required=false) Integer page) {
    
    // 不传时间，则从24小时前的预约开始显示
    Date pivot = datetime == null ? new Date(System.currentTimeMillis() - 24 * 60 * 60 * 1000L) : new Date(datetime);
    Integer pageNo = (page == null || page == 0) ? 1 : page;
    return service.query(companyId, pivot, null, business, pageNo);
  }
  
  @ResponseBody
  @RequestMapping(value = "/appointments/{id}", method = RequestMethod.GET, produces="application/json")
  public Appointment detail(@PathVariable("id") Integer id) {
    return service.query(id);
  }
  
  @ResponseBody
  @RequestMapping(value = "/appointments", method = RequestMethod.POST, produces="application/json")
  public Appointment save(@RequestBody Appointment appointment) {
    
    service.save(appointment);
    
    return appointment;
  }
}
