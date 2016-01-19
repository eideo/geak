package com.github.xsocket.geak.entity;

/**
 * 极客业务实体，表示各门店所开放的游戏主题。
 * 
 * @author MWQ
 */
public class Business extends AbstractEntity {

  private static final long serialVersionUID = 6215102026175441064L;
  
  /** 别名 */
  protected String alias;
  /** 业务说明 */
  protected String note;
  /** 业务所属公司(门店) */
  protected Company company;

  public String getAlias() {
    return alias;
  }

  public void setAlias(String alias) {
    this.alias = alias;
  }

  public String getNote() {
    return note;
  }

  public void setNote(String note) {
    this.note = note;
  }

  public Company getCompany() {
    return company;
  }

  public void setCompany(Company company) {
    this.company = company;
  }

}
