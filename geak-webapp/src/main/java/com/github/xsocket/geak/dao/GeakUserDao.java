package com.github.xsocket.geak.dao;

import org.springframework.stereotype.Repository;

import com.github.xsocket.dao.BasicDao;
import com.github.xsocket.geak.entity.GeakUser;

/**
 * 用户信息的数据访问对象
 * 
 * @author MWQ
 */
@Repository
public interface GeakUserDao extends BasicDao<GeakUser, String> {

}
