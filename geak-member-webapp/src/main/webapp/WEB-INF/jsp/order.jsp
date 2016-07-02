<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%><!DOCTYPE html>
<html>
<head>
  <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge">
  <title>极客密室 - 会员中心</title>
  <meta name="viewport" content="initial-scale=1, maximum-scale=1">
  <meta name="apple-mobile-web-app-capable" content="yes">
  <meta name="apple-mobile-web-app-status-bar-style" content="black">
  <link rel="shortcut icon" href="/favicon.ico">

  <link rel="stylesheet" href="//cdn.bootcss.com/weui/0.4.2/style/weui.css">
  <link rel="stylesheet" href="/css/vux.css">
  <link rel="stylesheet" href="//g.alicdn.com/msui/sm/0.6.2/css/sm.min.css">
  <link rel="stylesheet" href="/css/app.css">
</head>
<body>
  <div class="page-group" id="app">
  
    <div class="page page-current" id="page_order_detail">
      <header class="bar bar-nav">
        <a class="icon icon-left pull-left" href="#page_order_list"></a>
        <button class="button button-danger pull-right" @click="orderCancel(order)"
          v-if="order.state=='NEW'||order.state=='UNPAYED'">
              取消订单</button>
        <h1 class='title'>订单详情</h1>
      </header>
      <nav class="bar bar-tab" v-if="order.state=='NEW'||order.state=='UNPAYED'">
        <a class="tab-item color-primary">
          <span class="badge">{{orderStateName(order)}}</span>
          订单总额:{{order.amount | currency "￥"}}
        </a>
        <a class="tab-item tab-button-primary" @click="orderDepositPay(order)">
            余额支付<small v-if="orderHasDiscount(order)" class="color-danger">(立减10元)</small></a>
      </nav>
      <nav class="bar bar-tab" v-if="order.state!='NEW'&amp;&amp;order.state!='UNPAYED'">
        <a class="tab-item" href="#page_index">
          <span class="icon icon-me"></span>
          <span class="tab-label">极客会员</span>
        </a>
        <a class="tab-item tab-button-primary" href="#page_order_list">消费记录</a>
      </nav>
      <div class="content">
        <small :class="orderStateClass(order)">{{order.createdDate | date "yyyy-MM-dd hh:mm"}}</small>
        <div class="list-block">
          <ul class="geak-products">
            <li class="item-content" v-if="product.count>0" v-for="product in order.products">
              <span class="item-inner">{{product.alias}}</span>
              <span class="item-after" style="padding-right:.5rem">
                <b>{{product.price * product.count | currency "￥"}}</b> 
                  = {{product.price}} * {{product.count}}
              </span>
            </li>
            <li class="item-content">
              <span class="item-inner">总计：</span>
              <span class="item-after" style="padding-right:.5rem">
                <b class="color-primary">{{orderAmount(order.products) | currency "￥"}}</b></span>
            </li>
            <li v-if="order.note">
              <span class="item-content">{{order.note}}</span>
            </li>
          </ul>
        </div> <!-- /产品模块 -->
        <p></p>
        <div class="list-block">
          <ul>
            <li>
              <span class="item-content">
                <span class="item-inner">
                  <span class="item-title label">实际支付</span>
                  <span class="item-after">{{order.amount | currency "￥"}}</span>
                </span>
              </span>
            </li>
            <template v-if="order.paymentMode=='1'&amp;&amp;orderStateName(order)=='余额支付'">
              <li>
                <span class="item-content color-primary">会员余额支付优惠10元</span>
              </li>
            </template>
            <li v-if="order.paymentNote">
              <span class="item-content">{{order.paymentNote}}</span>
            </li>
          </ul>
        </div><!-- /支付模块 -->
        <p></p>
        <div class="list-block">
          <ul>            
            <li v-if="order.paymentDate">
              <span class="item-content">
                <span class="item-inner">
                  <span class="item-title label">付款时间</span>
                  <span class="item-input">
                    {{order.paymentDate | date "yyyy-MM-dd hh:mm"}}
                  </span>
                </span>
              </span>
            </li>
            <li v-if="order.entranceDate">
              <span class="item-content">
                <span class="item-inner">
                  <span class="item-title label">进场时间</span>
                  <span class="item-input">
                    {{order.entranceDate | date "yyyy-MM-dd hh:mm"}}
                  </span>
                </span>
              </span>
            </li>
            <li v-if="order.exitDate">
              <span class="item-content">
                <span class="item-inner">
                  <span class="item-title label">离场时间</span>
                  <span class="item-input">
                    {{order.exitDate | date "yyyy-MM-dd hh:mm"}}
                  </span>
                </span>
              </span>
            </li>
          </ul>
        </div><!-- /玩家模块 -->
        <p></p>
        <div class="list-block">
          <ul>
            <template v-for="detail in order.promotions">
              <li>
                <span class="item-content">
                  <span class="item-inner">
                    <span class="item-title label">{{detail.mode}}</span>
                    <span class="item-after">{{detail.note}}</span>
                  </span>
                </span>
              </li>
            </template>
            <li v-if="order.promotionNote">
              <span class="item-content">{{order.promotionNote}}</span>
            </li>
          </ul>
        </div><!-- /优惠模块 -->
      </div>
    </div> <!-- /#page_order_detail -->

    <div class="page" id="page_index">
      <header class="bar bar-nav">
        <h1 class='title'>极客会员</h1>
      </header>
      <nav class="bar bar-tab">
        <a class="tab-item active">
          <span class="icon icon-me"></span>
          <span class="tab-label">极客会员</span>
        </a>
        <a class="tab-item external" href="/news.html">
          <span class="icon icon-browser"></span>
          <span class="tab-label">极客星球</span>
        </a>
      </nav>
      <div class="content">
        <div class="list-block media-list">
          <ul>
            <li class="item-content"><!-- item-link -->
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
            <li class="item-content item-link" onclick="$.router.load('#page_charge_list')">
              <span class="item-media"><i class="icon icon-card"></i></span>
              <span class="item-inner">
                <span class="item-title">账户余额</span>
                <span class="item-after">{{member.balance | currency '￥'}}</span>
              </span>
            </li>
            <li class="item-content item-link" onclick="$.router.load('#page_order_list')">
              <span class="item-media"><i class="icon icon-menu"></i></span>
              <span class="item-inner">
                <span class="item-title">消费记录</span>
              </span>
            </li>
          </ul>
        </div><!-- /.list-block 账户-->
      <div class="content-block">
        <div class="row">
          <button class="col-50 button button-big button-fill button-primary" onclick="wx.scanQRCode();">
            <i class="icon icon-code"></i> 确认订单</button>
          <a class="col-50 button button-big button-fill button-success" href="#page_charge">
            <i class="icon icon-gift"></i> 账户充值</a>
        </div>
      </div><!-- /.content-block 扫码 -->
      </div>
    </div><!-- /#page_index -->
    
    <div class="page" id="page_charge">
      <header class="bar bar-nav">
        <a class="icon icon-left pull-left back"></a>
        <h1 class='title'>账户充值</h1>
      </header>
      <nav class="bar bar-footer bar-tab">
        <a class="tab-item" href="#page_index">
          <span class="icon icon-me"></span>
          <span class="tab-label">极客会员</span>
        </a>
        <a class="tab-item tab-button-primary" href="#page_charge_list">充值记录</a>
      </nav>
      <div class="content">
        <div class="content-block">
          <p><a href="#" class="button button-big button-fill button-success" @click="prepay(100)">充值【<b style="font-size:100%">￥100.00</b>】元</a></p>
          <p><a href="#" class="button button-big button-fill button-success" @click="prepay(200)">充值【<b style="font-size:100%">￥200.00</b>】元</a></p>
          <p><a href="#" class="button button-big button-fill button-warning" @click="prepay(300)">充值【<b style="font-size:120%">￥300.00</b>】元</a></p>
          <p><a href="#" class="button button-big button-fill button-danger" @click="prepay(400)">充值【<b style="font-size:150%">￥400.00</b>】元</a></p>
          <p><a href="#" class="button button-big button-fill" @click="prepay(500)">充值【<b style="font-size:180%">￥500.00</b>】元</a></p>
        </div>
      </div>
    </div><!-- /#page_charge -->

    <div class="page" id="page_charge_list">
      <header class="bar bar-nav">
        <a class="icon icon-left pull-left back"></a>
        <h1 class='title'>充值记录</h1>
      </header>
      <nav class="bar bar-footer bar-tab">
        <a class="tab-item back">
          <span class="icon icon-me"></span>
          <span class="tab-label">极客会员</span>
        </a>
        <a class="tab-item tab-button-success" href="#page_charge">账户充值</a>
      </nav>
      <div class="content">
        <div class="list-block media-list" v-if="depositList.length==0">
          <ul>              
            <li>
              <div class="item-content">
              <!--
                <div class="item-media"><img src="/img/logo.jpg" style='width: 4rem;'></div>
              -->
                <div class="item-inner">
                  <div class="item-title">您还没有任何充值记录哦~</div>
                  <div class="item-text" style="height:2.5rem">
                    <a href="#page_charge" class="button button-big button-success">
                      <i class="icon icon-gift"></i> 马上去充值</a>
                  </div>
                </div>
              </div>
            </li>
          </ul>
        </div>
        <template v-for="item in depositList">
          <div class="card">
            <div class="card-header">
              <span>微信充值</span>
              <span>{{item.beginDate | date "yyyy-MM-dd hh:ss"}}</span>
            </div>
            <div class="card-footer">
              <span :class="stateClass(item)">{{stateName(item);}}</span>
              <b :class="stateClass(item)">{{item.amount | currency "￥"}}</b>
            </div>
          </div>
        </template>
      </div>
    </div><!-- /#page_charge_list -->

    <div class="page" id="page_order_list">
      <header class="bar bar-nav">  
        <a class="icon icon-left pull-left" href="#page_index"></a>
        <h1 class='title'>消费记录</h1>
      </header>
      <nav class="bar bar-footer bar-tab">
        <a class="tab-item back">
          <span class="icon icon-me"></span>
          <span class="tab-label">极客会员</span>
        </a>
        <a class="tab-item tab-button-success" href="#page_charge">账户充值</a>
      </nav>
      <div class="content">
        <template v-for="order in orderList">
          <div class="card" @click="viewOrder(order)">
            <div class="card-header">
              <span>{{order.createdDate | date "MM月dd日 hh:mm"}}</span>
              <span>{{order.content}}</span>
            </div>
            <div v-if="orderHasDiscount(order)" class="card-content" style="text-align:right;">
              <p class="color-primary">会员余额支付立减10元</p>
            </div>
            <div class="card-footer">
              <span :class="orderStateClass(order)">{{order.amount | currency "￥"}} </span>
              <span :class="orderStateClass(order)">{{orderStateName(order)}}</span>
              <button class="button button-primary button-fill" 
                  v-if="order.state=='UNPAYED'||order.state=='NEW'"
                  @click.stop="orderDepositPay(order)">余额支付</button>
            </div>
          </div>
        </template>
      </div> <!-- /.content -->
    </div> <!-- /#page_order_list -->
    
    <div class="page" id="page_member_info">
      <header class="bar bar-nav">
      	<a class="icon icon-left pull-left back"></a>
        <h1 class="title">个人信息</h1>
      </header>
      <div class="content">
        <div class="content-block-title color-danger">{{message}}&nbsp;</div>
        <div class="list-block">
          <ul>  
            <li>
              <div class="item-content">
                <div class="item-media"><i class="icon icon-me"></i></div>
                <div class="item-inner">
                  <div class="item-input">
                    <input type="text" placeholder="会员昵称" v-model="member.nickname" />
                  </div>
                  <span class="item-input">
                      <select v-model="member.sex">
                        <option value="M">先生</option>
                        <option value="F">女士</option>
                        <option value="S">同学</option>
                      </select>
                    </span>
                </div>
              </div>
            </li>            
            <li>
              <div class="item-content">
                <div class="item-media"><i class="icon icon-phone"></i></div>
                <div class="item-inner">
                  <div class="item-input">
                    <input type="text" id="txt_phone" placeholder="会员手机" v-model="member.phone"/>
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
                    <button class="button" id="btn_captcha" data-tag="60" onclick="fetchCaptcha()">获取验证码</button>
                  </div>
                </div>
              </div>
            </li>
          </ul>
        </div>
        <div class="content-block">
          <p><a class="button button-round button-fill button-big"
            @click="saveMember()">提 交 信 息</a></p>
        </div>
      </div><!-- /.content -->
    </div><!-- /#page_login -->
  
  </div> <!-- /#app -->
  
  <!-- <script type="text/javascript" src="/js/vconsole.min.js"></script> -->
  <!--  <script type="text/javascript" src="//cdn.bootcss.com/vue/1.0.24/vue.min.js"></script> 
  <script type="text/javascript" src="//cdn.bootcss.com/zepto/1.1.6/zepto.min.js"></script>
  <script type="text/javascript" src="//g.alicdn.com/msui/sm/0.6.2/js/sm.min.js"></script>
  <script type="text/javascript" src="//res.wx.qq.com/open/js/jweixin-1.1.0.js"></script>-->
  <script type="text/javascript" src="/js/all-lib.min.js"></script>
  <script type="text/javascript" src="/js/index.js"></script>
  <script type="text/javascript">
  	window.INDEX = false;
    window.MEMBER = ${member.toJsonString()};
    window.ORDER = {"id":${orderId}};
    wx.config({
      debug: false,
      appId: '${config.appId}',
      timestamp: '${config.timestamp}',
      nonceStr: '${config.nonceStr}',
      signature: '${config.signature}',
      jsApiList: ['scanQRCode', 'chooseWXPay']
    });
  </script>
</body>
</html>
