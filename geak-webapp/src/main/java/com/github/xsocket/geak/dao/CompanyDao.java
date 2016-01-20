package com.github.xsocket.geak.dao;

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
  
}
