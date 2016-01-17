package com.github.xsocket.geak.service;

import java.util.Date;
import java.util.List;

import com.github.xsocket.geak.entity.Appointment;

/**
 * 预约相关的业务服务。
 * 
 * @author MWQ
 */
public interface AppointmentService {

  List<Appointment> query(Integer companyId, Date start, Date end, Integer[] business, Integer page);
  
  /**
   * 保存预约数据。
   * <p>
   * 如果提供预约标识(id)数据，则对数据进行更新；否则进行新建操作。
   * 
   * @param appointment 预约数据
   * @return 新建/更新预约的数量
   */
  int save(Appointment appointment);
}
