package com.github.xsocket.geak;

public class WechatException extends RuntimeException {

  private static final long serialVersionUID = 1498280029873040169L;
  
  protected int errorCode;
  
  public WechatException(int code) {
    this.errorCode = code;
  }
  
  public WechatException(int code, String message) {
    super(message);
    this.errorCode = code;
  }
  
  public WechatException(int code, Throwable ex) {
    super(ex);
    this.errorCode = code;
  }
  
  public WechatException(int code, String message, Throwable ex) {
    super(message, ex);
    this.errorCode = code;
  }

  public int getErrorCode() {
    return errorCode;
  }
  
  public String getErrorMessage() {
    return this.getMessage();
  }
}
