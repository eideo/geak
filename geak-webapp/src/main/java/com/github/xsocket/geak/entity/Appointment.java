package com.github.xsocket.geak.entity;

import java.util.Date;
import java.util.List;

/**
 * 极客业务预约实体。
 * 
 * @author MWQ
 */
public class Appointment extends AbstractEntity {

  private static final long serialVersionUID = -851975256777848604L;
  
  /** 预约时间 */
  protected Date datetime;
  /** 确认时间 */
  protected Date confirmedDatetime;
  /** 取消时间 */
  protected Date cancelledDatetime;
  /** 预约客户 */
  protected Customer customer;
  /** 预约客户总人数 */
  protected int customerCount;
  /** 预约状态 */
  protected String state;
  /** 预约备注 */
  protected String note;
  /** 预约所属公司(门店) */
  protected Company company;
  /** 预约的业务列表 */
  protected List<Business> businesses;

  public Date getDatetime() {
    return datetime;
  }

  public void setDatetime(Date datetime) {
    this.datetime = datetime;
  }
  
  public Date getConfirmedDatetime() {
    return confirmedDatetime;
  }

  public void setConfirmedDatetime(Date datetime) {
    this.confirmedDatetime = datetime;
  }
  
  public Date getCancelledDatetime() {
    return cancelledDatetime;
  }

  public void setCancelledDatetime(Date datetime) {
    this.cancelledDatetime = datetime;
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

  public Company getCompany() {
    return company;
  }

  public void setCompany(Company company) {
    this.company = company;
  }

  public List<Business> getBusinesses() {
    return businesses;
  }

  public void setBusinesses(List<Business> businesses) {
    this.businesses = businesses;
  }

  public String getNote() {
    return note;
  }

  public void setNote(String note) {
    this.note = note;
  }

}
