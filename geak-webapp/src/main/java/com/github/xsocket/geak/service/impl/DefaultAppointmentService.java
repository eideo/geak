package com.github.xsocket.geak.service.impl;

import java.util.Date;
import java.util.List;

import com.github.xsocket.geak.entity.Appointment;
import com.github.xsocket.geak.service.AppointmentService;

/**
 * 默认预约服务实现类。
 * 
 * @author MWQ
 */
public class DefaultAppointmentService implements AppointmentService {

  @Override
  public List<Appointment> query(Integer companyId, Date start, Date end, Integer[] business, Integer page) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public int save(Appointment appointment) {
    // TODO Auto-generated method stub
    return 0;
  }

}
