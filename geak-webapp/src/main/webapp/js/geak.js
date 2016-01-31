(function($){
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
      $.get("/appointments/"+id, function(detail){
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
    apiGetAppointments(TODAY, null, 1, function(list){
      if(list.length == 0) {
        // 展示空提示
        $("#list").html('<li id="card_empty" class="card"><div class="card-header">'
              +  '<label class="color-danger">今日暂时没有任何预约</label>'
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
      var datetime = detail.datetime;
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
        url : "/appointments",
        data : JSON.stringify(detail),
        contentType : "application/json",
        success : function(data){
          refreshDetail(data);
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
      "customerCount": parseInt($("#item_customer_count").val()),
      "datetime": new Date($("#item_datetime").val()).getTime(),
      "id": $("#item_id").val(),
      "state": $("#item_state").val(),
      "company": COMPANY
    };
    $("#list_business input:checked").each(function(){
      detail.businesses.push({"id":$(this).val(), "alias":$(this).data("alias")});
    });

    if(detail.businesses.length == 0) {
      $.toast("请选择预约的主题！");
      return false;
    } else if(!detail.datetime){
      $.toast("请输入正确的预约时间！");
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

  function bindDetail(detail) {
    var a = detail ? detail :  {
      "businesses": [],
      "customer": { "name": "", "sex": "M", "telephone": "" },
      "customerCount": 5,
      "datetime": new Date().getTime() + 60*60*1000,
      "id": 0,
      "state": "NEW"
    };
    $("#item_id").val(a.id);
    $("#item_state").val(a.state);
    $("#item_customer_name").val(a.customer.name);
    $("#item_customer_sex option[value='"+a.customer.sex+"']").attr("selected", "selected");
    $("#item_customer_count").val(a.customerCount);
    $("#item_customer_tele").val(a.customer.telephone);
    $("#list_business input[type='checkbox']").prop("checked",false);
    $.each(a.businesses, function(i,item) {
      $("#_b_"+item.id).prop("checked",true);
    });
    $("#item_datetime").val(moment(a.datetime).format("YYYY-MM-DD HH:mm"));
    $("#btn_save").hide();
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

  // 绑定卡片事件
  function bindEvent(li) {
    var id = $(li).data("id");
    $(li).click(function(){
      showDetail(id);
    });

    $("button", li).click(function(e){
      e.stopPropagation();
      confirmAppointment(id);
    });
  }

  // 确认到场
  function confirmAppointment(id) {
    var name = $("#card_"+id+" .item-name").text();
    var business = $("#card_"+id+" .item-business").text();
    var content = "<div style='text-align:left;margin-bottom:-0.5rem'>玩家：" 
          + name + "<br/>主题：" +  business + "<br/>到场时间：</div>";
    $.prompt(content,"确认玩家到场", function (value) {
        var date = new Date(value).getTime();
        if(date) {
          apiComfirmAppointment(id, date);
        } else {
          $.toast("请输入正确的到场时间！");
        }
      }
    );
    var now = moment(new Date());
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
  function apiGetAppointments(datetime, business, page, callback) {
    if(LOADING) return;
    LOADING = true;
    $.ajax({
      url:"/appointments",
      traditional: true,
      data:{
        "company" : COMPANY.id,
        "page" : page,
        "datetime" : datetime,
        "business" : business
      },
      dataType:"json", 
      success:function(list){
        if(!business) {
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
        } else {
          callback(list);
        }
        LOADING = false;
      }
    });
  }

  /* 加载门店的主题信息 */
  function loadBusinesses() {
    $.get("/businesses?company=" + COMPANY.id, function(list){
      $("#list_business").html(tmpl("tmpl_business", list));

      $("#list_business input[type='checkbox'],#item_datetime").change(function(){
        var datetime = parseInt(new Date($("#item_datetime").val()).getTime());
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
    apiGetAppointments(datetime, ids.join(","), 0, function(list){
      $("#list_relate").html(tmpl("tmpl_relate", list));
    });
  }

  function loadMore() {
    if (LOWER_LOADING) return;
    LOWER_LOADING = true;
    $("#btn_more").hide();
    $('.infinite-scroll-preloader .preloader').show();
    apiGetAppointments(LOWER_DATE, null, -1, function(list){
      if(list.length == 0) {
        $.detachInfiniteScroll($('.infinite-scroll'));
        $('.infinite-scroll-preloader .preloader').hide();
        $("#btn_more").show();
        $.toast("已经没有更早的预约数据。");
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
    refresh();

    loadBusinesses();

    // 下拉刷新
    $(document).on('refresh', '.pull-to-refresh-content',function(e) {
      if (UPPER_LOADING) return;
      UPPER_LOADING = true;
      apiGetAppointments(UPPER_DATE, null, 1, function(list){
        if(list.length == 0) {
          $.toast("暂无最新的预约数据。");
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

    $("#item_datetime").datetimePicker({
      toolbarTemplate: '<header class="bar bar-nav">\
      <button class="button button-link pull-right close-picker">确定</button>\
      <h1 class="title">选择日期和时间</h1>\
      </header>'
    });
  });

})(jQuery);