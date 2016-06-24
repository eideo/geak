<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%><!DOCTYPE html>
<html>
<head>
  <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge">
  <title>极客密室 - 极客星球</title>
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
    <div class="page page-current" id="page_geak_info">
      <header class="bar bar-nav">
        <h1 class='title'>极客星球</h1>
      </header>
      <nav class="bar bar-tab">
        <a class="tab-item external" href="/index.html">
          <span class="icon icon-me"></span>
          <span class="tab-label">极客会员</span>
        </a>
        <a class="tab-item active">
          <span class="icon icon-browser"></span>
          <span class="tab-label">极客星球</span>
        </a>
      </nav>
      <div class="content">
        <div class="buttons-tab">
          <a href="#tab_gf1" class="tab-link active button">极客工厂</a>
          <a href="#tab_gf2" class="tab-link button">极客密室</a>
        </div>
        <div class="tabs">
          <div id="tab_gf1" class="tab active">
            <template v-for="news in newsList | filterBy '0' in 'type'">
              <div class="card" class="external">
                <div class="card-content">
                  <div style="font-size:120%;padding:.25rem;">{{news.content}}</div>
                </div>
                <div class="card-header color-white no-border no-padding">
                  <img class='card-cover' :src="news.logo" :alt="news.content">
                </div>
                <div class="card-footer">
                  <a href="#" class="link">{{news.time}}</a>
                  <a :href="news.link" class="link external">查看全文</a>
                </div>
              </div>
            </template>
          </div>
          <div id="tab_gf2" class="tab">
            <template v-for="news in newsList | filterBy '1' in 'type'">
              <div class="card" :href="news.link" class="external">
                <div class="card-content">
                  <div class="card-content-inner">{{news.content}}</div>
                </div>
                <div class="card-header color-white no-border no-padding">
                  <img class='card-cover' :src="news.logo" :alt="news.content">
                </div>
                <div class="card-footer">
                  <a href="#" class="link">{{news.time}}</a>
                  <a :href="news.link" class="link external">查看全文</a>
                </div>
              </div>
            </template>
          </div>
        </div>
      </div><!-- /.content -->
    </div><!-- /#page_geak_info -->
  
  </div> <!-- /#app -->
  
  <!-- <script type="text/javascript" src="/js/vconsole.min.js"></script> -->
  <script type="text/javascript" src="//cdn.bootcss.com/vue/1.0.24/vue.js"></script>
  <script type="text/javascript" src="//cdn.bootcss.com/zepto/1.1.6/zepto.min.js"></script>
  <script type="text/javascript" src="//g.alicdn.com/msui/sm/0.6.2/js/sm.min.js"></script>
  <script type="text/javascript">
    $(function(){
      window.vm = new Vue({
        "el": "#app",
        "data": {
          newsList: [
            {"content":"VR体验——尽在极客工厂", "type":"0", "time":"06月15日", "logo":"/img/news/0-4.jpg", 
              "link":"http://mp.weixin.qq.com/s?__biz=MjM5ODY0NDk5Mw==&mid=517330443&idx=1&sn=1c26ecbb1b1fb703f4959368b8fda80e#rd"},
            {"content":"把透支的精力补起来——极客工厂", "type":"0", "time":"06月14日", "logo":"/img/news/0-3.jpg", 
              "link":"http://mp.weixin.qq.com/s?__biz=MjM5ODY0NDk5Mw==&mid=517330407&idx=1&sn=82f50b2e82fdec8835d7b239b1cf1a51#rd"},
            {"content":"比放假更开心的事——极客工厂", "type":"0", "time":"06月14日", "logo":"/img/news/0-2.jpg", 
              "link":"http://mp.weixin.qq.com/s?__biz=MjM5ODY0NDk5Mw==&mid=517330425&idx=1&sn=508884673bac8d0b47e08941d3ccf17a#rd"},
            {"content":"太原最好玩儿的地方——极客工厂", "type":"0", "time":"06月13日", "logo":"/img/news/0-1.jpg", 
              "link":"http://mp.weixin.qq.com/s?__biz=MjM5ODY0NDk5Mw==&mid=517330387&idx=1&sn=49c5ae736138c879779acae42dac5635#rd"},

            {"content":"V极客密室逃脱千峰店", "type":"1", "time":"2015年07月03日", "logo":"/img/news/1-6.jpg", 
              "link":"http://mp.weixin.qq.com/s?__biz=MjM5ODY0NDk5Mw==&mid=217358961&idx=1&sn=fa9389e77622b8fd7164f483e99d0523#rd"},
            {"content":"V极客密室逃脱长风店", "type":"1", "time":"2015年07月03日", "logo":"/img/news/1-5.jpg", 
              "link":"http://mp.weixin.qq.com/s?__biz=MjM5ODY0NDk5Mw==&mid=217358711&idx=1&sn=c953769c05f8b74ad43bb2eb789ed523#rd"},
            {"content":"V极客密室逃脱柳巷店", "type":"1", "time":"2015年07月03日", "logo":"/img/news/1-4.jpg", 
              "link":"http://mp.weixin.qq.com/s?__biz=MjM5ODY0NDk5Mw==&mid=412754079&idx=1&sn=8f2af33a6725c019caefd63619060fed#rd"},
            {"content":"V极客密室逃脱食品街店", "type":"1", "time":"2015年02月13日", "logo":"/img/news/1-3.jpg", 
              "link":"http://mp.weixin.qq.com/s?__biz=MjM5ODY0NDk5Mw==&mid=211625803&idx=1&sn=520cf8f01ebbf27a91209f26fe9f31c5#rd"},
            {"content":"V极客密室逃脱体育路店", "type":"1", "time":"2014年09月26日", "logo":"/img/news/1-2.jpg", 
              "link":"http://mp.weixin.qq.com/s?__biz=MjM5ODY0NDk5Mw==&mid=204500563&idx=1&sn=6acb3406fb0244220c5b52ccbe14d641#rd"},
            {"content":"V极客密室逃脱大南门店", "type":"1", "time":"2014年09月26日", "logo":"/img/news/1-1.jpg", 
              "link":"http://mp.weixin.qq.com/s?__biz=MjM5ODY0NDk5Mw==&mid=204500632&idx=1&sn=10cd2973f3310d5cdb004980573999f6#rd"}
          ]
        }
      });
    }); 
  </script>
</body>
</html>
