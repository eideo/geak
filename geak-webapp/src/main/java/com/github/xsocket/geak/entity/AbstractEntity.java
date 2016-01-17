package com.github.xsocket.geak.entity;

import java.io.Serializable;

public abstract class AbstractEntity implements Serializable {
 
  private static final long serialVersionUID = -1939586777320443480L;
  
  /** 实体标识 */
  protected Integer id;
  /** 实体名称 */
  protected String name;

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
}
