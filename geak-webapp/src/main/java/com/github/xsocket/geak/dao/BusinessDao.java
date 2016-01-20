package com.github.xsocket.geak.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.github.xsocket.dao.BasicDao;
import com.github.xsocket.geak.entity.Business;
import com.github.xsocket.geak.entity.Company;

/**
 * 业务信息的数据访问对象
 * 
 * @author MWQ
 */
@Repository
public interface BusinessDao extends BasicDao<Business, Integer> {

  /**
   * 根据公司(门店)标识查询其下所含的业务。
   * @param company 公司(门店)
   * @return
   */
  List<Business> selectByCompany(@Param("company") Company company);
}
