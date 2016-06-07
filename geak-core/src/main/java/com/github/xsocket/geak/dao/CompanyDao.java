package com.github.xsocket.geak.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.github.xsocket.geak.entity.Company;

/**
 * 公司(门店)信息的数据访问对象
 * 
 * @author MWQ
 */
@Repository
public interface CompanyDao {
  
  /**
   * 通过用户标识获取其所在门店列表
   * @param id
   * @return
   */
  List<Company> selectByUserId(String id);
  
  /**
   * 根据实体标识获取实体数据。
   * @param id 实体标识
   * @return 如果数据存在则返回实体数据，否则返回 null
   */
  Company selectById(Integer id);

  /**
   * 新增实体数据。
   * @param entity 实体数据
   * @return 新增数量
   */
  int insert(Company entity);
  
  /**
   * 更新实体数据。
   * @param entity 实体数据
   * @return 更新数量
   */
  int update(Company entity);
}
