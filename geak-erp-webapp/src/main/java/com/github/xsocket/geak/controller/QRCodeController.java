package com.github.xsocket.geak.controller;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.github.xsocket.geak.util.QRCodeUtils;

@Controller
public class QRCodeController {
  
  @RequestMapping(value = "/qrcode", method = RequestMethod.GET)
  public void getImage(
      @RequestParam("content") String content, 
      @RequestParam(value="size", required=false) Integer size,
      HttpServletResponse response) throws Exception {

    if(size == null || size <= 0) {
      size = 300;
    }
    
    ServletOutputStream out = null;
    try {
      response.setContentType("image/png");
      out = response.getOutputStream();
      QRCodeUtils.writeToStream(content, size, "png", out);
      out.flush();
    } finally {
      IOUtils.closeQuietly(out);
    }
  }
}

