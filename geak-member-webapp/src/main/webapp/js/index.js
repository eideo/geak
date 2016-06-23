Date.prototype.format = function (fmt) { //author: meizz 
  var o = {
      "M+": this.getMonth() + 1, //月份 
      "d+": this.getDate(), //日 
      "h+": this.getHours(), //小时 
      "m+": this.getMinutes(), //分 
      "s+": this.getSeconds(), //秒 
      "q+": Math.floor((this.getMonth() + 3) / 3), //季度 
      "S": this.getMilliseconds() //毫秒 
  };
  if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
  for (var k in o)
  if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
  return fmt;
}

Vue.filter("date", function (ticks, fmt) {
  return new Date(ticks).format(fmt?fmt:"yyyy-MM-dd");
});

$(function(){

  window.vm = new Vue({
    "el": "#app",
    "data": {
      newsList: GEAK_INFO,
      member: MEMBER,
      depositList: [],
      orderList: [],
      order:{}
    },
    methods: {
      prepay: function (amount) {
        $.showIndicator();
        $.post("/member/deposit?amount=" + amount, function(config){
          var _id = config.id;
          wx.chooseWXPay({
            timestamp: config.timeStamp,
            nonceStr: config.nonceStr,
            package: config.package,
            signType: config.signType,
            paySign: config.paySign,
            success: function (res) {
              console.log(res);
              $.toast("充值成功！");
            },
            cancel: function(res) {
              $.post("/member/deposit/cancel?id=" + _id);
            }
          });
          $.hideIndicator();
        });
      },

      // #page_charge_list
      stateName: function(item) {
        if(item.state == 'PAYED') {
          return '已支付';
        } else if(item.state == 'CANCELLED') {
          return '已取消';
        } else if(item.state == 'EXPIRED') {
          return '已过期';
        } else {
          return '未支付';
        } 
      },
      stateClass: function(item) {
        if(item.state == 'PAYED') {
          return 'color-success';
        } else if(item.state == 'CANCELLED') {
          return 'color-danger';
        } else if(item.state == 'EXPIRED') {
          return 'color-warning';
        } else {
          return 'color-primary';
        } 
      },

      // #page_order_list
      viewOrder: function(order) {
        vm.order = order;
        $.router.load("#page_order_detail");
      },
      orderAmount: function(items) {
        if(!items) return 0;
        var t = 0;
        items.forEach(function(item){
          t += item.count * item.price;
        });
        return t;
      },
      orderStateName: function(o) {
        switch (o.state) {
          case "NEW" : return "新订单";
          case "UNPAYED" : return "未支付";
          case "PAYED" : return "已支付";
          case "ENTRANCED" : return "已支付";
          case "EXITED" : return "已支付";
          case "CANCELLED" : return "已取消";
        }
      }
    }
  });


  $(document).on("pageInit", "#page_index", function(){
    $.showIndicator();
    $.get("/member", function(m){
      vm.member = m;
      $.hideIndicator();
    });   
  });

  $(document).on("pageInit", "#page_charge_list", function(){
    $.showIndicator();
    $.get("/member/deposit", function(list){
      vm.depositList = list;
      $.hideIndicator();
    });   
  });

  // 初始化订单明细页面
  $(document).on("pageInit", "#page_order_detail", function() {
    if(vm.order.id > 0) {
      $.showIndicator();
      $.get("/member/orders/" + vm.order.id, function(order){
        vm.order = order;
        $.hideIndicator();
      });   
    }
  });

  // 初始化产品列表页面
  $(document).on("pageInit", "#page_order_list", function(){
    $.showIndicator();
    $.get("/member/orders", function(list){
      vm.orderList = list;
      $.hideIndicator();
    });
  });

  $.init();
});

window.GEAK_INFO = [
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
];

