package com.github.xsocket.geak.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.github.xsocket.dao.BasicDao;
import com.github.xsocket.geak.entity.PromotionPlan;

/**
 * 促销计划的数据访问对象。
 * 
 * @author MWQ
 */
@Repository
public interface PromotionPlanDao extends BasicDao<PromotionPlan, Integer>  {

  /**
   * 获取所有促销数据。
   * @return
   */
  List<PromotionPlan> selectAll();
}
