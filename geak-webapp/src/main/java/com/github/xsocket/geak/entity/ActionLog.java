package com.github.xsocket.geak.entity;

import java.util.Date;

import com.alibaba.fastjson.JSON;
import com.github.xsocket.aa.entity.User;

/**
 * 操作日志。
 * 
 * @author MWQ
 */
public class ActionLog extends AbstractEntity {

  private static final long serialVersionUID = 379334927672353468L;

  public static final String ACTION_INSERT = "INSERT";

  public static final String ACTION_UPDATE = "UPDATE";

  public static final String ACTION_DELETE = "DELETE";

  /** 操作对象的内容 */
  protected String content;
  /** 操作时间 */
  protected Date createdDate = new Date();
  /** 操作人 */
  protected User user;

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }
  
  public void setContent(Object content) {
    this.content = JSON.toJSONString(content);
  }

  public String getAction() {
    return name;
  }

  public void setAction(String action) {
    this.name = action;
  }

  public Date getCreatedDate() {
    return createdDate;
  }

  public void setCreatedDate(Date createdDate) {
    this.createdDate = createdDate;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

}
