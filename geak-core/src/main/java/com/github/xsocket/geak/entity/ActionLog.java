package com.github.xsocket.geak.entity;

import java.io.Serializable;
import java.util.Date;

import com.alibaba.fastjson.JSON;
import com.github.xsocket.geak.util.GeakUtils;

/**
 * 操作日志。
 * 
 * @author MWQ
 */
public class ActionLog implements Serializable {

  private static final long serialVersionUID = 379334927672353468L;

  public static final String ACTION_INSERT = "INSERT";

  public static final String ACTION_UPDATE = "UPDATE";

  public static final String ACTION_DELETE = "DELETE";
  
  /** 实体标识 */
  protected Integer id;
  /** 实体名称 */
  protected String name;
  /** 操作 */
  protected String action;
  /** 操作对象的内容 */
  protected String content;
  /** 操作时间 */
  protected Date createdDate = new Date();
  /** 操作人 */
  protected User user;
  
  public ActionLog() {
    this.createdDate = new Date();
    this.user = GeakUtils.getCurrentUser();
  }
  
  public ActionLog(String action, Object content) {
    this();
    this.setAction(action);
    this.setContent(content);
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }
  
  public void setContent(Object content) {
    this.content = JSON.toJSONString(content);
    if(this.name == null) {
      this.name = content.getClass().getName();
    }
  }

  public String getAction() {
    return action;
  }

  public void setAction(String action) {
    this.action = action;
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
