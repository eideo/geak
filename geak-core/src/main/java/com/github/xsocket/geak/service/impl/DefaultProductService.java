package com.github.xsocket.geak.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.xsocket.geak.dao.ProductDao;
import com.github.xsocket.geak.entity.Product;
import com.github.xsocket.geak.service.ProductService;

@Service
public class DefaultProductService implements ProductService {
  
  // private static final Logger LOGGER = LoggerFactory.getLogger(DefaultProductService.class); 

  @Autowired
  private ProductDao dao;

  @Override
  public List<Product> listProductByCompany(Integer companyId) {
    if(companyId == null || companyId <= 0) {
      throw new IllegalArgumentException("Please pass legal company id argument.");
    } else {
      return dao.selectByCompanyId(companyId);
    }
  }

}
