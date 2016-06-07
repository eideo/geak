package com.github.xsocket.geak.entity;

import java.io.Serializable;

public class Product implements Serializable {

  private static final long serialVersionUID = -5393915683182201215L;
  
  /** 产品标识 */
  protected Integer id;
  /** 产品名称 */
  protected String name;
  /** 产品别名 */
  protected String alias;
  /** 产品类型 */
  protected String type;
  /** 产品状态 */
  protected String state;
  /** 价格 */
  protected Integer price;
  /** 原价 */
  protected Integer price0;
  /** 产品所属公司(门店) */
  protected Company company;
  
  public Integer getId() {
    return id;
  }
  public void setId(Integer id) {
    this.id = id;
  }
  public String getName() {
    return name;
  }
  public void setName(String name) {
    this.name = name;
  }
  public String getAlias() {
    return alias;
  }
  public void setAlias(String alias) {
    this.alias = alias;
  }
  public String getType() {
    return type;
  }
  public void setType(String type) {
    this.type = type;
  }
  public Integer getPrice() {
    return price;
  }
  public void setPrice(Integer price) {
    this.price = price;
  }
  public Integer getPrice0() {
    return price0;
  }
  public void setPrice0(Integer price0) {
    this.price0 = price0;
  }
  public Company getCompany() {
    return company;
  }
  public void setCompany(Company company) {
    this.company = company;
  }
  
  
}
