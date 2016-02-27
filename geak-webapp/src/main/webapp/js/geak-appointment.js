(function($){
  var SOURCES = ["老玩家","团购","连续场","地推","朋友介绍","搜索","各店互推","其他","合作商"];
  var LOADING = false;

  function showDetail(id) {
    bindDetail();
    $.router.loadPage("#page_detail");
    if(id > 0) {
      $.showIndicator();
      $.get("/appointments/"+id, function(detail){
        bindDetail(detail);
        $.hideIndicator();
      });
    }
  }

  window.QUERY_LIST = function(){
    refresh();
  }

  //刷新预约
  function refresh(ispull) {
    if(!ispull) {
      $.showIndicator();
    }

    // 在geak.js中定义
    var param = window.QUERY_PARAM;
    apiGetAppointments(param.start, param.end, param.timespan, param.business, function(list){
      // 先清空列表
      $("#list_new, #list_confirmed, #list_cancelled").empty();
      if(list.length > 0) {
        $.each(list, function(i,item){
          var html = tmpl("tmpl_card_item", item)
          $("#list_" + item.state.toLowerCase()).append(html);
        });
        $("#list_new > li, #list_confirmed > li, #list_cancelled > li").each(function(){ 
          bindEvent(this);
        });
      }
      refreshView();
      if(!ispull) {
      $.hideIndicator();
    } else {
      $.pullToRefreshDone('.pull-to-refresh-content');
    }
      $.hideIndicator();
    });
  }

  // 根据列表详情决定是否显示
  function refreshView() {
    $.each(["new", "confirmed", "cancelled"], function(i,name){
      if($("#list_"+name + ">li").length == 0) {
        $("#list_"+name+"_empty").show();
      } else {
        $("#list_"+name+"_empty").hide();
      }
    });
  }

  function refreshDetail(detail) {
    var id = detail.id;
    var datetime = detail.datetime;
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
        url : "/appointments",
        data : JSON.stringify(detail),
        contentType : "application/json",
        success : function(data){
          refreshDetail(data);
          $("#card_empty").remove();
          $.hideIndicator();
          $.router.back("#page_list");
        }
      });
    }
  }

  function parseDetail() {
    var detail = {
      "businesses": [],
      "customer": { 
        "sex": $("#item_customer_sex option:selected").val(), 
        "name": $("#item_customer_name").val(), 
        "telephone": $("#item_customer_tele").val() 
      },
      "datetime": moment($("#item_date").val() + " " + $("#item_time").val().substring(0,5)).valueOf(),
      "customerCount": parseInt($("#item_customer_count").val()),
      "id": $("#item_id").val(),
      "state": $("#item_state").val(),
      "source": _parseCheckboxData("#list_source input:checked"),
      "note": $("#item_note").val().trim(),
      "company": COMPANY
    };
    $("#list_business input:checked").each(function(){
      detail.businesses.push({"id":$(this).val(), "alias":$(this).data("alias")});
    });
      

    if(!detail.datetime){
      $.toast("请输入正确的预约时间！");
      return false;
    } else if(detail.datetime < (new Date().getTime()-60*60*1000) ){
      $.toast("请输入合理的预约时间！");
      return false;
    } else if(isNullOrEmpty(detail.customer.name)){
      $.toast("请输入玩家姓名！");
      return false;
    } else if(!(detail.customerCount>0)){
      $.toast("请输入预约人数！");
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

  function bindDetail(detail) {
    var a = detail ? detail :  {
      "businesses": [],
      "customer": { "name": "", "sex": "M", "telephone": "" },
      "customerCount": 5,
      "datetime": moment().valueOf() + 60*60*1000,
      "id": 0,
      "note": "",
      "state": "NEW"
    };
    $("#item_id").val(a.id);
    $("#item_state").val(a.state);
    $("#item_note").val(a.note);
    $("#item_customer_name").val(a.customer.name);
    $("#item_customer_sex option[value='"+a.customer.sex+"']").attr("selected", "selected");
    $("#item_customer_count").val(a.customerCount);
    $("#item_customer_tele").val(a.customer.telephone);
    $("#list_business input[type='checkbox']").prop("checked",false);
    $.each(a.businesses, function(i,item) {
      $("#_b_"+item.id).prop("checked",true);
    });

    var dt = moment(a.datetime);
    $("#item_date").val(dt.format("YYYY-MM-DD"));
    var hour = dt.format("HH");
    $("#item_time").val(window.TIMES[0]);
    $.each(window.TIMES, function(i,span){
      if(span.indexOf(hour) == 0) {
        $("#item_time").val(span);
        return false;
      }
    });
 
    $("#btn_save").hide();
    _bindSource(a.source);
    if(a.state == "NEW") {
      $("#btn_save").show();
      $("#item_cancelled_datetime").parents("li").hide();
      $("#item_confirmed_datetime").parents("li").hide();
    } else if(a.state == "CONFIRMED") {
      $("#item_confirmed_datetime").val(moment(a.confirmedDatetime).format("YYYY-MM-DD HH:mm"));
      $("#item_cancelled_datetime").parents("li").hide();
      $("#item_confirmed_datetime").parents("li").show();
    } else if(a.state == "CANCELLED") {
      $("#item_cancelled_datetime").val(moment(a.cancelledDatetime).format("YYYY-MM-DD HH:mm"));
      $("#item_cancelled_datetime").parents("li").show();
      $("#item_confirmed_datetime").parents("li").hide();
    }
  }

  function _bindSource(sourceStr) {
    var sourceArray = sourceStr ? sourceStr.split(",") : [];
    $("#list_source input[type='checkbox']").prop("checked",false);
    $.each(sourceArray, function(i,name) {
      $("#list_source input[value='"+name+"']").prop("checked",true);
    });
  }

  // 绑定卡片事件
  function bindEvent(li) {
    var id = $(li).data("id");
    $(li).click(function(){
      showDetail(id);
    });

    $("button.confirm", li).click(function(e){
      e.stopPropagation();
      confirmAppointment(id);
    });

    $("button.cancel", li).click(function(e){
      e.stopPropagation();
      cancelAppointment(id);
    });
  }

  // 取消预约
  function cancelAppointment(id) {
    var name = $("#card_"+id+" .item-name").text();
    var business = $("#card_"+id+" .item-business").text();
    var datetime = $("#card_"+id+" .card-header .item-title").text();
    var content = "<div style='text-align:left;margin-bottom:-0.5rem'>玩家：" 
          + name + "<br/>主题：" +  business + "<br/>时间：" + datetime + "</div>";
    $.confirm(content, "您确定取消预约吗？", function (value) {
      $.showIndicator();
      $.post("/appointments/" + id + "/cancel", function(detail){
        refreshDetail(detail);
        $.hideIndicator();
        $.toast("预约已取消");
      });
    });
  }

  // 确认到场
  function confirmAppointment(id) {
    var name = $("#card_"+id+" .item-name").text();
    var business = $("#card_"+id+" .item-business").text();
    var content = "<div style='text-align:left;margin-bottom:-0.5rem'>玩家：" 
          + name + "<br/>主题：" +  business + "<br/>到场时间：</div>";
    $.prompt(content,"确认玩家到场", function (value) {
        var date = moment(value).valueOf();
        if(date>0) {
          apiComfirmAppointment(id, date);
        } else {
          $.toast("请输入正确的到场时间！");
        }
      }
    );
    var now = moment();
    $("div.modal-inner input.modal-text-input").val(now.format("YYYY-MM-DD HH:mm"));
  }

  /* 确认到场 */
  function apiComfirmAppointment(id, date) {
    $.showIndicator();
    $.ajax({
      type : "POST",
      url : "/appointments/" + id + "/confirm",
      data : {"datetime":date},
      success : function(list){
        $.hideIndicator();
        $.confirm('<div style="text-align:left;">根据玩家预约主题自动生成 <b class="color-primary">'+list.length+'</b> 条接待信息，是否转到接待页面？</div>',
          "确认到场成功", function () {
          window.location.href="orders.html";
        }, function(){
          $.showIndicator(); // 不跳转则刷新当前信息
          $.get("/appointments/"+id, function(detail){
            refreshDetail(detail);
            $.hideIndicator();
          }); 
        });
      }
    });
  }

  /* 加载预约列表 */
  function apiGetAppointments(start, end, timespan, business, callback) {
    if(LOADING) return;
    LOADING = true;
    $.ajax({
      url:"/appointments",
      traditional: true,
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

  /* 加载门店的主题信息 */
  function loadBusinesses() {
    $.get("/businesses?company=" + COMPANY.id, function(list){
      $("#list_business").html(tmpl("tmpl_business", list));

      $("#list_business input[type='checkbox'],#item_date,#item_time").change(function(){
        // 绑定单选事件
        var id = $(this).attr("id");
        if(id != "item_date" && id != "item_time") {
          $("#list_business input[type='checkbox']").each(function(){
            if(id != $(this).attr("id")) {
              $(this).prop("checked",false);
            }
          });
        }

        var datetime = moment($("#item_date").val() + " " + $("#item_time").val().substring(0.5)).valueOf();
        var ids = [];
        $("#list_business input:checked").each(function(){
          ids.push($(this).val());
        });
        if(datetime && ids.length > 0) {
          // 加载相关预约信息
          loadRelate(datetime, ids);
        } else {
          $("#list_relate").html(tmpl("tmpl_relate", []));
        }
      });
    });
  }

  function loadRelate(datetime, ids) {
    var date = moment(datetime).format("YYYY-MM-DD");
    var dateTicks = moment(date).valueOf();
    apiGetAppointments(dateTicks, dateTicks, null, ids, function(list){
      $("#list_relate").html(tmpl("tmpl_relate", list));
    });
  }

  function loadSources() {
    $("#list_source").html(tmpl("tmpl_source", SOURCES));
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

    $("#btn_refresh").click(function(){ refresh(); });
    $("#btn_create").click(function(){ showDetail(); });
    $("#btn_back").click(function(){ $.router.back("#page_list"); });
    $("#btn_save").click(function(){ saveDetail(); });
    $("#btn_more").click(function(){ loadMore(); });

    loadBusinesses();
    loadSources();

    // 下拉刷新
    $(document).on('refresh', '.pull-to-refresh-content',function(e) {
      refresh(true);
    });

    $(document).on("pageInit", function() {

      // 选择时间
      $("#item_time").picker({
        toolbarTemplate: '<header class="bar bar-nav">\
        <button class="button button-link pull-right close-picker">确定</button>\
        <h1 class="title">选择时间</h1>\
        </header>',
        cols: [{
          textAlign: 'center',
          values: window.TIMES
        }]
      });
    }); 
    $.init();
    
    refresh();
  });

})(jQuery);