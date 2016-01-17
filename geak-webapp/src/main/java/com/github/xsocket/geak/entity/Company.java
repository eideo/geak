package com.github.xsocket.geak.entity;

/**
 * 极客各分部实体，表示各个门店。
 * 
 * @author MWQ
 */
public class Company extends AbstractEntity {

  private static final long serialVersionUID = -8273921811539478395L;
  
  /** 别名 */
  protected String alias;
  /** 地址 */
  protected String address;

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
