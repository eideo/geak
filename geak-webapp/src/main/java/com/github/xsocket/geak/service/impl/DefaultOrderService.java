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
  public int save(Order order) {
    validate(order);
    LOGGER.debug("开始保存预约信息");
    
    // 首先登记客户信息
    Customer customer = registerOrderCustomer(order);
    order.setCustomer(customer);
    
    // 保存预约主体信息
    int updated = 0;
    Integer id = order.getId();
    if(id != null && id.intValue() > 0) {
      updated = orderDao.update(order);
      if(updated == 1) {
        logDao.insert(new ActionLog(ActionLog.ACTION_UPDATE, order));
      }
    } else {
      updated = orderDao.insert(order);
      if(updated == 1) {
        logDao.insert(new ActionLog(ActionLog.ACTION_INSERT, order));
      }
    }
    
    // 重新记录预约的支付和促销信息
    orderDao.deletePayments(order);
    orderDao.insertPayments(order, order.getPayments());
    
    orderDao.deletePromotions(order);
    orderDao.insertPromotions(order, order.getPromotions());
    
    LOGGER.debug("成功保存预约信息");
    return updated;
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
