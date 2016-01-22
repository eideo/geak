package com.github.xsocket.geak.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.xsocket.geak.dao.PaymentModeDao;
import com.github.xsocket.geak.entity.PaymentMode;

@Controller
public class PaymentModeController {
  
  @Autowired
  protected PaymentModeDao dao;

  @ResponseBody
  @RequestMapping(value = "/payments", method = RequestMethod.GET, produces="application/json")
  public List<PaymentMode> list() {
    return dao.selectAll();
  }
}
