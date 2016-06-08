package com.github.xsocket.geak.dao;

import org.springframework.stereotype.Repository;

import com.github.xsocket.geak.entity.Member;

/**
 * 极客会员的数据访问对象
 * 
 * @author MWQ
 */
@Repository
public interface MemberDao {
  
  /**
   * 通过openid获取会员信息。
   * @return
   */
  Member selectedByOpenId(String openId);
  
  /**
   * 根据实体标识获取实体数据。
   * @param id 实体标识
   * @return 如果数据存在则返回实体数据，否则返回 null
   */
  Member selectById(Integer id);

  /**
   * 新增实体数据。
   * @param entity 实体数据
   * @return 新增数量
   */
  int insert(Member entity);
  
  /**
   * 更新实体数据。
   * @param entity 实体数据
   * @return 更新数量
   */
  int update(Member entity);
}
