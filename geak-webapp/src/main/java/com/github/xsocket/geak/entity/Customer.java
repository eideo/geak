package com.github.xsocket.geak.entity;

import com.google.common.base.Strings;

/**
 * 极客的客户实体。
 * 
 * @author MWQ
 */
public class Customer extends AbstractEntity {

  private static final long serialVersionUID = -1415585523230502393L;
  
  /** 性别码 - 男性 */
  private static final String SEX_MALE = "M";
  /** 性别码 - 女性 */
  private final String SEX_FEMALE = "F";
  /** 性别码 - 同学 */
  private final String SEX_STUDENT = "S";

  /** 电话 */
  protected String telephone;
  /** 性别 */
  protected String sex = SEX_MALE;

  public String getTelephone() {
    return Strings.isNullOrEmpty(telephone) ? "未填写" : telephone;
  }

  public void setTelephone(String telephone) {
    this.telephone = telephone;
  }

  public String getSex() {
    return sex;
  }

  public void setSex(String sex) {
    this.sex = SEX_MALE.equalsIgnoreCase(sex) ? SEX_MALE : 
        (SEX_FEMALE.equalsIgnoreCase(sex) ? SEX_FEMALE : SEX_STUDENT );
  }

}
