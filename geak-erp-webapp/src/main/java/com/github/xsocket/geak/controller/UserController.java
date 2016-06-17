package com.github.xsocket.geak.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.xsocket.geak.entity.User;
import com.github.xsocket.geak.service.UserService;

@Controller
public class UserController {
  
  @Autowired
  protected UserService service;

  @ResponseBody
  @RequestMapping(value = "/users", method = RequestMethod.GET, produces="application/json")
  public List<User> all() {
    return service.listUser();
  }
  
  @ResponseBody
  @RequestMapping(value = "/users/{id}/transfer", method = RequestMethod.POST, produces="application/json")
  public User transfer(
      @PathVariable("id") String userId, 
      @RequestParam(value="companyId", required=true) Integer companyId) {
    
    User user = service.loadUserById(userId);
    user.getCompany().setId(companyId);
    service.modifyUser(user);
    return user;
  }
}
