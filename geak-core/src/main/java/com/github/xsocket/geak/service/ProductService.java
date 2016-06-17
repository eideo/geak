package com.github.xsocket.geak.service;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.github.xsocket.geak.entity.Product;

public interface ProductService {

  @Transactional(readOnly = true)
  List<Product> listProductByCompany(Integer companyId);
}
