package com.github.xsocket.geak.entity;

import com.github.xsocket.aa.entity.User;

public class GeakUser extends User {

  private static final long serialVersionUID = -7376059262213005660L;
  
  /** 当前极客用户所在公司 */
  protected Company company;

  public Company getCompany() {
    return company;
  }

  public void setCompany(Company company) {
    this.company = company;
  }
  
}
