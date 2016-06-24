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
      user: USER,
      filterType: "",
      queryDateStr: new Date().format("yyyy-MM-dd"),
     // queryDate: new Date().getTime(),
      queryType: "当日",
      products: [],
      orders: [],
      order: {
        member:{
          name:"",
          phone:"",
          sex:"M"
        }
      }
    },
    computed: {
      queryDate: function() {
        return new Date(this.queryDateStr).getTime();
      },
      // 订单总价
      amount:function () {
        var t = 0;
        this.products.forEach(function(item){
          t += item.count * item.price;
        });
        return t;
      },
      orderQrcodeLink: function() {
        if(this.order.id > 0) {
          return "/qrcode?content=http://geak-member.weikuai01.com/member/orders/link/"+this.order.id+"#page_order_detail";
        }
        return null;
      }
    },
    methods: {
      changeCompany: function() {
        location.href='/index.html?company=' + this.user.company.id;
      },
      nextDate: function() {
        var d = new Date(this.queryDateStr);
        d.setDate(d.getDate() + 1);
        this.queryDateStr = d.format("yyyy-MM-dd");
        this.refreshOrderList();
      },
      prevDate: function() {
        var d = new Date(this.queryDateStr);
        d.setDate(d.getDate() - 1);
        this.queryDateStr = d.format("yyyy-MM-dd");
        this.refreshOrderList();
      },
      orderStateName: function(o) {
        switch (o.state) {
          case "NEW" : return "新订单";
          case "UNPAYED" : return "未支付";
          case "PAYED" : return "已支付";
          case "ENTRANCED" : return "已进场";
          case "EXITED" : return "已离场";
          case "CANCELLED" : return "已取消";
        }
      },
      orderAmount: function(items) {
        if(!items) return 0;
        var t = 0;
        items.forEach(function(item){
          t += item.count * item.price;
        });
        return t;
      },
      productImage: function(item) {
        return "/img/product/37.jpg#" + item.id
      },

      refreshOrderList: function() {
        var start = this.queryDate;
        var end = this.queryDate;
        if(this.queryType == "本周") {
          start = week1(start);
          end = week7(end);
        } else if(this.queryType == "本月") {
          start = month1(start);
          end = month30(end);
        }
        doLoadOrders(start, end);
      },
      // 新建订单
      createOrder: function() {
        vm.order = {
          member:{
            name:"",
            phone:"",
            sex:"M"
          },
          state: "NEW",
          note: "",
          amount: this.orderAmount(this.products),
          paymentNote:"",
          paymentMode: "0",
          payments:[],
          paymentNote:"",
          promotions:[],
          promotionNote:"",
          products: this.products,
        };
        $.router.load("#page_order_detail");
      },
      // 确认支付
      orderConfirmPay: function(order) {
        var _order = order ? order : vm.order;
        if(this.isValidOrder(_order)) {
          _order.paymentDate = new Date().getTime();
          doSaveOrder(_order);
        }
      },
      // 玩家确认
      orderConfirmMember: function(order) {
        var _order = order ? order : vm.order;
        if(isNaN(_order.amount) || _order.amount < 0){
          $.toast("请输入正确的支付总额");
          return false;
        }
        doSaveOrder(_order, function(){
          $.router.load('#page_order_qrcode');
        });
        
      },
      // 控制订单状态
      orderUnpay: function(order) {
        var _order = order ? order : vm.order;
        doShiftOrderState(_order, "确定重新支付该订单吗？", "unpay", order);
      },
      orderEntrance: function(order) {
        var _order = order ? order : vm.order;
        doShiftOrderState(_order, "确认玩家已进场？", "entrance", order);
      },
      orderExit: function(order) {
        var _order = order ? order : vm.order;
        doShiftOrderState(_order, "确认玩家已离场？", "exit", order);
      },
      orderCancel: function(order) {
        var _order = order ? order : vm.order;
        doShiftOrderState(_order, "确定取消订单吗？", "cancel", order);
      },
      viewOrder: function(order) {
        vm.order = order;
        $.router.load("#page_order_detail");
      },
      // 验证订单
      isValidOrder: function(order) {
        var _order = order ? order : vm.order;

        var hasMainProduct = false;
        order.products.forEach(function(item){
          if(item.type.indexOf("门票") >= 0 && item.count >0) {
            hasMainProduct = true;
          }
        });

        if(hasMainProduct && isNullOrEmpty(_order.member.name)) {
          $.toast("请输入玩家姓名");
          return false;
        } else if(hasMainProduct && isNullOrEmpty(_order.member.phone)) {
          $.toast("请输入玩家联系方式");
          return false;
        } else if(isNaN(_order.amount) || _order.amount < 0){
          $.toast("请输入正确的支付总额");
          return false;
        }
        return true;
      }
    }
  });

  function doShiftOrderState(order, confirmContent, shiftState, refresh) {
    $.confirm(confirmContent, function() {
      $.showIndicator();
      $.post("/orders/" + order.id + "/" + shiftState, function(data){
        vm.order = data;
        $.hideIndicator();
        $.toast("操作成功");
        if(refresh) {
          vm.refreshOrderList();
        }
      });
    });
  }

  function doSaveOrder(order, callback) {
    $.showIndicator();
    $.ajax({
      type: "POST", 
      contentType: "application/json",
      data: JSON.stringify(order),
      url: "/orders",
      success:function(data){
        vm.order = data;
        $.hideIndicator();
        if(callback) {
          callback();
        } else {
          $.toast("操作成功");
        }        
      }
    });
  }

  function doLoadOrders(start, end) {
    $.showIndicator();
    $.get("/orders?start=" + start + "&end=" + end, function(list){
      vm.orders = list;
      $.hideIndicator();
    });
  }

  // 初始化产品列表页面
  $(document).on("pageInit", "#page_order_list", function(){
    vm.refreshOrderList();
  });

  // 初始化产品列表页面
  $(document).on("pageInit", "#page_order_new", function(){
    $.showIndicator();
    $.get("/products", function(list){
      for(var i = 0; i < list.length; i++) {
        list[i].count = 0;
      }
      vm.products = list;
      $.hideIndicator();
    });   
  });

  // 初始化订单明细页面
  $(document).on("pageInit", "#page_order_detail", function() {
    if(vm.order.id > 0) {
      $.showIndicator();
      $.get("/orders/" + vm.order.id, function(order){
        vm.order = order;
        $.hideIndicator();
      });   
    }
  });

  $.init();
  // 日期选择
  $("#txtDate").calendar({
    dateFormat: "yyyy-mm-dd",
    inputReadOnly: true,
    onChange: function(p,value,display) {
      vm.queryDateStr = display[0];
      vm.refreshOrderList();
    }
  });
});

/* 计算日期 */
function week1(date) {
  var d = new Date(date);
  var week = (d.getDay() == 0 ? 7 : d.getDay()); 
  d.setDate(d.getDate() - week + 1);
  return d.getTime();
}
function week7(date){
  var d = new Date(date);
  var week = d.getDay() == 0 ? 7 : d.getDay(); 
  d.setDate(d.getDate() - week + 7);
  return d.getTime();
}
function month1(date){
  var d = new Date(date);
  d.setDate(1);
  return d.getTime();
}
function month30(date){
  var d = new Date(date);
  d.setMonth(d.getMonth() + 1);
  d.setDate(0);
  return d.getTime();
}

/* 判断字符串是否为空 */
function isNullOrEmpty(str) {
  if (str == null || str == undefined) {
    return true;
  } else {
    return $.trim(str) == "";
  }
}