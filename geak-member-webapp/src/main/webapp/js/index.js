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
      message: "",
      member: MEMBER,
      depositList: [],
      orderList: [],
      order:ORDER
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
      orderDepositPay: function(o) {
        var balance = vm.member.balance;
        var amount = o.amount - (this.orderHasDiscount(o) ? 10 : 0);
        if(amount > balance) {
          // 余额不足
          $.confirm("您账户余额不足，是否进行充值？", function() {
            $.router.load("#page_charge");
          });
        } else {
          $.confirm("您确定使用账户余额支付该订单吗？账户余额:￥" + balance + ",支付金额:￥" + amount, function() {
            $.showIndicator();
            $.post("/member/orders/dpay/" + o.id, function(data) {
              vm.member.balance = balance - data.amount;
              vm.order = data;
              $.hideIndicator();
              $.toast("操作成功");
            });
          });
        }
      },
      orderCancel: function(o) {
        $.confirm("您确定取消该订单嘛？", function() {
          $.showIndicator();
          $.post("/member/orders/unlink/" + o.id, function(data) {
            $.hideIndicator();
            $.toast("操作成功");
            $.router.load("#page_order_list");
            vm.order = {};
          });
        });
      },
      orderStateClass: function(item) {
        if(item.state == 'NEW' || item.state == 'UNPAYED') {
          return 'color-warning';
        } else {
          if(item.paymentMode=="1") {
            return 'color-primary';
          } else {
            return 'color-success';
          }
        } 
      },
      orderHasDiscount: function(item) {
        var _s = (item.state == 'NEW' || item.state == 'UNPAYED');
        return _s && (item.content.indexOf("工厂门票") >= 0);
      },
      orderStateName: function(o) {
        var mode = (o.paymentMode == "1" ? "余额" : "已");
        switch (o.state) {
          case "NEW" : return "新订单";
          case "UNPAYED" : return "未支付";
          case "PAYED" : return mode + "支付";
          case "ENTRANCED" : return mode + "支付";
          case "EXITED" : return mode + "支付";
          case "CANCELLED" : return "已取消";
        }
      },
      saveMember: function() {
          var _phone = $("#txt_phone").val();
          var _captcha = $("#txt_captcha").val();
          if(_phone.length != 11) {
            $.toast("请输入11位手机号码");
            return false;
          } else if(_captcha.length != 4) {
            $.toast("请输入4位验证码");
            return false;
          }
          
    	  $.showIndicator();
    	    $.ajax({
    	      type: "POST", 
    	      contentType: "application/json",
    	      data: JSON.stringify(vm.member),
    	      url: "/member/info/" + _captcha,
    	      success:function(rt){
    	        $.hideIndicator();
    	        if(rt.error) {
    	        	vm.message = rt.error;
		            $.toast(rt.error); 
		          } else {
		        	  vm.message = " ";
		            $.toast("操作成功");            
		            $.router.load("#page_index");
		          }   
    	      }
    	    });
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
  
  if(!vm.member.phone && window.INDEX) {
	  vm.message = "请完善您的联系方式，以便我们提供更好的服务。";
	  $.router.load("#page_member_info");
  }
});


function fetchCaptcha() {
    var _phome = $("#txt_phone").val();
    if(_phome.length != 11) {
      $.toast("请输入11位手机号码");
    } else {
      $.showIndicator();
      $.get("/member/captcha?phone="+_phome, function(rt){          
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
