var Orders = [
  {"id":1, "amount":123, "state":"已支付", "payMode":"0", "payNote":"", "payDetail":[
    {"mode":"现金", "amount":0},
  ], "discountDetail":[],"discountNote":"","member":{name:"",phone:"",sex:"M",type:[]}, "createdDate":"2016-05-06T08:12:00", "products":[
    {"type":"门票","name":"密室1","price":68,"price0":80,"count":3,"image":"http://gqianniu.alicdn.com/bao/uploaded/i4//tfscom/i3/TB10LfcHFXXXXXKXpXXXXXXXXXX_!!0-item_pic.jpg_250x250q60.jpg"},
    {"type":"商品","name":"葡萄干","price":68,"price0":80,"count":4,"image":"http://gqianniu.alicdn.com/bao/uploaded/i4//tfscom/i3/TB10LfcHFXXXXXKXpXXXXXXXXXX_!!0-item_pic.jpg_250x250q60.jpg"},
  ]},

  {"id":2, "amount":456, "state":"未支付", "payMode":"1","payNote":"", "payDetail":[], "createdDate":"2016-05-07T08:12:00", "products":[
    {"type":"门票","name":"密室1","price":68,"price0":80,"count":3,"image":"http://gqianniu.alicdn.com/bao/uploaded/i4//tfscom/i3/TB10LfcHFXXXXXKXpXXXXXXXXXX_!!0-item_pic.jpg_250x250q60.jpg"},
    {"type":"商品","name":"葡萄干","price":68,"price0":80,"count":4,"image":"http://gqianniu.alicdn.com/bao/uploaded/i4//tfscom/i3/TB10LfcHFXXXXXKXpXXXXXXXXXX_!!0-item_pic.jpg_250x250q60.jpg"},
  ]},
  {"id":3, "amount":789, "state":"已离场", "payMode":"1","payNote":"", "payDetail":[], "createdDate":"2016-05-08T08:12:00", "products":[
    {"type":"门票","name":"密室1","price":68,"price0":80,"count":3,"image":"http://gqianniu.alicdn.com/bao/uploaded/i4//tfscom/i3/TB10LfcHFXXXXXKXpXXXXXXXXXX_!!0-item_pic.jpg_250x250q60.jpg"},
    {"type":"商品","name":"葡萄干","price":68,"price0":80,"count":4,"image":"http://gqianniu.alicdn.com/bao/uploaded/i4//tfscom/i3/TB10LfcHFXXXXXKXpXXXXXXXXXX_!!0-item_pic.jpg_250x250q60.jpg"},
  ]},
  {"id":4, "amount":321, "state":"已取消", "payMode":"1","payNote":"", "payDetail":[], "createdDate":"2016-05-09T08:12:00", "products":[
    {"type":"门票","name":"密室1","price":68,"price0":80,"count":3,"image":"http://gqianniu.alicdn.com/bao/uploaded/i4//tfscom/i3/TB10LfcHFXXXXXKXpXXXXXXXXXX_!!0-item_pic.jpg_250x250q60.jpg"},
    {"type":"商品","name":"葡萄干","price":68,"price0":80,"count":4,"image":"http://gqianniu.alicdn.com/bao/uploaded/i4//tfscom/i3/TB10LfcHFXXXXXKXpXXXXXXXXXX_!!0-item_pic.jpg_250x250q60.jpg"},
  ]},
  {"id":5, "amount":654, "state":"未确认", "payMode":"0","payNote":"", "payDetail":[], "createdDate":"2016-05-10T08:12:00", "products":[
    {"type":"门票","name":"密室1","price":68,"price0":80,"count":3,"image":"http://gqianniu.alicdn.com/bao/uploaded/i4//tfscom/i3/TB10LfcHFXXXXXKXpXXXXXXXXXX_!!0-item_pic.jpg_250x250q60.jpg"},
    {"type":"商品","name":"葡萄干","price":68,"price0":80,"count":4,"image":"http://gqianniu.alicdn.com/bao/uploaded/i4//tfscom/i3/TB10LfcHFXXXXXKXpXXXXXXXXXX_!!0-item_pic.jpg_250x250q60.jpg"},
  ]}
];

Vue.filter("date", function (value) {
  var d = new Date(value);
  return d.getFullYear() + "-" + d.getMonth() + "-" + d.getDate();
});

$(function(){

  window.vm2 = new Vue({
    "el": "#page_order_list",
    "data": {
      orders: Orders,
      currentOrder: Orders[0],
      filterType: "",
    },
    methods: {
      // 查看订单详情
      viewOrder: function(order) {
        vm3.order = order;
        $.router.load("#page_order_detail");
      }
    }
  });

  window.vm3 = new Vue({
    "el": "#page_order_detail",
    "data": {
      order:Orders[0],
    }
  });

  $("#my-input").calendar({
    value: ['2015-12-05']
});
});


var PRODUCTS = [
                {"type":"门票","name":"密室1","price":68,"price0":80,"count":0,"image":"http://gqianniu.alicdn.com/bao/uploaded/i4//tfscom/i3/TB10LfcHFXXXXXKXpXXXXXXXXXX_!!0-item_pic.jpg_250x250q60.jpg"},
                {"type":"门票","name":"密室2","price":68,"price0":80,"count":0,"image":"http://gqianniu.alicdn.com/bao/uploaded/i4//tfscom/i3/TB10LfcHFXXXXXKXpXXXXXXXXXX_!!0-item_pic.jpg_250x250q60.jpg"},
                {"type":"商品","name":"密室3","price":68,"price0":80,"count":0,"image":"http://gqianniu.alicdn.com/bao/uploaded/i4//tfscom/i3/TB10LfcHFXXXXXKXpXXXXXXXXXX_!!0-item_pic.jpg_250x250q60.jpg"},
                {"type":"商品","name":"密室4","price":68,"price0":80,"count":0,"image":"http://gqianniu.alicdn.com/bao/uploaded/i4//tfscom/i3/TB10LfcHFXXXXXKXpXXXXXXXXXX_!!0-item_pic.jpg_250x250q60.jpg"},
                {"type":"商品","name":"密室5","price":68,"price0":80,"count":0,"image":"http://gqianniu.alicdn.com/bao/uploaded/i4//tfscom/i3/TB10LfcHFXXXXXKXpXXXXXXXXXX_!!0-item_pic.jpg_250x250q60.jpg"},  
                {"type":"商品","name":"密室6","price":68,"price0":80,"count":0,"image":"http://gqianniu.alicdn.com/bao/uploaded/i4//tfscom/i3/TB10LfcHFXXXXXKXpXXXXXXXXXX_!!0-item_pic.jpg_250x250q60.jpg"}
              ];

              $(function(){

                window.vm = new Vue({
                  "el": "#page_order_new",
                  "data": {
                    products: PRODUCTS,
                    member:{
                      name:"",
                      phone:"",
                      sex:"M",
                      type:[],
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
                    }
                  }
                });
              });