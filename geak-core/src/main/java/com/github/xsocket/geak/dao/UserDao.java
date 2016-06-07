package com.github.xsocket.geak.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.github.xsocket.geak.entity.User;

/**
 * 极客用户的数据访问对象
 * 
 * @author MWQ
 */
@Repository
public interface UserDao {
  
  /**
   * 获取所有用户的信息。
   * @return
   */
  List<User> selectAll();
  
  /**
   * 根据实体标识获取实体数据。
   * @param id 实体标识
   * @return 如果数据存在则返回实体数据，否则返回 null
   */
  User selectById(String id);

  /**
   * 新增实体数据。
   * @param entity 实体数据
   * @return 新增数量
   */
  int insert(User entity);
  
  /**
   * 更新实体数据。
   * @param entity 实体数据
   * @return 更新数量
   */
  int update(User entity);
}
