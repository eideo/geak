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
      member: MEMBER,
      depositList: [],
      orderList: []
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
      }
    }
  });


  $(document).on("pageInit", "#page_index", function(){
    $.showIndicator();
    $.get("/member", function(m){
      vm.member = m;
      console.log(m.name);
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

  $.init();
});