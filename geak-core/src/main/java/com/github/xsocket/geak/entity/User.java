package com.github.xsocket.geak.entity;

import java.io.Serializable;
import java.util.List;

/**
 * 极客用户实体。
 * 
 * @author MWQ
 */
public class User implements Serializable {

  private static final long serialVersionUID = -8890147518291155037L;
  
  /** 用户标识 */
  protected String id;
  /** 用户姓名 */
  protected String name;
  /** 用户账户名 */
  protected String account;
  /** 登陆密码 */
  protected transient String password;
  /** 用户状态 */
  protected String state;
  /** 当前极客用户所在公司 */
  protected Company company;
  /** 极客用户所供职公司的列表 */
  protected List<Company> companies;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getAccount() {
    return account;
  }

  public void setAccount(String account) {
    this.account = account;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getState() {
    return state;
  }

  public void setState(String state) {
    this.state = state;
  }

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
