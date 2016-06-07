package com.github.xsocket.geak.dao;

import java.util.List;

import com.github.xsocket.geak.entity.Product;

public interface ProductDao {
  
  /**
   * 通过公司(门店)标识获取其在售商品列表
   * @param id
   * @return
   */
  List<Product> selectByCompanyId(Integer id);
  
  /**
   * 根据实体标识获取实体数据。
   * @param id 实体标识
   * @return 如果数据存在则返回实体数据，否则返回 null
   */
  Product selectById(Integer id);

  /**
   * 新增实体数据。
   * @param entity 实体数据
   * @return 新增数量
   */
  int insert(Product entity);
  
  /**
   * 更新实体数据。
   * @param entity 实体数据
   * @return 更新数量
   */
  int update(Product entity);
}
