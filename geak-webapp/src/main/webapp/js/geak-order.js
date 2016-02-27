(function($){
  var SOURCES = ["老玩家","团购","连续场","地推","朋友介绍","搜索","各店互推","其他","合作商"];
  var CUSTOMER_TYPES = ["小学生", "中学生", "大学生", "青年人", "中年人", "老年人"];
  var COMPANIES = [{"id":1, "name":"大南门店" },{"id":2, "name":"体育路店" },{"id":3, "name":"食品街店" },
                   {"id":4, "name":"柳巷店" },{"id":5, "name":"长风店" },{"id":6, "name":"千峰店" }];
  var LOADING = false;

  // 获取预约的起始时间
  var TODAY = new Date();
  TODAY.setHours(0);
  TODAY.setMinutes(0);
  TODAY.setSeconds(0);
  TODAY.setMilliseconds(0);
  TODAY = TODAY.getTime();

  function showDetail(id) {
    bindDetail();
    $.router.loadPage("#page_detail");
    if(id > 0) {
      $.showIndicator();
      $.get("/orders/"+id, function(detail){
        bindDetail(detail);
        $.hideIndicator();
      });
    }
  }

  window.QUERY_LIST = function(){
    refresh();
  }

  //刷新接待
  function refresh(ispull) {
    if(!ispull) {
      $.showIndicator();
    }

    // 在geak.js中定义
    var param = window.QUERY_PARAM;
    apiGetOrders(param.start, param.end, param.timespan, param.business, function(list){
      // 先清空列表
      $("#list_new,#list_payed,#list_entranced,#list_exited,#list_cancelled").empty();
      if(list.length > 0) {
        $.each(list, function(i,item){
          var html = tmpl("tmpl_card_item", item)
          $("#list_" + item.state.toLowerCase()).append(html);
        });
        $("#list_new>li,#list_payed>li,#list_entranced>li,#list_exited>li,#list_cancelled>li").each(function(){ 
          bindEvent(this);
        });
      }
      refreshView();
      if(!ispull) {
        $.hideIndicator();
      } else {
        $.pullToRefreshDone('.pull-to-refresh-content');
      }
    });
  }

  // 根据列表详情决定是否显示
  function refreshView() {
    $.each(["new", "payed", "entranced", "exited", "cancelled"], function(i,name){
      if($("#list_"+name + ">li").length == 0) {
        $("#list_"+name+"_empty").show();
      } else {
        $("#list_"+name+"_empty").hide();
      }
    });
  }

  function refreshDetail(detail) {
    var id = detail.id;
    var datetime = detail.createdDatetime;
    var $list = $("#list_"+detail.state.toLowerCase());
    var $ref = null;
    // 按顺序查找应该插入的位置
    $("li", $list).each(function(){
      var d = parseInt($(this).data("datetime"));
      if(d > datetime) {
        $ref = $(this);
        return false;
      }
    });
    $("#card_" + id).remove();
    var result = tmpl("tmpl_card_item", detail);
    if($ref) {
      $(result).insertBefore($ref);
    } else {
      $list.append(result);
    }
    bindEvent($("#card_" + id));
    refreshView();
  }

  function saveDetail() {
    var detail = parseDetail();
    if(detail) {
      $.showIndicator();
      $.ajax({
        type : "POST",
        url : "/orders",
        data : JSON.stringify(detail),
        contentType : "application/json",
        success : function(data){
          refreshDetail(data);
          //bindDetail(data);
          $("#card_empty").remove();
          $.hideIndicator();
          $.router.back("#page_list");
        }
      });
    }
  }

  function parseDetail() {
    var price = isNullOrEmpty($("#item_total_price").val()) ? 0 : $("#item_total_price").val().trim();
    var detail = {
      "business": null,
      "customer": { 
        "sex": $("#item_customer_sex option:selected").val(), 
        "name": $("#item_customer_name").val(), 
        "telephone": $("#item_customer_tele").val() 
      },
      "customerCount": parseInt($("#item_customer_count").val()),
      "id": $("#item_id").val(),
      "state": $("#item_state").val(),      
      "promotionNote": $("#item_promotion_note").val(),
      "totalPrice": parseInt(price),
      "source": _parseCheckboxData("#list_source input:checked"),
      "customerType": _parseCheckboxData("#list_customer_type input:checked"),
      "payments":_parsePayment(),
      "promotions":_parsePromotion(),
      "company": COMPANY
    };
    $("#list_business input:checked").each(function(){
      detail.business = {"id":$(this).val(), "alias":$(this).data("alias")};
    });

    if(!detail.business) {
      $.toast("请选择接待的主题！");
      return false;
    } else if(isNullOrEmpty(detail.customer.name)){
      $.toast("请输入玩家姓名！");
      return false;
    } else if(isNaN(detail.totalPrice)){
      $.toast("请输入正确的支付总额！");
      return false;
    } else if(!(detail.customerCount>0)){
      $.toast("请输入玩家人数！");
      return false;
    } else if(isNullOrEmpty(detail.customer.telephone)){
      $.toast("请输入联系方式！");
      return false;
    }

    return detail;
  }

  function _parseCheckboxData(selector) {
    var source = [];
    $(selector).each(function(){ source.push($(this).val()); });
    return source.length > 0 ? source.join(",") : null;
  }

  function _parsePayment() {
    var payments = [];
    $("#list_payment input").each(function(){ 
      var amount = parseInt($(this).val());
      if(amount > 0) {
        payments.push({ "amount":amount, "mode":{"id":$(this).data("id")} });
      }
    });
    return payments;
  }

  function _parsePromotion() {
    var promotions = [];
    $("#list_promotion input").each(function(){ 
      if($(this).attr("id") != "item_promotion_note" // 忽略其他优惠
          && $(this).attr("id") != "_p_6") {  // 打车劵另行处理
        var count = parseInt($(this).val());
        if(count > 0) {
          promotions.push({ "count":count, "plan":{"id":$(this).data("id")}, "note":"" });
        }
      }
    });
    // 处理打车券
    var price = parseInt($("#_p_6").val());
    if(price > 0) {
      promotions.push({ "count":price, "plan":{"id":6}, "note":$("#_p_6_note option:selected").val() });
    }
    return promotions;
  }

  function bindDetail(detail) {
    var item = detail ? detail :  {
      "business": {},
      "customer": { "name": "", "sex": "M", "telephone": "" },
      "customerCount": 5,
      "createdDatetime": moment().valueOf(),
      "entranceDatetime": null,
      "exitDatetime": null,
      "totalPrice": 0,
      "id": 0,
      "payments": [],
      "promotions": [],
      "state": "NEW"
    };
    $("#item_id").val(item.id);
    $("#item_state").val(item.state);
    $("#item_total_price").val(item.totalPrice);
    $("#item_customer_name").val(item.customer.name);
    $("#item_customer_sex option[value='"+item.customer.sex+"']").attr("selected", "selected");
    $("#item_customer_count").val(item.customerCount);
    $("#item_customer_tele").val(item.customer.telephone);
    $("#list_business input[type='checkbox']").prop("checked",false);
    if(item.business && item.business.id) {
      $("#_b_"+item.business.id).prop("checked",true);
    }
    _bindSource(item.source);
    _bindCustomerType(item.customerType);
    _bindPayments(item.payments);
    _bindPromotions(item.promotions);
    $("#item_promotion_note").val(item.promotionNote);
    $("#item_total_price").val(item.totalPrice);
    // 绑定时间信息
    var datetime = item.appointments ? item.appointments.confirmedDatetime : item.createdDatetime;
    $("#item_datetime").val(moment(datetime).format("YYYY-MM-DD HH:mm"));
    // 绑定状态信息
    if(item.state == "NEW") {
      $("#btn_save").show();
      $("#btn_extrnace").hide();
      $("#btn_exit").hide();
      $("#item_entrance_datetime").parents("li").hide();
      $("#item_exit_datetime").parents("li").hide();
      $("#item_cancel_datetime").parents("li").hide();
      if(item.id == 0) {
        // 新建
        $("#btn_cancel").hide();
        $("#state_name").text("新建");
        $("#page_detail .content-block-title").attr("class", "content-block-title");
      } else {
        $("#btn_cancel").show();
        $("#state_name").text("未支付");
        $("#page_detail .content-block-title").attr("class", "content-block-title color-danger");
      }
    } else if(item.state == "PAYED") {
      $("#btn_save").show();
      $("#btn_extrnace").show();
      $("#btn_exit").hide();
      $("#btn_cancel").hide();
      $("#item_entrance_datetime").parents("li").hide();
      $("#item_exit_datetime").parents("li").hide();
      $("#item_cancel_datetime").parents("li").hide();
      $("#state_name").text("已支付");
      $("#page_detail .content-block-title").attr("class", "content-block-title color-warning");
    } else if(item.state == "ENTRANCED") {
      $("#btn_save").hide();
      $("#btn_extrnace").hide();
      $("#btn_exit").show();
      $("#btn_cancel").hide();
      $("#item_entrance_datetime").val(moment(item.entranceDatetime).format("YYYY-MM-DD HH:mm"));
      $("#item_entrance_datetime").parents("li").show();
      $("#item_exit_datetime").parents("li").hide();
      $("#item_cancel_datetime").parents("li").hide();
      $("#state_name").text("已入场");
      $("#page_detail .content-block-title").attr("class", "content-block-title color-primary");
    } else if(item.state == "EXITED") {
      $("#btn_save").hide();
      $("#btn_extrnace").hide();
      $("#btn_exit").hide();
      $("#btn_cancel").hide();
      $("#item_entrance_datetime").val(moment(item.entranceDatetime).format("YYYY-MM-DD HH:mm"));
      $("#item_exit_datetime").val(moment(item.exitDatetime).format("YYYY-MM-DD HH:mm"));
      $("#item_entrance_datetime").parents("li").show();
      $("#item_exit_datetime").parents("li").show();
      $("#item_cancel_datetime").parents("li").hide();
      $("#state_name").text("已离场");
      $("#page_detail .content-block-title").attr("class", "content-block-title color-success");
    } else if(item.state == "CANCELLED") {
      $("#btn_save").hide();
      $("#btn_extrnace").hide();
      $("#btn_exit").hide();
      $("#btn_cancel").hide();
      $("#item_cancel_datetime").val(moment(item.cancelledDatetime).format("YYYY-MM-DD HH:mm"));
      $("#item_cancel_datetime").parents("li").show();
      $("#item_entrance_datetime").parents("li").hide();
      $("#item_exit_datetime").parents("li").hide();
      $("#state_name").text("已取消");
      $("#page_detail .content-block-title").attr("class", "content-block-title");
    }
  }

  function _bindSource(source) {
    var array = source ? source.split(",") : [];
    $("#list_source input[type='checkbox']").prop("checked",false);
    $.each(array, function(i,name) {
      $("#list_source input[value='"+name+"']").prop("checked",true);
    });
  }
  function _bindCustomerType(types) {
    var array = types ? types.split(",") : [];
    $("#list_customer_type input[type='checkbox']").prop("checked",false);
    $.each(array, function(i,name) {
      $("#list_customer_type input[value='"+name+"']").prop("checked",true);
    });
  }
  function _bindPayments(payments) {
    $("#list_payment input").val("");
    $.each(payments, function(i,item) {
       $("#_m_"+item.mode.id).val(item.amount);
    });
  }
  function _bindPromotions(promotions) {
    $("#list_promotion input").val("");
    $("#_p_6_note option").removeAttr("selected");
    $.each(promotions, function(i,item) {
        var id = "#_p_"+item.plan.id;
        $(id).val(item.count);
        // 特殊处理打车劵
        if(id=="#_p_6") {
          $("#_p_6_note option[value='"+item.note+"']").attr("selected", "selected");
        }
    });
  }

  // 绑定卡片事件
  function bindEvent(li) {
    var id = $(li).data("id");
    $(li).click(function(){
      showDetail(id);
    });

    $("button.pay", li).click(function(e){
      e.stopPropagation();
      showDetail(id);
    });

    $("button.cancel", li).click(function(e){
      e.stopPropagation();
      confirmCancel(id);
    });

    $("button.entrance", li).click(function(e){
      e.stopPropagation();
      confirmEntrance(id);
    });

    $("button.exit", li).click(function(e){
      e.stopPropagation();
      confirmExit(id);
    });
  }

  // 取消接待
  function confirmCancel(id, goback) {
    var name = $("#card_"+id+" .item-name").text();
    var business = $("#card_"+id+" .item-business").text();
    var content = "<div style='text-align:left;margin-bottom:-0.5rem'>玩家：" 
          + name + "<br/>主题：" +  business + "</div>";
    $.confirm(content, "您确定取消接待吗？", function (value) {
      $.showIndicator();
      $.post("/orders/" + id + "/cancel", function(detail){
        $.hideIndicator();
        $.toast("接待已取消");
        refreshDetail(detail);
        bindDetail(detail);
        if(goback) {
          $.router.back("#page_list");
        }
      });
    });
  }

  // 确认入场
  function confirmEntrance(id, goback) {
    var name = $("#card_"+id+" .item-name").text();
    var business = $("#card_"+id+" .item-business").text();
    var content = "<div style='text-align:left;margin-bottom:-0.5rem'>玩家：" 
          + name + "<br/>主题：" +  business + "<br/>入场时间：</div>";
    $.prompt(content,"确认玩家入场", function (value) {
        var date = moment(value).valueOf();
        if(date) {
          $.showIndicator();
          $.ajax({
            type : "POST",
            url : "/orders/" + id + "/entrance",
            data : {"datetime":date},
            success : function(detail){
              $.hideIndicator();
              $.toast("确认成功");
              refreshDetail(detail);
              bindDetail(detail);
              if(goback) {
                $.router.back("#page_list");
              }
            }
          });
        } else {
          $.toast("请输入正确的入场时间！");
        }
      }
    );
    var now = moment();
    $("div.modal-inner input.modal-text-input").val(now.format("YYYY-MM-DD HH:mm"));
  }

  // 确认离场
  function confirmExit(id, goback) {
    var name = $("#card_"+id+" .item-name").text();
    var business = $("#card_"+id+" .item-business").text();
    var content = "<div style='text-align:left;margin-bottom:-0.5rem'>玩家：" 
          + name + "<br/>主题：" +  business + "<br/>离场时间：</div>";
    $.prompt(content,"确认玩家离场", function (value) {
        var date = moment(value).valueOf();
        if(date) {
          $.showIndicator();
          $.ajax({
            type : "POST",
            url : "/orders/" + id + "/exit",
            data : {"datetime":date},
            success : function(detail){
              $.hideIndicator();
              $.toast("确认成功");
              refreshDetail(detail);
              bindDetail(detail);
              if(goback) {
                $.router.back("#page_list");
              }
            }
          });
        } else {
          $.toast("请输入正确的离场时间！");
        }
      }
    );
    var now = moment();
    $("div.modal-inner input.modal-text-input").val(now.format("YYYY-MM-DD HH:mm"));
  }

  /* 加载订单列表 */
  function apiGetOrders(start, end, timespan, business, callback)  {
    if(LOADING) return;
    LOADING = true;
    $.ajax({
      url:"/orders",
      data:{
        "company" : COMPANY.id,
        "start" : start,
        "end" : end,
        "timespan" : (timespan && timespan.length > 0) ? timespan.join(",") : null,
        "business" : (business && business.length > 0) ? business.join(",") : null
      },
      dataType:"json", 
      success:function(list){
        callback(list);
        LOADING = false;
      }
    });
  }

  /* 加载各类基础信息信息 */
  function loadBusinesses() {
    $.get("/businesses?company=" + COMPANY.id, function(list){
      $("#list_business").html(tmpl("tmpl_business", list));
      // 绑定单选事件
      $("#list_business input[type='checkbox']").change(function(){
        var $this = $(this);
        $("#list_business input[type='checkbox']").each(function(){
          if($this.attr("id") != $(this).attr("id")) {
            $(this).prop("checked",false);
          }
        });
      });
    });
    
  }
  function loadSources() {
    $("#list_source").html(tmpl("tmpl_source", SOURCES));
  }
  function loadCustomerType() {
    $("#list_customer_type").html(tmpl("tmpl_customer_type", CUSTOMER_TYPES));
  }
  function loadPayments() {
    $.get("/payments", function(list){
      $("#list_payment").html(tmpl("tmpl_payment", list));
      // 绑定单选事件
      $("#list_payment input").bind("input propertychange", function(){
        computeSum();
      });
    });
  }
  function loadPromotions() {
    $.get("/promotions", function(list){
      list.splice(5,1); // 打车劵特殊处理
      $("#list_promotion").prepend(tmpl("tmpl_promotion", list));
    });
  }

  function computeSum() {
    var sum = 0;
    $("#list_payment input").each(function(){
      var amount = parseInt($(this).val());
      var price = parseInt($(this).data("price"));
      if(amount) sum += amount * price;
    });
    $("#item_total_price").val(sum);
  }

  /* 判断字符串是否为空 */
  function isNullOrEmpty(str) {
    if (str == null || str == undefined) {
      return true;
    } else {
      return $.trim(str) == "";
    }
  }

  function initPage() {
    // 打车劵来源信息初始化
    var list = [1,2,3,4,5,6];
    if(COMPANY.id == "2"||COMPANY.id == "5"||COMPANY.id == "6") {
      list.splice(COMPANY.id - 1, 1);
    } else {
      list = [2,5,6];
    }
    $.each(list, function(i,v){
      var cname = COMPANIES[v-1].name;
      $("#_p_6_note").append('<option value="'+cname+'">'+cname+'</option>');
    });
  }

  // init
  $(function(){
    $.init();
    initPage();

    $("#btn_refresh").click(function(){ refresh(); });
    $("#btn_create").click(function(){ showDetail(); });
    $("#btn_back").click(function(){ $.router.back("#page_list"); });
    $("#btn_save").click(function(){ saveDetail(); });
    $("#btn_extrnace").click(function(){ confirmEntrance($("#item_id").val(), true);});
    $("#btn_exit").click(function(){ confirmExit($("#item_id").val(), true);  });
    $("#btn_cancel").click(function(){ confirmCancel($("#item_id").val(), true);});
    refresh();

    loadBusinesses();
    loadCustomerType();
    loadSources();
    loadPayments();
    loadPromotions();

    // 下拉刷新
    $(document).on('refresh', '.pull-to-refresh-content',function(e) {
      refresh(true);
    });
  });

})(jQuery);