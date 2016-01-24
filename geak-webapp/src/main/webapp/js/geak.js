
(function($){
  var pt = PageTransitions;

  // 分别表示前后数据的当前页码
  var aPrevPage = aNextPage = 0;
  var aDatePivot = new Date().getTime() - 24*60*60*1000;  // 24小时之前

  //刷新预约
  function refreshAppointments() {
    aPrevPage = aNextPage = 0;
    aDatePivot = new Date().getTime() - 24*60*60*1000;
    $("#mask_a").show();
    $("#mask_a h3").hide();
    $("#mask_a h4").show();
    apiGetAppointments(aDatePivot, null, null, function(list){
      if(list.length == 0) {
        $("#mask_a").show();
        $("#mask_a h4").hide();
        $("#mask_a h3").show();
      } else {
        $("#alist").html("");
        appendAppointments(list);
        $("#mask_a").hide();
      }
    });
  }

  // 确认到场
  function confirmAppointment(id) {

  }

  function appendAppointments(list) {
    $.each(list, function(index, item){
      var bs = item.businesses[0].alias;
      for(var i = 1; i < item.businesses.length; i++) {
        bs += " | " + item.businesses[i].alias
      } 
      var action = '';
      if(item.state == "NEW") {
        action = '<button class="btn btn-xs btn-primary" onclick="confirmAppointment('
            +item.id+')">确认到场</button>';
      } else if(item.state == "CONFIRMED") {
        action = '<span class="label label-success">已确认</span>';
      } else if(item.state == "CANCELLED") {
        action = '<span class="label label-danger">已取消</span>';
      } 
      var str = '<li class="row" aid="'+item.id+'">'
        + '<span class="col-xs-4 text-center">'+ moment(item.datetime).format("MM月DD日 HH:mm") + '</span>'
        + '<span class="col-xs-2 text-right">'+ item.customerCount +'</span>'
        + '<span class="col-xs-4 text-center">' + bs + '</span>'
        + '<span class="col-xs-2 text-right">' + action + '</span>'
        +'</li>';
      $("#alist").append(str);
    });
  }



  $("#btn_go_a, #btn_back_a").on('click', function(){ pt.gotoPage( 0 ); });
  $("#btn_go_o").on('click', function(){ alert($(window).width() + "," +$(window).height());return; pt.gotoPage( 1 ); });
  $("#btn_new_a").on('click', function(){ pt.gotoPage( 2 ); });
  $("#btn_new_o").on('click', function(){ pt.gotoPage( 3 ); });
  $("#btn_refresh_a,#link_refresh_a").on('click', function(){ refreshAppointments(); });
  $("#btn_refresh_o").on('click', function(){ console.log("刷新接待"); });

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

  function hideMask() {$("#mask").hide();}
  function showMask(title, text) {
    $("#mask span.title").html(title?title:"页面加载中...");
    $("#mask span.text").html(text?text:"请稍后...");
    $("#mask").show();
  }

  $(hideMask);
})(jQuery);