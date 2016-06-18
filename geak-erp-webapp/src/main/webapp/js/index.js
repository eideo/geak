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
      }
    }
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


  $.init();
});