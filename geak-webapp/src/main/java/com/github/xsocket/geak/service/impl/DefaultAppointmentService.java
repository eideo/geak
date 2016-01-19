package com.github.xsocket.geak.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.xsocket.dao.Pagination;
import com.github.xsocket.geak.dao.AppointmentDao;
import com.github.xsocket.geak.dao.CustomerDao;
import com.github.xsocket.geak.entity.Appointment;
import com.github.xsocket.geak.entity.Business;
import com.github.xsocket.geak.entity.Customer;
import com.github.xsocket.geak.service.AppointmentService;
import com.github.xsocket.util.DefaultPair;
import com.github.xsocket.util.Pair;
import com.google.common.collect.Sets;

/**
 * 默认预约服务实现类。
 * 
 * @author MWQ
 */
@Service
public class DefaultAppointmentService implements AppointmentService {
  
  private static final Logger LOGGER = LoggerFactory.getLogger(DefaultAppointmentService.class);
  
  @Autowired
  protected AppointmentDao appointmentDao;
  
  @Autowired
  protected CustomerDao customerDao;

  @Override
  public List<Appointment> query(Integer companyId, Date start, Date end, Integer[] business, Integer page) {
    // TODO 查询逻辑有问题
    Date begin = start == null ? new Date() : start;
    Date over = null;
    Pagination pagination = null;
    if(page == null) {
      // 不分页
    } else if(page == 1) {
      over = end;
      pagination = new Pagination(page);
    } else if(page > 1) {
      pagination = new Pagination(page);
      over = null;
    } else if(page < 0) {
      // 页码小于0，则反查小于begin日期的数据
      pagination = new Pagination(0 - page);
      over = begin;
    }
    
    // 返回查询数据
    if(business == null || business.length == 0) {
      return appointmentDao.selectByCompany(companyId, begin, over, pagination);
    } else {
      return appointmentDao.selectByBusiness(companyId, begin, over, business, pagination);
    }
  }

  @Override
  public int save(Appointment appointment) {
    validate(appointment);
    LOGGER.debug("开始保存预约信息");
    
    // 首先登记客户信息
    Customer customer = registerAppointmentCustomer(appointment);
    appointment.setCustomer(customer);
    
    // 保存预约主体信息
    int updated = 0;
    Integer id = appointment.getId();
    if(id != null && id.intValue() > 0) {
      updated = appointmentDao.update(appointment);
    } else {
      updated = appointmentDao.insert(appointment);
    }
    
    // 重新记录预约的业务(主题)信息
    appointmentDao.deleteRelation(appointment, null);
    Set<Pair<Appointment, Business>> sets = Sets.newHashSet();
    for(Business b : appointment.getBusinesses()) {
      sets.add(DefaultPair.newPair(appointment, b));
    }
    appointmentDao.insertRelation(sets);
    
    LOGGER.debug("成功保存预约信息");
    return updated;
  }
  
  /**
   * 登记预约中的客户信息。
   * @param appointment 预约
   * @return 客户信息
   */
  protected Customer registerAppointmentCustomer(Appointment appointment) {
    //查看客户信息是否已经存在
    Customer customer = appointment.getCustomer();
    List<Customer> customers = customerDao.selectByTelephone(customer.getTelephone());
    if(customers != null && !customers.isEmpty()) {
      for(Customer temp : customers) {
        if(temp.getName().equals(customer.getName())) {
          // 记录客户信息包含:电话+名字
          return temp;
        }
      }
    }
    
    // 登记客户信息
    customerDao.insert(customer);
    return customer;
  }
  
  protected void validate(Appointment appointment) {
    // TODO 验证参数
  }

}
