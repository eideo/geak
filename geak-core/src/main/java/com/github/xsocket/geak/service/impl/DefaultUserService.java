package com.github.xsocket.geak.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.xsocket.geak.dao.UserDao;
import com.github.xsocket.geak.entity.User;
import com.github.xsocket.geak.service.UserService;

@Service
public class DefaultUserService implements UserService {

  @Autowired
  private UserDao userDao;
  
  @Override
  public User loadUserById(String userId) {
    return userDao.selectById(userId);
  }

}
