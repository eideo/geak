<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%><!DOCTYPE html>
<html>
<head>
  <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge">
  <title>极客密室 - 发生错误啦...</title>
  <meta name="viewport" content="initial-scale=1, maximum-scale=1">
  <meta name="apple-mobile-web-app-capable" content="yes">
  <meta name="apple-mobile-web-app-status-bar-style" content="black">
  <link rel="shortcut icon" href="/favicon.ico">

  <link rel="stylesheet" href="//cdn.bootcss.com/weui/0.4.2/style/weui.css">
  <link rel="stylesheet" href="//g.alicdn.com/msui/sm/0.6.2/css/sm.min.css">
  <link rel="stylesheet" href="/css/app.css">
</head>
<body>
  <div class="page-group" id="app">
    <div class="page page-current" id="page_error">
      <header class="bar bar-nav">
        <h1 class='title'>极客工厂</h1>
      </header>
      <nav class="bar bar-tab">
        <a class="tab-item external" href="/index.html">
          <span class="icon icon-me"></span>
          <span class="tab-label">极客会员</span>
        </a>
        <a class="tab-item external" href="/news.html">
          <span class="icon icon-browser"></span>
          <span class="tab-label">极客星球</span>
        </a>
      </nav>
      <div class="content">
        <div class="content-block-title"><span class="icon icon-star"></span> 抱歉，出错了！</div>
		  <div class="card">
		    <div class="card-content">
		      <div class="card-content-inner">${ex.message}</div>
		    </div>
		  </div>
      </div><!-- /.content -->
    </div><!-- /#page_error -->
  
  </div> <!-- /#app -->
  
  <script type="text/javascript" src="/js/all-lib.min.js"></script>
</body>
</html>
