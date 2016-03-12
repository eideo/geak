package com.github.xsocket.geak.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.github.xsocket.dao.BasicDao;
import com.github.xsocket.geak.entity.Company;

/**
 * 公司(门店)信息的数据访问对象
 * 
 * @author MWQ
 */
@Repository
public interface CompanyDao extends BasicDao<Company, Integer> {
  
  /**
   * 通过用户标识获取其所在门店列表
   * @param id
   * @return
   */
  List<Company> selectByUserId(String id);
}
