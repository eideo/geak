package com.github.xsocket.geak.dao;

import org.springframework.stereotype.Repository;

import com.github.xsocket.dao.BasicDao;
import com.github.xsocket.dao.BasicRelationDao;
import com.github.xsocket.geak.entity.Appointment;
import com.github.xsocket.geak.entity.Business;

/**
 * 预约业务的数据访问对象。
 * 
 * @author MWQ
 */
@Repository
public interface AppointmentDao extends BasicDao<Appointment, Integer>, BasicRelationDao<Appointment, Business> {

}
