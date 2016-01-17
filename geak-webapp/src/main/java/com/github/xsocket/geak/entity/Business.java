package com.github.xsocket.geak.entity;

/**
 * 极客业务实体，表示各门店所开放的游戏主题。
 * 
 * @author MWQ
 */
public class Business extends AbstractEntity {

  private static final long serialVersionUID = 6657031008117151824L;
  
  /** 别名 */
  protected String alias;
  /** 业务说明 */
  protected String note;

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

}
