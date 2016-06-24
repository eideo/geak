package com.github.xsocket.geak.service.impl;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.xsocket.geak.dao.ActionLogDao;
import com.github.xsocket.geak.dao.MemberDao;
import com.github.xsocket.geak.dao.OrderDao;
import com.github.xsocket.geak.entity.ActionLog;
import com.github.xsocket.geak.entity.Member;
import com.github.xsocket.geak.entity.Order;
import com.github.xsocket.geak.entity.OrderPayment;
import com.github.xsocket.geak.entity.OrderProduct;
import com.github.xsocket.geak.entity.OrderPromotion;
import com.github.xsocket.geak.service.OrderService;
import com.github.xsocket.geak.util.GeakUtils;

/**
 * 默认订单服务实现类。
 * 
 * @author MWQ
 */
@Service
public class DefaultOrderService implements OrderService {
  
  private static final Logger LOGGER = LoggerFactory.getLogger(DefaultOrderService.class);
  
  private static final String STATE_NEW = "NEW";
  private static final String STATE_UNPAYED = "UNPAYED";
  private static final String STATE_PAYED = "PAYED";
  private static final String STATE_ENTRANCED = "ENTRANCED";
  private static final String STATE_EXITED = "EXITED";
  private static final String STATE_CANCELLED = "CANCELLED";
  
  private static final String CONTENT_SPLIT = " | ";
  
  @Autowired
  protected OrderDao orderDao;
  
  @Autowired
  protected MemberDao memberDao;
  
  @Autowired
  protected ActionLogDao logDao;

  @Override
  public List<Order> query(Integer companyId, Date start, Date end) {
    return orderDao.selectByCompany(companyId, start, end);
  }
  
  @Override
  public List<Order> listMemberOrders(Integer memberId) {
    return orderDao.selectByMember(memberId);
  }

  @Override
  public Order loadOrder(Integer id) {
    return orderDao.selectById(id);
  }
  
  @Override
  public Order depositPay(Integer id, Member member) {
    Order order = orderDao.selectById(id);
    Integer memberId = order.getMember().getId();
    String state = order.getState();
    if(STATE_NEW.equals(state) || STATE_UNPAYED.equals(state)) {
      if(member.getId().equals(memberId)) {
        
        // 判断订单是否有工厂门票，并确认优惠金额
        boolean hasDiscount = false;
        for(OrderProduct op : order.getProducts()) {
          if(op.getType().equals("工厂门票")) {
            hasDiscount = true;
            break;
          }
        }
        
        int realAmount = order.getAmount() - (hasDiscount ? 10 : 0);
        if(realAmount < 0) {
          String msg = String.format("Order [%d] count not be deposit pay, its discount amount is (%d) less than 0", 
              order.getId(), realAmount);
          throw new IllegalArgumentException(msg);
        }
        
        int balance = member.getBalance();
        if(realAmount > balance) {
          String msg = String.format("Order [%d] count not be deposit pay, its discount amount is (%d) large than member balance (%d)", 
              order.getId(), realAmount, balance);
          throw new IllegalArgumentException(msg);
        }
        
        LOGGER.debug("Starting deposit pay order...");
        
        
        order.setAmount(realAmount);
        order.setPaymentDate(new Date());
        order.setPaymentMode("1");
        order.setState(STATE_PAYED);
        orderDao.update(order);
        
        // 更新余额
        member.setBalance(member.getBalance() - realAmount);
        memberDao.update(member);
        LOGGER.debug("Finished deposit pay order.");
        return order;
      } else {
        String msg = String.format("Order [%d] count not be deposit pay, its memberId is (%d) not (%d)", 
            order.getId(), memberId, member.getId());
        throw new IllegalArgumentException(msg);
      }
    } else {
      String msg = String.format("Order [%d] could not be deposit pay, its state is (%s)", order.getId(), state);
      throw new IllegalArgumentException(msg);
    }
  }
  
  @Override
  public Order linkOrder(Integer id, Member member) {
    Order order = orderDao.selectById(id);
    Integer memberId = order.getMember().getId();
    String state = order.getState();
    if(STATE_NEW.equals(state) || STATE_UNPAYED.equals(state)) {
      if(member.getId().equals(memberId)) {        
        return order;        
      } else if(memberId == 0) {
        LOGGER.debug("Starting link order...");
        // 同一个用户才能解除关联
        order.setMember(member);
        order.setState(STATE_UNPAYED);
        orderDao.update(order);
        LOGGER.debug("Finished link order.");
        return order;
      } else {
        String msg = String.format("Order [%d] count not be link, its memberId is (%d) not (%d)", 
            order.getId(), memberId, member.getId());
        throw new IllegalArgumentException(msg);
      }
    } else {
      String msg = String.format("Order [%d] could not be link, its state is (%s)", order.getId(), state);
      throw new IllegalArgumentException(msg);
    } 
  }
  
  @Override
  public Order unlinkOrder(Integer id, Member member) {
    Order order = orderDao.selectById(id);
    Integer memberId = order.getMember().getId();
    String state = order.getState();
    if(STATE_NEW.equals(state) || STATE_UNPAYED.equals(state)) {
      if(member.getId().equals(memberId)) {
        LOGGER.debug("Starting unlink order...");
        // 同一个用户才能解除关联
        Member temp = order.getMember();
        temp.setId(0);
        temp.setName("");
        temp.setPhone("");
        temp.setSex("S");
        order.setMember(temp);
        order.setState(STATE_UNPAYED);
        orderDao.update(order);
        LOGGER.debug("Finished unlink order.");
        
        return order;        
      } else {
        String msg = String.format("Order [%d] not be unlink, its memberId is (%d) not (%d)", 
            order.getId(), memberId, member.getId());
        throw new IllegalArgumentException(msg);
      }
    } else {
      String msg = String.format("Order [%d] could not be unlink, its state is (%s)", order.getId(), state);
      throw new IllegalArgumentException(msg);
    }
  }
  
  

  @Override
  public Order saveOrder(Order order) {
    validate(order);
    
    // 订单状态为取消、已进场、已离场时，不能修改
    if(STATE_CANCELLED.equals(order.getState())
        || STATE_ENTRANCED.equals(order.getState())
        || STATE_EXITED.equals(order.getState())) {
      String msg = String.format("Order [%d] could not be update, its state is (%s)", order.getId(), order.getState());
      throw new IllegalArgumentException(msg);
    }

    // 确认订单状态
    if(order.getPaymentDate() != null) {
      order.setState(STATE_PAYED);
    } else {
      order.setState(STATE_NEW);
    }

    LOGGER.debug("Starting save order...");
    // 保存订单主体信息
    int updated = 0;
    Integer id = order.getId();
    if(id != null && id.intValue() > 0) {
      updated = orderDao.update(order);
      if(updated == 1) {
        logDao.insert(new ActionLog(ActionLog.ACTION_UPDATE, order));
      }
    } else {
      order.setCompany(GeakUtils.getCurrentUser().getCompany());
      order.setCreatedDate(new Date());
      updated = orderDao.insert(order);
      if(updated == 1) {
        logDao.insert(new ActionLog(ActionLog.ACTION_INSERT, order));
      }
    }
    
    // 重新记录预约的支付和促销信息
    orderDao.deleteProducts(order);
    orderDao.insertProducts(order, order.getProducts());
    
    orderDao.deletePayments(order);
    if(order.getPayments() != null && !order.getPayments().isEmpty()) {
      orderDao.insertPayments(order, order.getPayments());
    }
    
    orderDao.deletePromotions(order);
    if(order.getPromotions() != null && !order.getPromotions().isEmpty()) {
      orderDao.insertPromotions(order, order.getPromotions());
    }
    
    LOGGER.debug("Finished save order.");
    return orderDao.selectById(order.getId());
  }
  
  protected void validate(Order order) {
    // TODO 验证参数
    List<OrderProduct> products = order.getProducts();
    if(products == null || products.isEmpty()) {
      throw new IllegalArgumentException("Order's product should not be null.");
    }
    
    // 计算产品数量和主产品名称
    int memberCount = 0;
    List<OrderProduct> prods = new ArrayList<OrderProduct>();
    StringBuilder sb = new StringBuilder(CONTENT_SPLIT);
    for(OrderProduct p : products) {
      if(p.getCount() != null && p.getCount() > 0) {
        prods.add(p);
        
        if(p.getType().contains("门票")) {
          memberCount += p.getCount();
          sb.append(p.getAlias());
        }
      }
    }
    order.setProducts(prods);
    order.setMemberCount(memberCount);
    order.setContent(sb.substring(CONTENT_SPLIT.length()));
    
    // 解析优惠券
    List<OrderPromotion> promotions = order.getPromotions();
    NumberFormat format = NumberFormat.getIntegerInstance();
    if(promotions != null && !promotions.isEmpty()) {
      for(OrderPromotion op : promotions) {
        try {
          op.setCount(format.parse(op.getNote()).intValue()) ;
        } catch (ParseException e) {
          op.setCount(0);
        }
      }
    }
    
    // 解析支付细节
    List<OrderPayment> list = order.getPayments();
    if(list != null && !list.isEmpty()) {
      List<OrderPayment> payments = new ArrayList<OrderPayment>();
      for(OrderPayment p : list) {
        if(p.getCount() != null && p.getCount() > 0) {
          payments.add(p);
        }
      }
      order.setPayments(payments);
    }    
  }
  
  @Override
  public Order pay(Integer id) {
    Order order = orderDao.selectById(id);
    if(!STATE_NEW.equals(order.getState())) {
      String msg = String.format("Order [%d] could not be set (PAYED), its state is (%s), not (UNPAYED).", id, order.getState());
      throw new IllegalArgumentException(msg);
    }
    
    order.setPaymentDate(new Date());
    order.setState(STATE_PAYED);
    orderDao.update(order);
    logDao.insert(new ActionLog("PAYED", order));
    return order;
  }
  
  @Override
  public Order unpay(Integer id) {
    Order order = orderDao.selectById(id);
    if(!STATE_PAYED.equals(order.getState())) {
      String msg = String.format("Order [%d] could not be set (UNPAYED), its state is (%s), not (PAYED).", id, order.getState());
      throw new IllegalArgumentException(msg);
    }
    // FIXME 处理余额支付的情况
    order.setEntranceDate(new Date());
    order.setState(STATE_UNPAYED);
    orderDao.update(order);
    logDao.insert(new ActionLog("UNPAY", order));
    return order;
  }
  

  @Override
  public Order entrance(Integer id) {
    Order order = orderDao.selectById(id);
    if(!STATE_PAYED.equals(order.getState())) {
      String msg = String.format("Order [%d] could not be set (ENTRANCED), its state is (%s), not (PAYED).", id, order.getState());
      throw new IllegalArgumentException(msg);
    }
    
    order.setEntranceDate(new Date());
    order.setState(STATE_ENTRANCED);
    orderDao.update(order);
    logDao.insert(new ActionLog("ENTRANCE", order));
    return order;
  }
  
  @Override
  public Order exit(Integer id) {
    Order order = orderDao.selectById(id);
    if(!STATE_ENTRANCED.equals(order.getState())) {
      String msg = String.format("Order [%d] could not be set (EXITED), its state is (%s), not (ENTRANCED).", id, order.getState());
      throw new IllegalArgumentException(msg);
    }
    
    order.setExitDate(new Date());
    order.setState(STATE_EXITED);
    orderDao.update(order);
    logDao.insert(new ActionLog("EXIT", order));
    return order;
  }
  
  @Override
  public Order cancel(Integer id) {
    Order order = orderDao.selectById(id);
    if(STATE_ENTRANCED.equals(order.getState()) || STATE_EXITED.equals(order.getState())) {
      String msg = String.format("Order [%d] could not be set (CANCELLED), its state is (%s).", id, order.getState());
      throw new IllegalArgumentException(msg);
    }
    
    order.setCancelledDate(new Date());
    order.setState(STATE_CANCELLED);
    orderDao.update(order);
    logDao.insert(new ActionLog("CANCEL", order));
    return order;
  }

}
