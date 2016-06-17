package com.github.xsocket.geak;

public class SmsException extends RuntimeException {

  private static final long serialVersionUID = 2348152542014669179L;
  
  protected int errorCode;
  
  public SmsException(int code) {
    this.errorCode = code;
  }
  
  public SmsException(int code, String message) {
    super(message);
    this.errorCode = code;
  }
  
  public SmsException(int code, Throwable ex) {
    super(ex);
    this.errorCode = code;
  }
  
  public SmsException(int code, String message, Throwable ex) {
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
