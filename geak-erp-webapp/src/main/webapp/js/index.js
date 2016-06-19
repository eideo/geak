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
      filterType: "",
      products: [],
      order: {
        member:{
          name:"",
          phone:"",
          sex:"M"
        }
      }
    },
    computed: {
      // 订单总价
      amount:function () {
        var t = 0;
        this.products.forEach(function(item){
          t += item.count * item.price;
        });
        return t;
      },
      orderStateName: function() {
        switch (this.order.state) {
          case "NEW" : return "新订单";
          case "UNPAYED" : return "未支付";
          case "PAYED" : return "已支付";
          case "ENTRANCED" : return "已进场";
          case "EXITED" : return "已离场";
          case "CANCELLED" : return "已取消";
        }
      }
    },
    methods: {
      orderAmount: function(items) {
        var t = 0;
        items.forEach(function(item){
          t += item.count * item.price;
        });
        return t;
      },
      productImage: function(item) {
        return "/img/product/37.jpg#" + item.id
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
      orderConfirmPay: function() {
        if(this.isValidOrder()) {
          var now = new Date();
          vm.order.paymentDate = now.getTime();
          doSaveOrder(vm.order);
        }
      },
      // 控制订单状态
      orderUnpay: function() {
        doShiftOrderState(vm.order, "确定重新支付该订单吗？", "unpay");
      },
      orderEntrance: function() {
        doShiftOrderState(vm.order, "确认玩家已进场？", "entrance");
      },
      orderExit: function() {
        doShiftOrderState(vm.order, "确认玩家已离场？", "exit");
      },
      orderCancel: function() {
        $.toast("TODO 取消订单");
        return;
        doShiftOrderState(vm.order, "确定取消订单吗？", "cancel");
      },
      // 验证订单
      isValidOrder: function() {
        if(isNullOrEmpty(vm.order.member.name)) {
          $.toast("请输入玩家姓名");
          return false;
        } else if(isNullOrEmpty(vm.order.member.phone)) {
          $.toast("请输入玩家联系方式");
          return false;
        } else if(isNaN(vm.order.amount) || vm.order.amount < 0){
          $.toast("请输入正确的支付总额");
          return false;
        }
        return true;
      }
    }
  });

  function doShiftOrderState(order, confirmContent, shiftState) {
    $.confirm(confirmContent, function() {
      $.showIndicator();
      $.post("/orders/" + order.id + "/" + shiftState, function(data){
        vm.order = data;
        $.hideIndicator();
        $.toast("操作成功");
      });
    });
  }

  function doSaveOrder(order) {
    $.showIndicator();
    $.ajax({
      type: "POST", 
      contentType: "application/json",
      data: JSON.stringify(order),
      url: "/orders",
      success:function(data){
        vm.order = data;
        $.hideIndicator();
      }
    });
  }


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
});

/* 判断字符串是否为空 */
function isNullOrEmpty(str) {
  if (str == null || str == undefined) {
    return true;
  } else {
    return $.trim(str) == "";
  }
}