package com.github.xsocket.geak.entity;

import java.io.Serializable;

/**
 * 极客各分部实体，表示各个门店。
 * 
 * @author MWQ
 */
public class Company implements Serializable {

  private static final long serialVersionUID = -8273921811539478395L;

  /** 实体标识 */
  protected Integer id;
  /** 实体名称 */
  protected String name;
  /** 别名 */
  protected String alias;
  /** 地址 */
  protected String address;

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

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }
}
