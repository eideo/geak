package com.github.xsocket.geak.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.xsocket.geak.dao.CompanyDao;
import com.github.xsocket.geak.dao.UserDao;
import com.github.xsocket.geak.entity.Company;
import com.github.xsocket.geak.entity.User;
import com.github.xsocket.geak.service.UserService;

@Service
public class DefaultUserService implements UserService {

  @Autowired
  private UserDao userDao;
  
  @Autowired
  private CompanyDao companyDao;
  
  @Override
  public User loadUserById(String userId) {
    return userDao.selectById(userId);
  }
  
  @Override
  public List<User> listUser() {
    return userDao.selectAll();
  }

  @Override
  public User modifyUser(User user) {
    userDao.update(user);
    return user;
  }

  @Override
  public List<Company> listCompanyByUser(String userId) {
    return companyDao.selectByUserId(userId);
  }

}
