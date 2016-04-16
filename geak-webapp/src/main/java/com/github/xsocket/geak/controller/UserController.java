package com.github.xsocket.geak.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.xsocket.geak.dao.GeakUserDao;
import com.github.xsocket.geak.entity.GeakUser;

@Controller
public class UserController {
  
  @Autowired
  protected GeakUserDao service;

  @ResponseBody
  @RequestMapping(value = "/users", method = RequestMethod.GET, produces="application/json")
  public List<GeakUser> all() {
    return service.selectAll();
  }
  
  @ResponseBody
  @RequestMapping(value = "/users/{id}/transfer", method = RequestMethod.POST, produces="application/json")
  public GeakUser transfer(
      @PathVariable("id") String userId, 
      @RequestParam(value="companyId", required=true) Integer companyId) {
    
    GeakUser user = service.selectById(userId);
    if(user != null) {
      user.getCompany().setId(companyId);
      service.update(user);
    }
    return user;
  }
}
