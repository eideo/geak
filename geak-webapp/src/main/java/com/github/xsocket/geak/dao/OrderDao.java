package com.github.xsocket.geak.dao;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.github.xsocket.dao.BasicDao;
import com.github.xsocket.dao.Pagination;
import com.github.xsocket.geak.entity.Order;
import com.github.xsocket.geak.entity.OrderPayment;
import com.github.xsocket.geak.entity.OrderPromotion;

/**
 * 订单(接待)的数据访问对象。
 * 
 * @author MWQ
 */
@Repository
public interface OrderDao extends BasicDao<Order, Integer> {
  
  List<Order> selectByCompany(
      @Param("companyId") Integer companyId, 
      @Param("start") Date start,
      @Param("end") Date end, 
      Pagination page);
  
  /**
   * 插入订单相关的支付内容
   * @param order 订单
   * @param payments 支付内容
   * @return
   */
  int insertPayments(@Param("order") Order order, @Param("payments") List<OrderPayment> payments);
  
  /**
   * 删除订单相关的支付内容。
   * @param order 订单
   * @return
   */
  int deletePayments(Order order);
  
  /**
   * 插入订单相关的促销内容
   * @param order 订单
   * @param promotions 促销内容
   * @return
   */
  int insertPromotions(@Param("order") Order order, @Param("promotions") List<OrderPromotion> promotions);
  
  /**
   * 删除订单相关的促销内容。
   * @param order 订单
   * @return
   */
  int deletePromotions(Order order);

}
