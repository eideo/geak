<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%><!DOCTYPE html>
<html>
<head>
  <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge">
  <title>极客密室 - 员工登陆</title>
  <meta name="viewport" content="initial-scale=1, maximum-scale=1">
  <meta name="apple-mobile-web-app-capable" content="yes">
  <meta name="apple-mobile-web-app-status-bar-style" content="black">
  <link rel="shortcut icon" href="/favicon.ico">
  
  <link rel="stylesheet" href="//g.alicdn.com/msui/sm/0.6.2/css/sm.min.css">
</head>
<body>
  <div class="page-group" id="app">
    <div class="page" id="page_login">
      <header class="bar bar-nav">
        <h1 class="title">极客密室 - 员工登陆</h1>
      </header>
      <form class="content" method="post">
        <div class="content-block-title color-danger">${error}&nbsp;</div>
        <div class="list-block">
          <ul>  
            <li>
              <div class="item-content">
                <div class="item-media"><i class="icon icon-me"></i></div>
                <div class="item-inner">
                  <div class="item-input">
                    <input type="text" name="account" placeholder="工号,如:2016012301" id="txt_id" value="${account}" />
                  </div>
                </div>
              </div>
            </li>            
            <li>
              <div class="item-content">
                <div class="item-media"><i class="icon icon-phone"></i></div>
                <div class="item-inner">
                  <div class="item-input">
                    <input type="text" name="phone" placeholder="手机号" id="txt_phone" value="${phone}" />
                  </div>
                </div>
              </div>
            </li>
            <li>
              <div class="item-content">
                <div class="item-media"><i class="icon icon-code"></i></div>
                <div class="item-inner">
                  <div class="item-input">
                    <input type="text" name="captcha" placeholder="验证码" id="txt_captcha">
                  </div>
                  <div class="item-title label">
                    <button class="button" id="btn_captcha" data-tag="60" onclick="return fetchCaptcha()">获取验证码</button>
                  </div>
                </div>
              </div>
            </li>
          </ul>
        </div>
        <div class="content-block">
          <p><input type="submit" class="button button-round button-fill button-big" value="登 录 系 统"
            onclick="return doSubmit()"/></p>
        </div>
      </form><!-- /.content -->
    </div><!-- /#page_login -->
  </div> <!-- /#app -->
  <script type="text/javascript" src="//cdn.bootcss.com/zepto/1.1.6/zepto.min.js"></script>
  <script type="text/javascript" src="//g.alicdn.com/msui/sm/0.6.2/js/sm.min.js"></script>
  <script>
    function fetchCaptcha() {
      var _phome = $("#txt_phone").val();
      if(_phome.length != 11) {
        $.toast("请输入11位手机号码");
      } else {
        $.showIndicator();
        $.get("/login/captcha?phone="+_phome, function(rt){          
          $.hideIndicator();
          if(rt.error) {
            $.toast(rt.error); 
          } else {
            $.toast("验证码已发送");            
            doCountDown();
          }
        });
      }
      return false;
    }    

    function doSubmit() {
      var _account = $("#txt_id").val();
      var _phone = $("#txt_phone").val();
      var _captcha = $("#txt_captcha").val();
      if(_account.length < 8) {
        $.toast("请输入正确的员工工号");
        return false;
      } else if(_phone.length != 11) {
        $.toast("请输入11位手机号码");
        return false;
      } else if(_captcha.length != 4) {
        $.toast("请输入4位验证码");
        return false;
      }
      return true;
    }

    var COUNT_DOWN = 60; 
    function doCountDown() { 
      var $captcha = $("#btn_captcha");
      if(COUNT_DOWN == 0) { 
        $captcha.removeAttr("disabled").removeClass("disabled").html("获取验证码"); 
        COUNT_DOWN = 60; 
      } else { 
        $captcha.addClass("disabled").attr("disabled", "disabled");
        $captcha.html("重新获取(" + COUNT_DOWN + ")"); 
        COUNT_DOWN--;
        setTimeout(function() { 
          doCountDown() 
        }, 1000);
      } 
    }
  </script>
</body>
</html>
