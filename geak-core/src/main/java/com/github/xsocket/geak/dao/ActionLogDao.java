package com.github.xsocket.geak.dao;

import org.springframework.stereotype.Repository;

import com.github.xsocket.geak.entity.ActionLog;

/**
 * 操作日志数据访问对象
 * 
 * @author MWQ
 */
@Repository
public interface ActionLogDao {

  /**
   * 新增操作日志。
   * @return
   */
  int insert(ActionLog log);
}
