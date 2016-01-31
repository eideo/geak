(function($){
  var SOURCES = ["老玩家","团购","朋友介绍","连续场","地推","各店互推","合作商","搜索","其他来源"];
  var CUSTOMER_TYPES = ["小学生", "中学生", "大学生", "青年人", "中年人", "老年人"];
  var LOADING = false;
  var CACHE = {};

  // 获取预约的起始时间
  var TODAY = new Date();
  TODAY.setHours(0);
  TODAY.setMinutes(0);
  TODAY.setSeconds(0);
  TODAY.setMilliseconds(0);
  TODAY = TODAY.getTime();

  var UPPER_DATE = LOWER_DATE = TODAY;
  var UPPER_LOADING = LOWER_LOADING = false;

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

  //刷新预约
  function refresh() {
    $.showIndicator();
    CACHE = {}; // 清空缓存列表
    UPPER_DATE = LOWER_DATE = TODAY; // 重新绑定上下限时间
    apiGetOrders(TODAY, null, 1, function(list){
      if(list.length == 0) {
        // 展示空提示
        $("#list").html('<li id="card_empty" class="card"><div class="card-header">'
              +  '<label class="color-danger">今日暂时没有任何接待</label>'
              +  '<button class="button" onclick="$(\'#btn_refresh\').click();">'
              +    '<i class="icon icon-refresh"></i> 刷新</label>'
              + '</div>'
            + '</li>');
      } else {
        $("#list").html(tmpl("tmpl_card", list));
        $("#list>li").each(function(){
          bindEvent(this);
        });
      }
      $.hideIndicator();
    });
  }

  function refreshDetail(detail) {
    var result = tmpl("tmpl_card_item", detail);
    if(CACHE[detail.id]) {
      $("#card_" + detail.id).prop("outerHTML", result);
    } else {
      var datetime = detail.createdDatetime;
      var refLi = null;
      // 按顺序查找应该插入的位置
      $("#list>li").each(function(){
        var d = parseInt($(this).data("datetime"));
        if(d < datetime) {
          refLi = $(this);
          return false;
        }
      });
      if(refLi) {
        $(result).insertBefore(refLi);
      } else {
        $("#list").append(result);
      }
      CACHE[detail.id] = true;
    }
    bindEvent($("#card_" + detail.id));
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
          bindDetail(data);
          $("#card_empty").remove();
          $.hideIndicator();
          $.toast("接待信息保存成功！");
        }
      });
    }
  }

  function parseDetail() {
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
      var count = parseInt($(this).val());
      if(count > 0) {
        promotions.push({ "count":count, "plan":{"id":$(this).data("id")} });
      }
    });
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
      "id": 0,
      "payments": [],
      "promotions": [],
      "state": "NEW"
    };
    $("#item_id").val(item.id);
    $("#item_state").val(item.state);
    $("#item_customer_name").val(item.customer.name);
    $("#item_customer_sex option[value='"+item.customer.sex+"']").attr("selected", "selected");
    $("#item_customer_count").val(item.customerCount);
    $("#item_customer_tele").val(item.customer.telephone);
    $("#list_business input[type='checkbox']").prop("checked",false);
    $("#_b_"+item.business.id).prop("checked",true);
    _bindSource(item.source);
    _bindCustomerType(item.customerType);
    _bindPayments(item.payments);
    _bindPromotions(item.promotions);
    computeSum();
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
      if(item.id == 0) {
        // 新建
        $("#state_name").text("新建");
        $("#page_detail .content-block-title").attr("class", "content-block-title");
      } else {
        $("#state_name").text("未支付");
        $("#page_detail .content-block-title").attr("class", "content-block-title color-danger");
      }
    } else if(item.state == "PAYED") {
      $("#btn_save").show();
      $("#btn_extrnace").show();
      $("#btn_exit").hide();
      $("#item_entrance_datetime").parents("li").hide();
      $("#item_exit_datetime").parents("li").hide();
      $("#state_name").text("已支付");
      $("#page_detail .content-block-title").attr("class", "content-block-title color-warning");
    } else if(item.state == "ENTRANCED") {
      $("#btn_save").hide();
      $("#btn_extrnace").hide();
      $("#btn_exit").show();
      $("#item_entrance_datetime").val(moment(item.entranceDatetime).format("YYYY-MM-DD HH:mm"));
      $("#item_entrance_datetime").parents("li").show();
      $("#item_exit_datetime").parents("li").hide();
      $("#state_name").text("已入场");
      $("#page_detail .content-block-title").attr("class", "content-block-title color-primary");
    } else if(item.state == "EXITED") {
      $("#btn_save").hide();
      $("#btn_extrnace").hide();
      $("#btn_exit").hide();
      $("#item_entrance_datetime").val(moment(item.entranceDatetime).format("YYYY-MM-DD HH:mm"));
      $("#item_exit_datetime").val(moment(item.exitDatetime).format("YYYY-MM-DD HH:mm"));
      $("#item_entrance_datetime").parents("li").show();
      $("#item_exit_datetime").parents("li").show();
      $("#state_name").text("已离场");
      $("#page_detail .content-block-title").attr("class", "content-block-title color-success");
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
    $.each(promotions, function(i,item) {
       $("#_p_"+item.plan.id).val(item.count);
    });
  }

  // 绑定卡片事件
  function bindEvent(li) {
    var id = $(li).data("id");
    $(li).click(function(){
      showDetail(id);
    });

    $("button", li).click(function(e){
      e.stopPropagation();
      var state = $(this).data("state");
      if(state == "NEW") {
        showDetail(id);
      } else if(state == "PAYED") {
        confirmEntrance(id);
      } else if(state == "ENTRANCED") {
        confirmExit(id);
      } 
    });
  }

  // 确认入场
  function confirmEntrance(id) {
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
              $.toast("确认成功！");
              refreshDetail(detail);
              bindDetail(detail);
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
  function confirmExit(id) {
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
              $.toast("确认成功！");
              refreshDetail(detail);
              bindDetail(detail);
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

  /* 加载预约列表 */
  function apiGetOrders(datetime, business, page, callback) {
    if(LOADING) return;
    LOADING = true;
    $.ajax({
      url:"/orders",
      data:{
        "company" : COMPANY.id,
        "page" : page,
        "datetime" : datetime
      },
      dataType:"json", 
      success:function(list){
        // 整体加载数据的情况
        if(list && list.length > 0) {
          if(list[0].datetime > UPPER_DATE ) {
            UPPER_DATE = list[0].datetime;
          }
          if(list[list.length-1].datetime < LOWER_DATE ) {
            LOWER_DATE = list[list.length-1].datetime;
          }
        }
        var data = [];
        $.each(list, function(i,item){
          if(!CACHE[item.id]) {
            CACHE[item.id] = true;
            data.push(item);
          }
        });
        callback(data);
        if(data.length > 0) {
          $("#card_empty").remove();
        }
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
      $("#list_promotion").html(tmpl("tmpl_promotion", list));
    });
  }

  function computeSum() {
    var sum = 0;
    $("#list_payment input").each(function(){
      var amount = parseInt($(this).val());
      if(amount) sum += amount;
    });
    $("#item_sum").text(sum);
  }

  function loadMore() {
    if (LOWER_LOADING) return;
    LOWER_LOADING = true;
    $("#btn_more").hide();
    $('.infinite-scroll-preloader .preloader').show();
    apiGetOrders(LOWER_DATE, null, -1, function(list){
      if(list.length == 0) {
        $.detachInfiniteScroll($('.infinite-scroll'));
        $('.infinite-scroll-preloader .preloader').hide();
        $("#btn_more").show();
        $.toast("已经没有更早的接待数据。");
      } else {
        $("#card_empty").remove();
        $("#list").append(tmpl("tmpl_card", list));
        $("#list>li").each(function(){
          bindEvent(this);
        });
      }
      LOWER_LOADING = false;
    });
  }

  /* 判断字符串是否为空 */
  function isNullOrEmpty(str) {
    if (str == null || str == undefined) {
      return true;
    } else {
      return $.trim(str) == "";
    }
  }

  // init
  $(function(){
    $.init();

    $("#btn_refresh").click(function(){ refresh(); });
    $("#btn_create").click(function(){ showDetail(); });
    $("#btn_back").click(function(){ $.router.back("#page_list"); });
    $("#btn_save").click(function(){ saveDetail(); });
    $("#btn_more").click(function(){ loadMore(); });
    $("#btn_extrnace").click(function(){ confirmEntrance($("#item_id").val()); });
    $("#btn_exit").click(function(){ confirmExit($("#item_id").val()); });
    refresh();

    loadBusinesses();
    loadCustomerType();
    loadSources();
    loadPayments();
    loadPromotions();

    // 下拉刷新
    $(document).on('refresh', '.pull-to-refresh-content',function(e) {
      if (UPPER_LOADING) return;
      UPPER_LOADING = true;
      apiGetOrders(UPPER_DATE, null, 1, function(list){
        if(list.length == 0) {
          $.toast("暂无最新的接待数据。");
        } else {
          $("#card_empty").remove();
          $("#list").prepend(tmpl("tmpl_card", list));
          $("#list>li").each(function(){
            bindEvent(this);
          });
        }
        $.pullToRefreshDone('.pull-to-refresh-content');
        UPPER_LOADING = false;
      });
    });

    // 向下滚动加载
    $(document).on('infinite', '.infinite-scroll',function() {
      loadMore();
    });
  });

})(jQuery);