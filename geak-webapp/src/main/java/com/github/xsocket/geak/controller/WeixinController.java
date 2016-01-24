package com.github.xsocket.geak.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.qq.weixin.mp.aes.AesException;
import com.qq.weixin.mp.aes.WXBizMsgCrypt;

/**
 * 微信企业号接入控制器。
 * 
 * @author MWQ
 */
@Controller
public class WeixinController {
  
  private static final Logger LOGGER = LoggerFactory.getLogger(WeixinController.class);

  private static final String CORP_ID = "wxb6ea60848a4abe21";
  
  private static final String TOKEN = "Geak2016";
  
  private static final String ENCODING_AES_KEY = "nvmywcrJK5294PT8duUT6ldwQIwHGklISX6vFUKobU5";
  
  /* TEST
  private String TOKEN = "QDG6eK";
  private String CORP_ID = "wx5823bf96d3bd56c7";
  private String ENCODING_AES_KEY = "jWmYm7qr5nMoAUwZRjGtBxmz3KA1tkAj3ykkR6q2B2C";
  */
  
  @ResponseBody
  @RequestMapping(value = "/weixin/qy/callback", method = RequestMethod.GET, produces = "text/plain")
  public String doAccess(
      @RequestParam("msg_signature") String signature, 
      @RequestParam("timestamp") String timestamp,
      @RequestParam("nonce") String nonce, 
      @RequestParam("echostr") String echostr) {
    
    LOGGER.debug("微信企业号尝试接入...");
    
    try {
      WXBizMsgCrypt wxcpt = new WXBizMsgCrypt(TOKEN, ENCODING_AES_KEY, CORP_ID);
      String echo = wxcpt.VerifyURL(signature, timestamp, nonce, echostr);
      LOGGER.debug("微信企业号接入成功！msg_singature:{}, timestamp:{}, nonce:{}, echostr:{}, echo:{}", 
          signature, timestamp, nonce, echostr, echo);
      return echo;
    } catch (AesException e) {
      LOGGER.warn("微信企业号接入失败！", e);
      throw new RuntimeException(e);
    }
    
  }
}
