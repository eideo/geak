
(function($){

  // 分别表示前后数据的当前页码
  var aPrevPage = aNextPage = 0;
  var aDatePivot = new Date().getTime() - 24*60*60*1000;  // 24小时之前

  function showDetail(id) {
    bindDetail();
    $.popup(".popup-detail");
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
    aDatePivot = new Date().getTime() - 24*60*60*1000;
    apiGetAppointments(aDatePivot, null, 1, function(list){
      if(list.length == 0) {
        // 展示空提示
        $("#list").html('<li id="card_empty" class="card"><div class="card-header">'
              +  '<label class="color-danger pull-left">暂时没有任何数据</label>'
              +  '<button class="button button-fill pull-right" onclick="$(\'#btn_refresh\').click();">'
              +    '<i class="icon icon-refresh"></i> 刷新</label>'
              + '</div>'
            + '</li>');
      } else {
        $("#list").html(tmpl("tmpl_card", list));
        bindEvent();
      }
      $.hideIndicator();
    });
  }

  function saveDetail() {
    var detail = parseDetail();
    if(detail) {
      $.showIndicator();
      $.ajax({
        type : "POST",
        url : "/appointments/",
        data : JSON.stringify(detail),
        contentType : "application/json",
        success : function(data){
          // TODO bind data
          $.hideIndicator();
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
      detail.businesses.push({"id":$(this).val()});
    });

    if(detail.businesses.length == 0) {
      alert("请选择预约主题！");
      return false;
    } else if(!detail.datetime){
      alert("请输入正确的预约时间！");
      return false;
    } else if(isNullOrEmpty(detail.customer.name)){
      alert("请输入玩家姓名！");
      return false;
    } else if(!(detail.customerCount>0)){
      alert("请输入预约人数！");
      return false;
    } else if(isNullOrEmpty(detail.customer.telephone)){
      alert("请输入联系方式！");
      return false;
    }

    return detail;
  }

  function bindDetail(detail) {
    var a = detail ? detail :  {
      "businesses": [],
      "customer": { "name": "", "sex": "M", "telephone": "" },
      "customerCount": 0,
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
      $("#item_cancelled_datetime").parents(".item-content").hide();
      $("#item_confirmed_datetime").parents(".item-content").hide();
    } else if(a.state == "CONFIRMED") {
      $("#item_confirmed_datetime").val(moment(a.confirmedDatetime).format("YYYY-MM-DD HH:mm"));
      $("#item_cancelled_datetime").parents(".item-content").hide();
      $("#item_confirmed_datetime").parents(".item-content").show();
    } else if(a.state == "CANCELLED") {
      $("#item_cancelled_datetime").val(moment(a.cancelledDatetime).format("YYYY-MM-DD HH:mm"));
      $("#item_cancelled_datetime").parents(".item-content").show();
      $("#item_confirmed_datetime").parents(".item-content").hide();
    }
  }

  // 绑定卡片事件
  function bindEvent() {
    $("#list>li").each(function(){
      var id = $(this).data("id");
      $(this).click(function(){
        showDetail(id);
      });

      $("button", this).click(function(e){
        e.stopPropagation();
        confirmAppointment(id);
      });

    });
  }

  // 确认到场
  function confirmAppointment(id) {
    $.prompt('请确认玩家的到场时间', function (value) {
        var date = moment(value);
        if(date.isValid()) {
          // TODO
          $.alert("确认到场" + date)
        } else {
          $.toast("请输入正确的到场时间！");
        }
      }
    );
    var now = moment(new Date());
    $("div.modal-inner input.modal-text-input").val(now.format("YYYY-MM-DD HH:mm"));
  }


  /* 加载预约列表 */
  function apiGetAppointments(datetime, business, page, callback) {
    $.ajax({
      url:"/appointments",
      data:{
        "company" : COMPANY.id,
        "page" : page,
        "datetime" : datetime,
        "business" : business
      },
      dataType:"json", 
      success:callback
    });
  }

  /* 加载门店的主题信息 */
  function loadBusinesses() {
    $.get("/businesses?company=" + COMPANY.id, function(list){
      $("#list_business").html(tmpl("tmpl_business", list));
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
    $("#btn_refresh").click(function(){ refresh(); });
    $("#btn_create").click(function(){ showDetail(); });
    $("#btn_back").click(function(){ $.closeModal(".popup-detail"); });
    $("#btn_save").click(function(){ saveDetail(); });
    refresh();

    loadBusinesses();

    $.init();

    // 下拉刷新
    $(document).on('refresh', '.pull-to-refresh-content',function(e) {
        setTimeout(function() {
            $.toast("OK");
            $.pullToRefreshDone('.pull-to-refresh-content');
        }, 2000);
    });

    // 向下滚动加载
    var loading = false;
    $(document).on('infinite', '.infinite-scroll',function() {

        // 如果正在加载，则退出
        if (loading) return;
        // 设置flag
        loading = true;

        setTimeout(function() {
            loading = false;
            console.log("OK");
        }, 1000);
    });
  });

})(jQuery);