package com.github.xsocket.geak.controller;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.xsocket.geak.entity.Appointment;

/**
 * 预约相关业务的控制器。
 * 
 * @author MWQ
 */
@Controller
public class AppointmentController {

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
      @RequestParam(value="datetime", required=false) 
        @DateTimeFormat(iso=ISO.DATE_TIME) Date datetime,
      @RequestParam(value="business", required=false) Integer[] business,
      @RequestParam(value="page", required=false) Integer page) {
    
    //System.out.println("");
    
    return Collections.emptyList();
  }
}
