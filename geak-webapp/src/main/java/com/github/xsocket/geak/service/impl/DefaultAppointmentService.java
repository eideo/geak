package com.github.xsocket.geak.service.impl;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.xsocket.dao.Pagination;
import com.github.xsocket.geak.dao.ActionLogDao;
import com.github.xsocket.geak.dao.AppointmentDao;
import com.github.xsocket.geak.dao.CustomerDao;
import com.github.xsocket.geak.dao.OrderDao;
import com.github.xsocket.geak.entity.ActionLog;
import com.github.xsocket.geak.entity.Appointment;
import com.github.xsocket.geak.entity.Business;
import com.github.xsocket.geak.entity.Customer;
import com.github.xsocket.geak.entity.Order;
import com.github.xsocket.geak.service.AppointmentService;
import com.github.xsocket.util.DefaultPair;
import com.github.xsocket.util.Pair;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

/**
 * 默认预约服务实现类。
 * 
 * @author MWQ
 */
@Service
public class DefaultAppointmentService implements AppointmentService {
  
  private static final Logger LOGGER = LoggerFactory.getLogger(DefaultAppointmentService.class);
  
  private static final String STATE_NEW = "NEW";
  private static final String STATE_CONFIRMED = "CONFIRMED";
  private static final String STATE_CANCELLED = "CANCELLED";
  
  @Autowired
  protected AppointmentDao appointmentDao;
  
  @Autowired
  protected OrderDao orderDao;
  
  @Autowired
  protected CustomerDao customerDao;
  
  @Autowired
  protected ActionLogDao logDao;

  @Override
  public List<Appointment> query(Integer companyId, Date start, Date end, String business, Integer page) {
    Date begin = start == null ? new Date() : start;
    Date over = null;
    Pagination pagination = null;
    if(page == null || page == 0) {
      over = end;
    } else if(page > 0) {
      // over = null; 分页的话不设置下限
      pagination = new Pagination(page);
    } else if(page < 0) {
      // 页码小于0，则反查小于begin日期的数据
      pagination = new Pagination(0 - page);
      over = begin;
      begin = null;
    }
    
    // 返回查询数据
    if(Strings.isNullOrEmpty(business)) {
      return appointmentDao.selectByCompany(companyId, begin, over, pagination);
    } else {
      return appointmentDao.selectByBusiness(companyId, begin, over, business, pagination);
    }
  }
  
  public Appointment query(Integer id) {
    return appointmentDao.selectById(id);
  }
  
  @Override
  public List<Order> confirm(Integer id, Date date) {
    Appointment appointment = appointmentDao.selectById(id);
    if(appointment == null) {
      LOGGER.warn("标识为 {} 的预约不存在，无法确认到场！", id);
      return Collections.emptyList();
    }
    if(!STATE_NEW.equals(appointment.getState())) {
      LOGGER.warn("预约\"{}\"的当前状态为\"{}\"，无法确认到场！", id, appointment.getState());
      //throw new IllegalArgumentException("预约状态不对，无法确认到场！");
      return Collections.emptyList();
    }
    
    appointment.setConfirmedDatetime(date);
    appointment.setState(STATE_CONFIRMED);
    int updated = appointmentDao.update(appointment);
    if(updated == 1) {
      // TODO 处理 updated 为其他值的情况
      logDao.insert(new ActionLog("CONFIRM", appointment));
    }
    
    // 为确认到场的预约，自动生成对应的接待
    List<Order> orders = Lists.newArrayList();
    List<Business> bs = appointment.getBusinesses();
    if(bs == null || bs.isEmpty()) {
      // 未定主题
      Order order = new Order();
      order.setAppointment(appointment);
      order.setCustomer(appointment.getCustomer());
      order.setCustomerCount(appointment.getCustomerCount());
      order.setCompany(appointment.getCompany());
      order.setState(STATE_NEW);
      order.setCreatedDatetime(date);
      updated = orderDao.insert(order);
      if(updated == 1) {
        orders.add(order);
        // TODO 处理 updated 为其他值的情况
        logDao.insert(new ActionLog("AUTO_INSERT", order));
      }
    } else {
      for(Business b : appointment.getBusinesses()) {
        Order order = new Order();
        order.setAppointment(appointment);
        order.setBusiness(b);
        order.setCustomer(appointment.getCustomer());
        order.setCustomerCount(appointment.getCustomerCount());
        order.setCompany(appointment.getCompany());
        order.setState(STATE_NEW);
        order.setCreatedDatetime(date);
        updated = orderDao.insert(order);
        if(updated == 1) {
          orders.add(order);
          // TODO 处理 updated 为其他值的情况
          logDao.insert(new ActionLog("AUTO_INSERT", order));
        }
      }
    }
    
    return orders;
  }
  
  @Override
  public Appointment cancel(Integer id) {
    Appointment appointment = appointmentDao.selectById(id);
    if(appointment == null) {
      LOGGER.warn("标识为 {} 的预约不存在，无法取消预约！", id);
      return null;
    }
    if(!STATE_NEW.equals(appointment.getState())) {
      LOGGER.warn("预约\"{}\"的当前状态为\"{}\"，无法取消预约！", id, appointment.getState());
      //throw new IllegalArgumentException("预约状态不对，无法确认到场！");
      return appointment;
    }
    
    appointment.setCancelledDatetime(new Date());
    appointment.setState(STATE_CANCELLED);
    int updated = appointmentDao.update(appointment);
    if(updated == 1) {
      // TODO 处理 updated 为其他值的情况
      logDao.insert(new ActionLog("CANCEL", appointment));
    } 
    
    return appointment;
  }

  @Override
  public Appointment save(Appointment appointment) {
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
      if(updated == 1) {
        logDao.insert(new ActionLog(ActionLog.ACTION_UPDATE, appointment));
      }
    } else {
      // 新预约
      appointment.setState(STATE_NEW);
      updated = appointmentDao.insert(appointment);
      if(updated == 1) {
        logDao.insert(new ActionLog(ActionLog.ACTION_INSERT, appointment));
      }
    }
    
    // 重新记录预约的业务(主题)信息
    appointmentDao.deleteRelation(appointment, null);
    List<Business> list = appointment.getBusinesses();
    if(list != null && !list.isEmpty()) {
      Set<Pair<Appointment, Business>> sets = Sets.newHashSet();
      for(Business b : appointment.getBusinesses()) {
        sets.add(DefaultPair.newPair(appointment, b));
      }
      appointmentDao.insertRelation(sets);
    }
    LOGGER.debug("成功保存预约信息");
    return appointmentDao.selectById(appointment.getId());
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
          if(!temp.getSex().equalsIgnoreCase(customer.getSex())) {
            temp.setSex(customer.getSex());
            customerDao.update(temp);
          }
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
