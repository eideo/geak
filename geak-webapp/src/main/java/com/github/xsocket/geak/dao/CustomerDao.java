package com.github.xsocket.geak.dao;

import java.io.Serializable;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.github.xsocket.dao.BasicDao;
import com.github.xsocket.geak.entity.Customer;

/**
 * 客户信息的数据访问对象
 * 
 * @author MWQ
 */
@Repository
public interface CustomerDao extends BasicDao<Customer, Serializable> {

  /**
   * 根据电话查询相应的客户。
   * @param telephone 电话
   * @return
   */
  List<Customer> selectByTelephone(String telephone);
}
