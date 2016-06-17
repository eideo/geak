package com.github.xsocket.geak.service;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.github.xsocket.geak.entity.Company;
import com.github.xsocket.geak.entity.User;

public interface UserService {

  @Transactional(readOnly = true)
  User loadUserById(String userId);
  
  @Transactional(readOnly = true)
  List<User> listUser();
  
  @Transactional(readOnly = true)
  List<Company> listCompanyByUser(String userId);
  
  @Transactional()
  User modifyUser(User user);
}
