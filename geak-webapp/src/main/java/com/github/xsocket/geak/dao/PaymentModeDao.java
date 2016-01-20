package com.github.xsocket.geak.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.github.xsocket.dao.BasicDao;
import com.github.xsocket.geak.entity.PaymentMode;

/**
 * 支付渠道的数据访问对象。
 * 
 * @author MWQ
 */
@Repository
public interface PaymentModeDao extends BasicDao<PaymentMode, Integer>  {

  /**
   * 获取所有支付渠道。
   * @return
   */
  List<PaymentMode> selectAll();
}
