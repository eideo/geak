package com.github.xsocket.geak.service;

import java.util.Date;
import java.util.List;
import java.util.Set;

import org.springframework.transaction.annotation.Transactional;

import com.github.xsocket.geak.entity.Order;
import com.github.xsocket.util.Pair;

public interface OrderService {
  
  /**
   * 根据条件查询订单信息。
   * @param companyId 订单所属公司(门店)的标识
   * @param start 
   * @param end
   * @param page 页码
   * @return
   */
  List<Order> query(Integer companyId, Date start, Date end, Integer page);
  
  List<Order> query(Integer companyId, Date start, Date end, Set<Pair<String, String>> timespan, String business);
  
  Order query(Integer id);
  
  @Transactional
  Order entrance(Integer id, Date date);
  
  @Transactional
  Order exit(Integer id, Date date);
  
  @Transactional
  Order cancel(Integer id);

  /**
   * 保存订单(接待)数据。
   * <p>
   * 如果提供订单标识(id)数据，则对数据进行更新；否则进行新建操作。
   * 
   * @param order 订单(接待)数据
   * @return 新建/更新订单(接待)的数量
   */
  @Transactional
  Order save(Order order);
}
