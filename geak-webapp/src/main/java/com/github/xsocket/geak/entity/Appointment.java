package com.github.xsocket.geak.entity;

import java.util.Date;
import java.util.List;

/**
 * 极客业务预约实体。
 * 
 * @author MWQ
 */
public class Appointment extends AbstractEntity {

  private static final long serialVersionUID = -8382013133937974108L;
  
  /** 预约时间 */
  protected Date datetime;
  /** 预约客户 */
  protected Customer customer;
  /** 预约客户总人数 */
  protected int customerCount;
  /** 预约状态 */
  protected String state;
  /** 预约的业务列表 */
  protected List<Business> businesses;

  public Date getDatetime() {
    return datetime;
  }

  public void setDatetime(Date datetime) {
    this.datetime = datetime;
  }

  public Customer getCustomer() {
    return customer;
  }

  public void setCustomer(Customer customer) {
    this.customer = customer;
  }

  public int getCustomerCount() {
    return customerCount;
  }

  public void setCustomerCount(int count) {
    this.customerCount = count;
  }

  public String getState() {
    return state;
  }

  public void setState(String state) {
    this.state = state;
  }

  public List<Business> getBusinesses() {
    return businesses;
  }

  public void setBusinesses(List<Business> businesses) {
    this.businesses = businesses;
  }

}
