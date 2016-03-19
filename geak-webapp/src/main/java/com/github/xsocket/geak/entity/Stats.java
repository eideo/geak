package com.github.xsocket.geak.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * 业务数据统计对象。
 * 
 * @author MWQ
 *
 */
public class Stats implements Serializable {

  private static final long serialVersionUID = 7136036809707690032L;
  
  /** 查询起始时间 */
  private Date start;
  /** 查询结束时间 */
  private Date end;
  /** 门店编号 */
  private Integer companyId;
  /** 门店名称 */
  private String companyName;
  /** 总收入 */
  private Integer totalIncome;
  /** 总接待数 */
  private Integer totalCount;
  /** 总人数 */
  private Integer totalCustomer;
  /** 状态 */
  private String state;

  public Date getStart() {
    return start;
  }

  public void setStart(Date start) {
    this.start = start;
  }

  public Date getEnd() {
    return end;
  }

  public void setEnd(Date end) {
    this.end = end;
  }

  public Integer getCompanyId() {
    return companyId;
  }

  public void setCompanyId(Integer companyId) {
    this.companyId = companyId;
  }

  public String getCompanyName() {
    return companyName;
  }

  public void setCompanyName(String companyName) {
    this.companyName = companyName;
  }

  public Integer getTotalIncome() {
    return totalIncome;
  }

  public void setTotalIncome(Integer totalIncome) {
    this.totalIncome = totalIncome;
  }

  public Integer getTotalCount() {
    return totalCount;
  }

  public void setTotalCount(Integer totalCount) {
    this.totalCount = totalCount;
  }

  public Integer getTotalCustomer() {
    return totalCustomer;
  }

  public void setTotalCustomer(Integer totalCustomer) {
    this.totalCustomer = totalCustomer;
  }

  public String getState() {
    return state;
  }

  public void setState(String state) {
    this.state = state;
  }

}
