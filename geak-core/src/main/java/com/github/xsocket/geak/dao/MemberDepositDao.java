package com.github.xsocket.geak.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.github.xsocket.geak.entity.Member;
import com.github.xsocket.geak.entity.MemberDeposit;

/**
 * 极客会员充值记录的数据访问对象
 * 
 * @author MWQ
 */
@Repository
public interface MemberDepositDao {
  
  /**
   * 获取会员的所有充值记录。
   * @return
   */
  List<MemberDeposit> selectedByMember(Member member);
  
  /**
   * 根据实体标识获取实体数据。
   * @param id 实体标识
   * @return 如果数据存在则返回实体数据，否则返回 null
   */
  MemberDeposit selectById(Integer id);

  /**
   * 新增实体数据。
   * @param entity 实体数据
   * @return 新增数量
   */
  int insert(MemberDeposit entity);
  
  /**
   * 更新实体数据。
   * @param entity 实体数据
   * @return 更新数量
   */
  int update(MemberDeposit entity);
}
