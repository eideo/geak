<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%><!DOCTYPE html>
<html>
  <head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>${user.company.name} | ${user.name}</title>
    <meta name="viewport" content="initial-scale=1, maximum-scale=1">
    <link rel="shortcut icon" href="/favicon.ico">
    <meta name="apple-mobile-web-app-capable" content="yes">
    <meta name="apple-mobile-web-app-status-bar-style" content="black">

	  <link rel="stylesheet" href="//cdn.bootcss.com/weui/0.4.2/style/weui.css">
    <link rel="stylesheet" href="/css/vux.css">
    <link rel="stylesheet" href="//g.alicdn.com/msui/sm/0.6.2/css/sm.min.css">
    <link rel="stylesheet" href="/css/app.css">
    <script type="text/javascript">
      window.USER = ${user.toJsonString()};
    </script>
  </head>
  <body>
    <div class="page-group" id="app">
  
      <div class="page page-current" id="page_order_list">
        <header class="bar bar-nav">       
          <select v-model="user.company.id" v-on:change="changeCompany"
            class="button button-link button-nav pull-left" style="font-size:80%">
            <template v-for="co in user.companies">
            <option value="{{co.id}}">{{co.name}}</option>
            </template>
          </select>

          <a class="button button-link button-nav pull-right" href="#page_order_new">
            <small>新建订单 <span class="icon icon-right"></span></small>
          </a>
          <h1 class='title'>订单列表</h1>
        </header>
        <div class="bar bar-header-secondary">
          <select v-model="queryType" class="button pull-left" v-on:change="refreshOrderList">
            <option>当日</option>
            <option>本周</option>
            <option>本月</option>
          </select>
          <select v-model="filterType" class="button pull-right">
            <option value="">全&nbsp;&nbsp;部</option>
            <option value="UNPAYED">未支付</option>
            <option value="PAYED">已支付</option>
            <option value="ENTRANCED">已进场</option>
            <option value="EXITED">已离场</option>
          </select>
          <div class="buttons-row">
            <a class="tab-link button" style="width:2rem" @click="prevDate"> < </a>
            <input id="txtDate" type="text" class="tab-link button" v-model="queryDateStr" />
            <a class="tab-link button" style="width:2rem" @click="nextDate"> > </a>
          </div>
        </div>
        <div class="content">
        <!--
          <div class="searchbar content-padded" v-bind:class="{'searchbar-active':filterType.length>0}">
            <a class="searchbar-cancel" @click="filterType=''">取消</a>
            <div class="search-input">
              <label class="icon icon-search" for="search"></label>
              <input type="search" id="search" placeholder="过滤订单..." v-model="filterType" />
            </div>
          </div>
        -->
          
          <template v-for="order in orders | filterBy filterType in 'state'">
            <div class="card" @click="viewOrder(order)">
              <div class="card-header">
                <span>{{order.createdDate | date "MM月dd日 hh:mm"}}</span>
                <span>{{order.content}}</span>
              </div>
              <div class="card-content">
                <div class="list-block media-list">
                  <ul>
                    <li class="item-content">
                      <div class="item-inner">{{order.member.name}}<small>({{order.member.phone}})</small></div>
                      <div class="item-inner" style="text-align:right;">{{order.memberCount}} 人</div>
                    </li>
                  </ul>
                </div>
              </div>
              <div class="card-footer">
                <span>{{order.amount | currency "￥"}} <small class="badge">{{orderStateName(order)}}</small></span>
                <button class="button button-danger button-fill" 
                    v-if="order.state!='ENTRANCED'&amp;&amp;order.state!='EXITED'&amp;&amp;order.state!='CANCELLED'"
                    @click.stop="orderCancel(order)">取消订单</button>
                <button class="button button-fill button-success" v-if="order.state=='PAYED'" 
                    @click.stop="orderEntrance(order)">确认进场</button>
                <button class="button button-fill button-primary" v-if="order.state=='ENTRANCED'" 
                    @click.stop="orderExit(order)">确认离场</button>
              </div>
            </div>
          </template>
        </div> <!-- /.content -->
      </div> <!-- /#page_order_list -->

      <div class="page" id="page_order_detail">
        <header class="bar bar-nav">
          <a class="icon icon-left pull-left" href="#page_order_list"></a>
          <button class="button button-danger pull-right" @click="orderCancel(false)"
            v-if="order.id>0&amp;&amp;order.state!='ENTRANCED'&amp;&amp;order.state!='EXITED'&amp;&amp;order.state!='CANCELLED'">
                取消订单</button>
          <h1 class='title'>订单详情</h1>
        </header>
        <nav class="bar bar-tab">
          <a class="tab-item color-primary">
            <span class="badge">{{orderStateName(order)}}</span>
            实际支付:{{order.amount | currency "￥"}}
          </a>
          <template v-if="order.state=='NEW'||order.state=='UNPAYED'">
            <a class="tab-item tab-button-success" @click="orderConfirmMember(false)">玩家确认</a>
            <a class="tab-item tab-button-primary" v-if="order.paymentMode=='0'" @click="orderConfirmPay(false)">确认支付</a>
          </template>
          <template v-if="order.state=='PAYED'">
            <a class="tab-item tab-button-warning" @click="orderUnpay(false)">重新付款</a>
            <a class="tab-item tab-button-success" @click="orderEntrance(false)">确认进场</a>
          </template>
          <template v-if="order.state=='ENTRANCED'">
            <a class="tab-item tab-button-primary" @click="orderExit(false)">确认离场</a>
          </template>
        </nav>
        <div class="content">
          <div class="vux-divider">产品信息</div>
          <div class="list-block media-list">
            <ul class="geak-products">
              <li class="item-content" v-if="product.count>0" v-for="product in order.products">
                <!--<span class="item-media"><img :src="productImage(product)"></span>-->
                <span class="item-inner">{{product.alias}}</span>
                <span class="item-after">
                    <b>{{product.price * product.count | currency "￥"}}</b>
                      ={{product.price}}*
                </span>
                <span class="item-after">
                  <a class="vux-number-selector vux-number-selector-sub color-danger" 
                    v-bind:class="{'hide':product.count <= 1}" @click="product.count--;">-</a> 
                  <input type="text" class="vux-number-input" v-model="product.count" number /> 
                  <a class="vux-number-selector vux-number-selector-plus color-primary" @click="product.count++;">+</a>
                </span>
              </li>
              <li class="item-content">
                <!-- <span class="item-media"><img/></span> -->
                <span class="item-inner">总计：</span>
                <span class="item-after" style="padding-right:.5rem">
                  <b class="color-primary">{{orderAmount(order.products) | currency "￥"}}</b></span>
              </li>
              <li>
                <span class="item-content">
                  <textarea placeholder="备注说明..." v-model="order.note"></textarea>
                </span>
              </li>
            </ul>
          </div> <!-- /产品模块 -->
          <div class="vux-divider">
            <select v-model="order.paymentMode">
              <option value="0">现金支付</option>
              <option value="1">会员支付</option>
            </select>
          </div>
          <div class="list-block">
            <ul>
              <li>
                <span class="item-content">
                  <span class="item-inner">
                    <span class="item-title label">实际支付</span>
                    <span class="item-input">
                      <input type="number" placeholder="支付总额" v-model="order.amount" />
                    </span>
                  </span>
                </span>
              </li>
              <template v-if="order.paymentMode=='0'" v-for="detail in order.payments">
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
                        <input type="number" placeholder="金额" v-model="detail.count" number/>
                      </span>
                      <span class="item-after" v-if="$index == order.payments.length-1">
                        <i class="weui_icon_cancel" @click="order.payments.pop()"></i>
                      </span>
                    </span>
                  </span>
                </li>
              </template>
              <li v-if="order.paymentMode=='0'">
                <span class="item-content">
                  <button class="button button-round button-fill" @click="order.payments.push({'mode':'现金', 'count':0})"> 
                    添加支付明细
                  </button>
                </span>
              </li>
              <li>
                <span class="item-content">
                  <textarea placeholder="支付备注..." v-model="order.paymentNote"></textarea>
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
                      <input type="text" placeholder="姓名" v-model="order.member.name"  />
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
                      <input type="text" placeholder="手机" v-model="order.member.phone" />
                    </span>
                  </span>
                </span>
              </li>
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
              <li>
                <span class="item-content">
                  <span class="item-inner">
                    <span class="item-title label">玩家身份</span>
                    <span class="item-input">
                      <select v-model="order.memberTypeArray" multiple>
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
              <li>
                <span class="item-content">
                  <span class="item-inner">
                    <span class="item-title label">来源渠道</span>
                    <span class="item-input">
                      <select v-model="order.sourceArray" multiple>
                        <option>老玩家</option>
                        <option>连续场</option>
                        <option>合作商</option>
                        <option>朋友介绍</option>
                        <option>各店互推</option>
                        <option>团购</option>                                                      
                        <option>地推</option>
                        <option>搜索</option>
                        <option>其他</option>
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
              <template v-for="detail in order.promotions">
                <li>
                  <span class="item-content">
                    <span class="item-inner">
                      <span class="item-title label">
                        <select v-model="detail.mode">
                          <option>A券</option>
                          <option>B券</option>
                          <option>C券</option>
                          <option>通关券</option>
                          <option>礼品券</option>
                          <option>打车券</option>
                        </select>
                      </span>
                      <span class="item-input">
                        <input type="text" placeholder="说明..." v-model="detail.note" />
                      </span>
                      <span class="item-after" v-if="$index == order.promotions.length-1">
                        <i class="weui_icon_cancel" @click="order.promotions.pop()"></i>
                      </span>
                    </span>
                  </span>
                </li>
              </template>
              <li>
                <span class="item-content">
                  <button class="button button-round button-fill" @click="order.promotions.push({'mode':'A券', 'note':''})"> 
                    添加优惠明细
                  </button>
                </span>
              </li>
              <li>
                <span class="item-content">
                  <textarea placeholder="优惠备注..." v-model="order.promotionNote"></textarea>
                </span>
              </li>
            </ul>
          </div><!-- /优惠模块 -->
        </div>
      </div> <!-- /#page_order_detail -->

      <div class="page" id="page_order_new">
        <header class="bar bar-nav">
          <a class="button button-link button-nav pull-left back">
            <small><span class="icon icon-left"></span> 取消</small>
          </a>
          <h1 class='title'>新建订单</h1>
        </header>
        <nav class="bar bar-tab" v-if="amount > 0">
          <a class="tab-item color-primary">产品总额:{{amount | currency "￥"}}</a>
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
                  <!-- <div class="item-media"><img :src="productImage(product)"></div> -->
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

      <div class="page" id="page_order_qrcode">
        <header class="bar bar-nav">
          <a class="button button-link button-nav pull-left back">
            <small><span class="icon icon-left"></span></small>
          </a>
          <h1 class='title'>玩家扫码确认</h1>
        </header>
        <nav class="bar bar-tab">
          <a class="tab-item color-primary">
            <span class="badge">{{orderStateName(order)}}</span> 实际支付:{{order.amount | currency "￥"}}
          </a>
          <a class="tab-item tab-button-primary back">返回</a>
        </nav>
        <div class="content">
          <div class="content-block-title">订单二维码</div>
            <div class="card">
              <div valign="bottom" class="card-header color-white no-border no-padding">
                <img class='card-cover' :src="orderQrcodeLink">
              </div>
              <div class="card-footer">
                请玩家通过微信扫一扫功能，扫描订单二维码以进行订单确认。
              </div>
            </div>
        </div>
      </div> <!-- /#page_order_qrcode -->

    </div> <!-- /#app -->
<!--
    <script type="text/javascript" src="/js/vconsole.min.js"></script>
    -->
    <script type="text/javascript" src="//cdn.bootcss.com/vue/1.0.24/vue.js"></script>
    <script type="text/javascript" src="//cdn.bootcss.com/zepto/1.1.6/zepto.min.js"></script>
    <script type="text/javascript" src="//g.alicdn.com/msui/sm/0.6.2/js/sm.min.js"></script>
    <script type="text/javascript" src="/js/index.js"></script>
  </body>
</html>
