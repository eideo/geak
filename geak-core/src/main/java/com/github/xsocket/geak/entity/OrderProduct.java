package com.github.xsocket.geak.entity;

public class OrderProduct extends Product {

  private static final long serialVersionUID = 5446819384509595936L;
  
  protected Integer count;
  
  protected Integer price;

  public Integer getCount() {
    return count;
  }

  public void setCount(Integer count) {
    this.count = count;
  }
  
  public Integer getPrice() {
    return price;
  }

  public void setPrice(Integer price) {
    this.price = price;
  }
  
}
