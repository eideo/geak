<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%><!DOCTYPE html>
<html>
  <head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>极客工厂</title>
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
  
      <div class="page" id="page_order_list">
        <header class="bar bar-nav">
          <a class="pull-right" href="#page_order_new">新建<i class="icon icon-right"></i></a>
          <h1 class='title'>订单列表</h1>
        </header>
        <div class="bar bar-header-secondary">
          <select v-model="filterType" class="button pull-left">
            <option value="">当日</option>
            <option>本周</option>
            <option>本月</option>
          </select>
          <select v-model="filterType" class="button pull-right">
            <option value="">全部订单</option>
            <option>已支付</option>
            <option>未支付</option>
          </select>
          <div class="buttons-row">
            <a href="#tab1" class="tab-link button"><</a>
            <input id="my-input" type="text" data-toggle="date" class="tab-link button" value="2016-06-05" />
            <a href="#tab1" class="tab-link button">></a>
          </div>
        </div>
        <div class="content">
          <div class="searchbar content-padded" v-bind:class="{'searchbar-active':filterType.length>0}">
            <a class="searchbar-cancel" @click="filterType=''">取消</a>
            <div class="search-input">
              <label class="icon icon-search" for="search"></label>
              <input type="search" id="search" placeholder="过滤订单..." v-model="filterType" />
            </div>
          </div><!-- /.serchbar -->
          <template v-for="order in orders | filterBy filterType in 'state'">
            <div class="card" @click="viewOrder(order)">
              <div class="card-header">
                <span>{{order.createdDate | date}}</span>
                <span>{{order.amount | currency "￥"}}</span>
              </div>
              <div class="card-content">
                <div class="list-block media-list">
                  <ul v-for="product in order.products">
                    <li class="item-content">
                      <div class="item-media"><img :src="product.image"></div>
                      <div class="item-inner">{{product.name}}</div>
                      <div class="item-inner">{{product.count}}</div>
                    </li>
                  </ul>
                </div>
              </div>
              <div class="card-footer">
                <span class="color-primary"><b>{{order.state}}</b></span>
                <span>状态操作</span>
              </div>
            </div>
          </template>
        </div> <!-- /.content -->
      </div> <!-- /#page_order_list -->

      <div class="page" id="page_order_detail">
        <header class="bar bar-nav">
          <a class="icon icon-left pull-left back"></a>
          <h1 class='title'>订单详情</h1>
        </header>
        <nav class="bar bar-tab">
          <a class="tab-item color-primary">
            <span class="badge">{{order.state}}</span>
            订单总额:{{order.amount | currency "￥"}}
          </a>
          <a class="tab-item tab-button-primary">确认下单</a>
        </nav>
        <div class="content">
          <div class="vux-divider">产品信息</div>
          <div class="list-block media-list">
            <ul class="geak-products" v-for="product in order.products">
              <li class="item-content" v-if="product.count>0">
                <div class="item-media"><img :src="product.image"></div>
                <div class="item-inner">{{product.name}}</div>
                <span class="item-after ">
                  <a class="vux-number-selector vux-number-selector-sub color-danger" 
                    v-bind:class="{'hide':product.count <= 0}" @click="product.count--;">-</a> 
                  <input type="text" class="vux-number-input" v-model="product.count" number /> 
                  <a class="vux-number-selector vux-number-selector-plus color-primary" @click="product.count++;">+</a>
                </span>
              </li>
            </ul>
          </div> <!-- /产品模块 -->
          <div class="vux-divider">
            <select v-model="order.payMode">
              <option value="0">现金支付</option>
              <option value="1">会员支付</option>
            </select>
          </div>
          <div class="list-block">
            <ul>
              <li>
                <span class="item-content">
                  <span class="item-inner">
                    <span class="item-title label">支付总额</span>
                    <span class="item-input">
                      <input type="number" v-model="order.amount"  />
                    </span>
                  </span>
                </span>
              </li>
              <template v-if="order.payMode=='0'" v-for="detail in order.payDetail">
                <li>
                  <span class="item-content">
                    <span class="item-inner">
                      <span class="item-title label">
                        <select v-model="detail.mode">
                          <option>现金</option>
                          <option>支付宝</option>
                          <option>微信</option>
                          <option>新美大</option>
                          <option>糯米</option>
                          <option>其他</option>
                        </select>
                      </span>
                      <span class="item-input">
                        <input type="number" v-model="detail.amount"  />
                      </span>
                      <span class="item-after" v-if="$index == order.payDetail.length-1">
                        <i class="weui_icon_cancel" @click="order.payDetail.pop()"></i>
                      </span>
                    </span>
                  </span>
                </li>
              </template>
              <li v-if="order.payMode=='0'">
                <span class="item-content">
                  <button class="button button-round" @click="order.payDetail.push({'mode':'现金', 'amount':0})"> 
                    添加支付方式
                  </button>
                </span>
              </li>
              <li>
                <span class="item-content">
                  <textarea placeholder="支付备注..." v-model="order.payNote"></textarea>
                </span>
              </li>
            </ul>
          </div><!-- /支付模块 -->
          <div class="vux-divider">玩家信息</div>
          <div class="list-block">
            <ul>
              <li>
                <span class="item-content">
                  <span class="item-inner">
                    <span class="item-title label">玩家姓名</span>
                    <span class="item-input">
                      <input type="text" v-model="order.member.name"  />
                    </span>
                    <span class="item-input">
                      <select v-model="order.member.sex">
                        <option value="M">先生</option>
                        <option value="F">女士</option>
                        <option value="S">同学</option>
                      </select>
                    </span>
                  </span>
                </span>
              </li>
              <li>
                <span class="item-content">
                  <span class="item-inner">
                    <span class="item-title label">联系方式</span>
                    <span class="item-input">
                      <input type="text" v-model="order.member.phone" />
                    </span>
                  </span>
                </span>
              </li>
              <li>
                <span class="item-content">
                  <span class="item-inner">
                    <span class="item-title label">玩家身份</span>
                    <span class="item-input">
                      <select v-model="order.member.type" multiple>
                        <option>小学生</option>
                        <option>中学生</option>
                        <option>大学生</option>
                        <option>青年人</option>                                                      
                        <option>中年人</option>
                        <option>老年人</option>
                      </select>
                    </span>
                  </span>
                </span>
              </li>
            </ul>
          </div><!-- /玩家模块 -->
          <div class="vux-divider">优惠信息</div>
          <div class="list-block">
            <ul>
              <template v-for="detail in order.discountDetail">
                <li>
                  <span class="item-content">
                    <span class="item-inner">
                      <span class="item-title label">
                        <select v-model="detail.mode">
                          <option>现金</option>
                          <option>支付宝</option>
                          <option>微信</option>
                          <option>新美大</option>
                          <option>糯米</option>
                          <option>其他</option>
                        </select>
                      </span>
                      <span class="item-input">
                        <input type="text" v-model="detail.note"  />
                      </span>
                      <span class="item-after" v-if="$index == order.discountDetail.length-1">
                        <i class="weui_icon_cancel" @click="order.discountDetail.pop()"></i>
                      </span>
                    </span>
                  </span>
                </li>
              </template>
              <li>
                <span class="item-content">
                  <button class="button button-round" @click="order.discountDetail.push({'mode':'现金', 'note':''})"> 
                    添加优惠信息
                  </button>
                </span>
              </li>
              <li>
                <span class="item-content">
                  <textarea placeholder="优惠备注..." v-model="order.discountNote"></textarea>
                </span>
              </li>
            </ul>
          </div><!-- /支付模块 -->
        </div>
      </div> <!-- /#page_order_detail -->

      <div class="page page-current" id="page_order_new">
        <header class="bar bar-nav">
           <a class="icon icon-left pull-left back"></a>
          <h1 class='title'>新建订单</h1>
        </header>
        <nav class="bar bar-tab" v-if="amount > 0">
          <a class="tab-item color-primary">订单总额:{{amount | currency "￥"}}</a>
          <a class="tab-item tab-button-primary" @click="createOrder">生成订单</a>
        </nav>
        <div class="content geak-products">
          <template v-for="product in products">
            <div class="vux-divider" style="clear:both;" v-if="$index==0 || product.type!=products[$index-1].type">
              {{product.type}}</div>
            <div class="card">
              <div class="card-header">{{product.name}}</div>
              <div class="list-block media-list">
                <div class="item-content">
                  <div class="item-media"><img src="/img/product/37.jpg"></div>
                  <div class="item-inner">
                    <div class="item-title">现价: <span>{{product.price | currency "￥"}}</span></div>
                    <div class="item-subtitle">原价: <span>{{product.price0 | currency "￥"}}</span></div>
                  </div>
                </div>
              </div>
              <div class="card-footer">
                <span class="color-primary">
                  <b v-bind:class="{'hide':product.count <= 0}">{{product.price*product.count | currency "￥"}}</b>
                </span>
                <span>
                  <a class="vux-number-selector vux-number-selector-sub color-danger" 
                    v-bind:class="{'hide':product.count <= 0}" @click="product.count--;">-</a> 
                  <input type="text" class="vux-number-input" v-model="product.count" number /> 
                  <a class="vux-number-selector vux-number-selector-plus color-primary" @click="product.count++;">+</a>
                </span>
              </div>
            </div>
          </template>
        </div>
      </div> <!-- /#page_new_order -->

    </div> <!-- /#app -->

    <script type="text/javascript" src="/js/vconsole.min.js"></script>
    <script type="text/javascript" src="//cdn.bootcss.com/vue/1.0.24/vue.js"></script>
    <script type="text/javascript" src="//cdn.bootcss.com/zepto/1.1.6/zepto.min.js"></script>
    <script type="text/javascript" src="//g.alicdn.com/msui/sm/0.6.2/js/sm.min.js"></script>
    <script type="text/javascript" src="/js/index.js"></script>
  </body>
</html>
