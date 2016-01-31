package com.github.xsocket.geak.service.impl;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.xsocket.dao.Pagination;
import com.github.xsocket.geak.dao.ActionLogDao;
import com.github.xsocket.geak.dao.CustomerDao;
import com.github.xsocket.geak.dao.OrderDao;
import com.github.xsocket.geak.entity.ActionLog;
import com.github.xsocket.geak.entity.Customer;
import com.github.xsocket.geak.entity.Order;
import com.github.xsocket.geak.service.OrderService;

/**
 * 默认订单服务实现类。
 * 
 * @author MWQ
 */
@Service
public class DefaultOrderService implements OrderService {
  
  private static final Logger LOGGER = LoggerFactory.getLogger(DefaultOrderService.class);
  
  private static final String STATE_NEW = "NEW";
  private static final String STATE_PAYED = "PAYED";
  private static final String STATE_ENTRANCED = "ENTRANCED";
  private static final String STATE_EXITED = "EXITED";
  
  @Autowired
  protected OrderDao orderDao;
  
  @Autowired
  protected CustomerDao customerDao;
  
  @Autowired
  protected ActionLogDao logDao;

  @Override
  public List<Order> query(Integer companyId, Date start, Date end, Integer page) {
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
    return orderDao.selectByCompany(companyId, begin, over, pagination);
  }

  @Override
  public Order query(Integer id) {
    return orderDao.selectById(id);
  }
  
  @Override
  public Order entrance(Integer id, Date date) {
    Order order = orderDao.selectById(id);
    if(order == null) {
      LOGGER.warn("标识为 {} 的订单不存在，无法确认入场！", id);
      return null;
    }
    if(!STATE_PAYED.equals(order.getState())) {
      LOGGER.warn("订单\"{}\"的当前状态为\"{}\"，请先进行支付后再确认入场！", id, order.getState());
      //throw new IllegalArgumentException("预约状态不对，无法确认到场！");
      return order;
    }
    
    order.setEntranceDatetime(date);
    order.setState(STATE_ENTRANCED);
    int updated = orderDao.update(order);
    if(updated == 1) {
      // TODO 处理 updated 为其他值的情况
      logDao.insert(new ActionLog("ENTRANCE", order));
    }
    
    return order;
  }
  
  @Override
  public Order exit(Integer id, Date date) {
    Order order = orderDao.selectById(id);
    if(order == null) {
      LOGGER.warn("标识为 {} 的订单不存在，无法确认离场！", id);
      return null;
    }
    if(!STATE_ENTRANCED.equals(order.getState())) {
      LOGGER.warn("订单\"{}\"的当前状态为\"{}\"，请先确认入场后再确认离场！", id, order.getState());
      //throw new IllegalArgumentException("预约状态不对，无法确认到场！");
      return order;
    }
    
    order.setExitDatetime(date);
    order.setState(STATE_EXITED);
    int updated = orderDao.update(order);
    if(updated == 1) {
      // TODO 处理 updated 为其他值的情况
      logDao.insert(new ActionLog("EXIT", order));
    }
    
    return order;
  }

  @Override
  public Order save(Order order) {
    validate(order);
    LOGGER.debug("开始保存订单信息");
    
    // 首先登记客户信息
    Customer customer = registerOrderCustomer(order);
    order.setCustomer(customer);
    
    
    // 保存订单主体信息
    int updated = 0;
    Integer id = order.getId();
    if(id != null && id.intValue() > 0) {
      // 只有入场前的订单才可以修改
      if(!STATE_NEW.equals(order.getState()) && !STATE_PAYED.equals(order.getState())) {
        LOGGER.warn("订单\"{}\"的当前状态为\"{}\"，不能进行更新！", id, order.getState());
        return orderDao.selectById(order.getId());
      }

      if(order.getPayments() == null || order.getPayments().isEmpty()) {
        order.setState(STATE_NEW);
      } else {
        order.setState(STATE_PAYED);
      }
      updated = orderDao.update(order);
      if(updated == 1) {
        logDao.insert(new ActionLog(ActionLog.ACTION_UPDATE, order));
      }
    } else {
      order.setCreatedDatetime(new Date());
      if(order.getPayments() == null || order.getPayments().isEmpty()) {
        order.setState(STATE_NEW);
      } else {
        order.setState(STATE_PAYED);
      }
      
      updated = orderDao.insert(order);
      if(updated == 1) {
        logDao.insert(new ActionLog(ActionLog.ACTION_INSERT, order));
      }
    }
    
    // 重新记录预约的支付和促销信息
    orderDao.deletePayments(order);
    if(order.getPayments() != null && !order.getPayments().isEmpty()) {
      orderDao.insertPayments(order, order.getPayments());
    }
    
    orderDao.deletePromotions(order);
    if(order.getPromotions() != null && !order.getPromotions().isEmpty()) {
      orderDao.insertPromotions(order, order.getPromotions());
    }
    
    LOGGER.debug("成功保存预约信息");
    return orderDao.selectById(order.getId());
  }
  
  /**
   * 登记预约中的客户信息。
   * @param order 预约
   * @return 客户信息
   */
  protected Customer registerOrderCustomer(Order order) {
    //查看客户信息是否已经存在
    Customer customer = order.getCustomer();
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
  
  protected void validate(Order appointment) {
    // TODO 验证参数
  }

}
