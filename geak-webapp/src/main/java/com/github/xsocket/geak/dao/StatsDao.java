package com.github.xsocket.geak.dao;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.github.xsocket.geak.entity.Stats;

/**
 * 统计查询对象。
 * 
 * @author MWQ
 */
@Repository
public interface StatsDao {
  

  /***
   * 查询起止时间段内的收入情况
   * @param start
   * @param end
   * @return
   */
  List<Stats> selectRevenue(@Param("start") Date start, @Param("end") Date end);
  
  /***
   * 查询当前所有门店的经营状态
   * @return
   */
  List<Stats> selectStatus(@Param("start") Date start, @Param("end") Date end);
}
