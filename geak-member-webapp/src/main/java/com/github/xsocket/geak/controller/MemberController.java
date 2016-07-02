package com.github.xsocket.geak.controller;

import java.io.StringReader;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;

import com.alibaba.fastjson.JSONObject;
import com.github.xsocket.geak.entity.Member;
import com.github.xsocket.geak.entity.MemberDeposit;
import com.github.xsocket.geak.service.MemberService;
import com.github.xsocket.geak.util.GeakUtils;

@Controller
public class MemberController {
  
  private static final Logger LOGGER = LoggerFactory.getLogger(MemberController.class);  
  
  // private static final DateFormat FORMAT = new SimpleDateFormat("yyyyMMddHHmmss");
  
  @Autowired
  private MemberService service; 
  
  @ResponseBody
  @RequestMapping(value = "/member", method = RequestMethod.GET, produces="application/json")
  public Member queryMember() {
    return GeakUtils.getCurrentMember();
  }
  
  @ResponseBody
  @RequestMapping(value = "/member/deposit", method = RequestMethod.GET, produces="application/json")
  public List<MemberDeposit> listDeposit() {
    return service.listDeposit(GeakUtils.getCurrentMember());
  }
  
  @ResponseBody
  @RequestMapping(value = "/member/deposit", method = RequestMethod.POST, produces="application/json")
  public JSONObject createDeposit(@RequestParam("amount") int amount,
      HttpServletRequest request, HttpServletResponse response) {
    return service.createMemberDeposit(
        GeakUtils.getCurrentMember(), 
        amount, 
        GeakUtils.getRequestIPAddress(request));
  }
  
  @ResponseBody
  @RequestMapping(value = "/member/deposit/cancel", method = RequestMethod.POST, produces="application/json")
  public MemberDeposit cancelDeposit(@RequestParam("id") Integer id) {
    return service.cancelMemberDeposit(id);
  }
  
  @RequestMapping(value = "/wechat/pay/callback", method = RequestMethod.POST)
  public void wechatPayCallback(@RequestBody String xml, HttpServletResponse response) throws Exception {
    LOGGER.debug("Wechat Pay Callback: " + xml);
    /*
    <xml>
      <appid><![CDATA[wx2421b1c4370ec43b]]></appid>
      <bank_type><![CDATA[CFT]]></bank_type>
      <fee_type><![CDATA[CNY]]></fee_type>
      <is_subscribe><![CDATA[Y]]></is_subscribe>
      <mch_id><![CDATA[10000100]]></mch_id>
      <nonce_str><![CDATA[5d2b6c2a8db53831f7eda20af46e531c]]></nonce_str>
      <openid><![CDATA[oUpF8uMEb4qRXf22hE3X68TekukE]]></openid>
      <out_trade_no><![CDATA[1409811653]]></out_trade_no>
      <result_code><![CDATA[SUCCESS]]></result_code>
      <return_code><![CDATA[SUCCESS]]></return_code>
      <sign><![CDATA[B552ED6B279343CB493C5DD0D78AB241]]></sign>
      <sub_mch_id><![CDATA[10000100]]></sub_mch_id>
      <time_end><![CDATA[20140903131540]]></time_end>
      <total_fee>1</total_fee>
      <trade_type><![CDATA[JSAPI]]></trade_type>
      <transaction_id><![CDATA[1004400740201409030005092168]]></transaction_id>
    </xml>
    */
    
    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
    DocumentBuilder db = dbf.newDocumentBuilder();
    StringReader sr = new StringReader(xml);
    InputSource is = new InputSource(sr);
    Document document = db.parse(is);
    
    Element root = document.getDocumentElement();
    String returnCode = root.getElementsByTagName("return_code").item(0).getTextContent();
    if("SUCCESS".equals(returnCode)) {
      String resuleCode = root.getElementsByTagName("result_code").item(0).getTextContent();
      if("SUCCESS".equals(resuleCode)) {
        String tradeNo = root.getElementsByTagName("out_trade_no").item(0).getTextContent();
        service.completeMemberDeposit(tradeNo);
      }
    }
    
    // 返回结果
    response.setContentType("application/xml");
    response.getWriter().println("<xml><return_code>SUCCESS</return_code><return_msg>OK</return_msg></xml>");
  }

}
