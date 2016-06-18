package com.github.xsocket.geak.dao;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.github.xsocket.geak.entity.Order;
import com.github.xsocket.geak.entity.OrderPayment;
import com.github.xsocket.geak.entity.OrderProduct;
import com.github.xsocket.geak.entity.OrderPromotion;

/**
 * 订单(接待)的数据访问对象。
 * 
 * @author MWQ
 */
@Repository
public interface OrderDao {
  
  List<Order> selectByCompany(
      @Param("companyId") Integer companyId, 
      @Param("start") Date start,
      @Param("end") Date end);
  
  Order selectById(Integer id);
  
  int insert(Order order);
  
  int update(Order order);
  
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
  
  /**
   * 插入订单相关的产品列表
   * @param order 订单
   * @param promotions 产品列表
   * @return
   */
  int insertProducts(@Param("order") Order order, @Param("promotions") List<OrderProduct> products);
  
  /**
   * 删除订单相关的产品列表。
   * @param order 订单
   * @return
   */
  int deleteProducts(Order order);

}
