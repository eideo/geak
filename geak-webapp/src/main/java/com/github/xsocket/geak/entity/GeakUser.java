package com.github.xsocket.geak.entity;

import java.util.List;

import com.github.xsocket.aa.entity.User;

public class GeakUser extends User {

  private static final long serialVersionUID = -7376059262213005660L;
  
  /** 当前极客用户所在公司 */
  protected Company company;
  /** 极客用户所供职公司的列表 */
  protected List<Company> companies;

  public Company getCompany() {
    return company;
  }

  public void setCompany(Company company) {
    this.company = company;
  }

  public List<Company> getCompanies() {
    return companies;
  }

  public void setCompanies(List<Company> companies) {
    this.companies = companies;
  }
  
}
