<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%><!DOCTYPE html>
<html>
<head>
  <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge">
  <title>极客会员</title>
  <meta name="viewport" content="initial-scale=1, maximum-scale=1">
  <link rel="shortcut icon" href="/favicon.ico">
  <meta name="apple-mobile-web-app-capable" content="yes">
  <meta name="apple-mobile-web-app-status-bar-style" content="black">

  <link rel="stylesheet" href="//cdn.bootcss.com/weui/0.4.2/style/weui.css">
  <link rel="stylesheet" href="/css/vux.css">
  <link rel="stylesheet" href="//g.alicdn.com/msui/sm/0.6.2/css/sm.min.css">
  <link rel="stylesheet" href="/css/app.css">
</head>
<body>
  <div class="page-group" id="app">
    <div class="page page-current" id="page_index">
      <header class="bar bar-nav">
        <h1 class='title'>极客会员</h1>
      </header>
      <nav class="bar bar-tab">
        <a class="tab-item external active" href="#">
          <span class="icon icon-home"></span>
          <span class="tab-label">极客会员</span>
        </a>
        <a class="tab-item external" href="#">
          <span class="icon icon-me"></span>
          <span class="tab-label">极客星球</span>
          <span class="badge">2</span>
        </a>
      </nav>
      <div class="content">
        <div class="list-block media-list">
          <ul>
            <li class="item-content item-link">
              <span class="item-media">
                <img :src="member.headSmall" style="width:2.5rem;">
              </span>
              <span class="item-inner">
                <span class="item-title-row">
                  <span class="item-title">{{member.nickname}}</span>
                </span>
                <span class="item-subtitle">极客账号：{{member.account}}</span>
              </span>
            </li>
          </ul>
        </div><!-- /.list-block 头像-->
        <div class="content-block-title">会员中心</div>
        <div class="list-block">
          <ul>
            <li class="item-content item-link">
              <span class="item-media"><i class="weui_icon_waiting"></i></span>
              <span class="item-inner">
                <span class="item-title">账户余额</span>
                <span class="item-after">{{member.balance | currency '￥'}}</span>
              </span>
            </li>
            <li class="item-content item-link">
              <span class="item-media"><i class="weui_icon_waiting"></i></span>
              <span class="item-inner">
                <span class="item-title">微信充值</span>
              </span>
            </li>
            <li class="item-content item-link">
              <span class="item-media"><i class="weui_icon_waiting"></i></span>
              <span class="item-inner">
                <span class="item-title">消费记录</span>
              </span>
            </li>
          </ul>
        </div><!-- /.list-block 账户-->
        
        <div class="content-block">
	      <p><button class="button button-big button-round" onclick="wx.scanQRCode();">扫一扫</button></p>
	      
	    </div><!-- /.content-block 扫码 -->
	    <div class="content-block">
	      <div class="row">
	        <div class="col-50"><button class="button button-big button-fill button-primary" onclick="wx.scanQRCode();">扫码支付</button></div>
	        <div class="col-50"><a href="#" class="button button-big button-fill button-success">微信充值</a></div>
	      </div>
	    </div><!-- /.content-block 扫码 -->
      </div>
    </div><!-- /#page_index -->

  </div> <!-- /#app -->
  <script type="text/javascript" src="/js/vconsole.min.js"></script>
  <script type="text/javascript" src="//cdn.bootcss.com/vue/1.0.24/vue.js"></script>
  <script type="text/javascript" src="//cdn.bootcss.com/zepto/1.1.6/zepto.min.js"></script>
  <script type="text/javascript" src="//g.alicdn.com/msui/sm/0.6.2/js/sm.min.js"></script>
  <script type="text/javascript" src="//res.wx.qq.com/open/js/jweixin-1.1.0.js"></script>
  <script type="text/javascript" src="/js/index.js"></script>
  <script type="text/javascript">
  	window.MEMBER = ${member.toJsonString()};
  	wx.config({
      debug: true,
      appId: '${config.appId}',
      timestamp: '${config.timestamp}',
      nonceStr: '${config.nonceStr}',
      signature: '${config.signature}',
      jsApiList: ['scanQRCode','onMenuShareTimeline', 'onMenuShareAppMessage', 'hideAllNonBaseMenuItem', 'showMenuItems']
  	});
  </script>
</body>
</html>
