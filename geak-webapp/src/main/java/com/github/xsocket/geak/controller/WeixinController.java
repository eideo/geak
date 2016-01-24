package com.github.xsocket.geak.controller;

import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

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
  
  private static final String EMPTY_STRING = "";

  private static final String CORP_ID = "wxb6ea60848a4abe21";
  
  private static final String TOKEN = "Geak2016";
  
  private static final String ENCODING_AES_KEY = "nvmywcrJK5294PT8duUT6ldwQIwHGklISX6vFUKobU5";

  private static WXBizMsgCrypt WXCPT;
  
  static {
    try {
      WXCPT = new WXBizMsgCrypt(TOKEN, ENCODING_AES_KEY, CORP_ID);
    } catch (AesException e) {
      LOGGER.warn("初始化WXBizMsgCrypt失败。", e);
    }
  }
  
  @ResponseBody
  @RequestMapping(value = "/weixin/qy/callback", method = RequestMethod.POST, produces = "text/plain")
  public String doReceive(
      @RequestParam("msg_signature") String signature, 
      @RequestParam("timestamp") String timestamp,
      @RequestParam("nonce") String nonce, 
      @RequestBody String xml) {
    
    LOGGER.debug("开始处理微信企业号回调...");
    try {
      String msg = WXCPT.DecryptMsg(signature, timestamp, nonce, xml);
      LOGGER.debug("成功解密回调消息:{}", msg);
      // 解析加密消息
      DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
      DocumentBuilder db = dbf.newDocumentBuilder();
      StringReader sr = new StringReader(msg);
      InputSource is = new InputSource(sr);
      Document document = db.parse(is);
      
      Element root = document.getDocumentElement();
      NodeList typeNode = root.getElementsByTagName("MsgType");
      if(typeNode == null || typeNode.getLength() == 0) {
        LOGGER.debug("无法处理消息。");
        return EMPTY_STRING;
      } 
      
      // 处理消息
      String type = typeNode.item(0).getTextContent();
      String result = EMPTY_STRING;
      switch (type) {
        case "text":
          result = processText(root);
          break;
        case "event":
          String event = getElementTagText(root, "Event");
          if(event.equals("subscribe")) {
            result = processSubscribe(root);
          } else if(event.equals("unsubscribe")) {
            result = processUnsubscribe(root);
          }
          break;
        default:
          LOGGER.debug("无法处理类型为'{}'的消息。", type);
          break;
      }
      // 返回消息给微信服务器
      return EMPTY_STRING.equals(result) ? EMPTY_STRING : WXCPT.EncryptMsg(result, timestamp, nonce);
    } catch (Exception e) {
      LOGGER.warn("微信企业号回调处理失败！", e);
      // 回复空字符串表示处理完毕
      return EMPTY_STRING;
    }
  }
  
  public String processSubscribe(Element root) {
    String userId = getElementTagText(root, "FromUserName");
    LOGGER.debug("订阅用户的标识为:{}", userId);
    return "";
  }
  
  public String processUnsubscribe(Element root) {
    // 取消订阅
    return EMPTY_STRING;
  }
  
  // 处理消息
  public String processText(Element root) {
    // 原封不动返回消息
    String userId = getElementTagText(root, "FromUserName");
    String content = getElementTagText(root, "Content");
    String msg = "<xml><ToUserName>%s</ToUserName><FromUserName>%s</FromUserName><CreateTime>%d</CreateTime><MsgType>text</MsgType><Content><![CDATA[%s]]></Content></xml>";
    return String.format(msg, userId, CORP_ID, System.currentTimeMillis() / 1000L, content);
  }
  
  @ResponseBody
  @RequestMapping(value = "/weixin/qy/callback", method = RequestMethod.GET, produces = "text/plain")
  public String doAccess(
      @RequestParam("msg_signature") String signature, 
      @RequestParam("timestamp") String timestamp,
      @RequestParam("nonce") String nonce, 
      @RequestParam("echostr") String echostr) {
    
    LOGGER.debug("微信企业号尝试接入...");
    
    try {
      String echo = WXCPT.VerifyURL(signature, timestamp, nonce, echostr);
      LOGGER.debug("微信企业号接入成功！msg_singature:{}, timestamp:{}, nonce:{}, echostr:{}, echo:{}", 
          signature, timestamp, nonce, echostr, echo);
      return echo;
    } catch (AesException e) {
      LOGGER.warn("微信企业号接入失败！", e);
      throw new RuntimeException(e);
    }
  }
  
  private String getElementTagText(Element elem, String tag) {
    return elem.getElementsByTagName(tag).item(0).getTextContent();
  }
}
