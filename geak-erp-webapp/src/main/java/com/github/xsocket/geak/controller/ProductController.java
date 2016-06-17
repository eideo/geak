package com.github.xsocket.geak.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.xsocket.geak.entity.Product;
import com.github.xsocket.geak.entity.User;
import com.github.xsocket.geak.service.ProductService;
import com.github.xsocket.geak.util.GeakUtils;

@Controller
public class ProductController {
  
  @Autowired
  protected ProductService service;

  @ResponseBody
  @RequestMapping(value = "/products", method = RequestMethod.GET, produces="application/json")
  public List<Product> list() {
    User user = GeakUtils.getCurrentUser();
    return service.listProductByCompany(user.getCompany().getId());
  }
}
