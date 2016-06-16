package com.github.xsocket.geak.service;

import org.springframework.transaction.annotation.Transactional;

import com.github.xsocket.geak.entity.User;

public interface UserService {

  @Transactional(readOnly = true)
  User loadUserById(String userId);
}
