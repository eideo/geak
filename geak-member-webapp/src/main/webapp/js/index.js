
$(function(){

  window.vm = new Vue({
    "el": "#page_index",
    "data": {
      member: MEMBER
    },
    methods: {
      
    }
  });

  // #page_charge
  window.vmCharge = new Vue({
    "el": "#page_charge",
    "data": { amount: 0 },
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
      }
    }
  });


});