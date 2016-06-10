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