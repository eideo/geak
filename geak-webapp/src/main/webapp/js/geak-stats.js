(function($){
  var COMPANIES_SOURCE = [{"id":1, "name":"大南门店" },{"id":2, "name":"体育路店" },{"id":3, "name":"食品街店" },
                   {"id":4, "name":"柳巷店" },{"id":5, "name":"长风店" },{"id":6, "name":"千峰店" }];
  
  
  function loadStatus() {
    var start = moment($("#search_begin1").val()).valueOf();
    var end = moment($("#search_end1").val()).valueOf();
    $.showIndicator();
    $.get("/stats/status?start=" + start + "&end=" + end, function(list){

      var result = COMPANIES_SOURCE;
      $.each(result, function(i,status){
        status["companyId"] = status["id"];
        status["companyName"] = status["name"];
        status["totalIncome"] = status["count1"] = status["count2"] = status["count3"] = 0;
        status["customer1"] = status["customer2"] = status["customer3"] = 0;
      });

      $.each(list, function(i,item){
        var index = item["companyId"] - 1;
        if(item.state == "EXITED") {
          result[index].count1 += item.totalCount;
          result[index].customer1 += item.totalCustomer;
        } else if(item.state == "APPOINTMENT") {
          result[index].count3 += item.totalCount;
          result[index].customer3 += item.totalCustomer;
        } else {
          result[index].count2 += item.totalCount;
          result[index].customer2 += item.totalCustomer;
        }
        result[index].totalIncome += item.totalIncome;
      });

      $("#card_status").html(tmpl("tmpl_status", result));
      $.hideIndicator();
    });
  }

  function loadRevenue() {
    var start = moment($("#search_begin2").val()).valueOf();
    var end = moment($("#search_end2").val()).valueOf();
    $.showIndicator();
    $.get("/stats/revenue?start=" + start + "&end=" + end, function(list){

      var result = COMPANIES_SOURCE;
      $.each(result, function(i,revenue){
        revenue["companyId"] = revenue["id"];
        revenue["companyName"] = revenue["name"];
        revenue["totalCount"] = revenue["totalIncome"] = revenue["totalCustomer"] = 0
      });

      $.each(list, function(i,item){
        var index = item["companyId"] - 1;
        result[index]["totalCount"] += item.totalCount;
        result[index]["totalIncome"] += item.totalIncome;
        result[index]["totalCustomer"] += item.totalCustomer;
      });

      $("#card_revenue").html(tmpl("tmpl_revenue", result));
      $.hideIndicator();
    });
  }
  
  function loadUsers() {
	  $.showIndicator();
    $.get("/users", function(list){
      $("#user_list").html(tmpl("tmpl_user", list));
      // bindevent
      $("#user_list select").change(function(e){
        var userId = $(this).data("userId");
        var companyId = $("option:selected", this).val();
        $.ajax({
          type : "POST",
          url:"/users/" + userId + "/transfer",
          data:{"companyId" : companyId},
          success : function(detail){
            $.toast("员工门店调整成功");
          }
        });
      });
      $.hideIndicator();
    });
  }
  
  
  $(function(){
	  var now = moment().format("YYYY-MM-DD"); 
    $("input[data-toggle='date']").val(now);	
	  
    // 实时状态
    loadStatus();
    $("#btn_search1").click(function(){ loadStatus(); });

    // 利润统计
    loadRevenue();
    $("#btn_search2").click(function(){ loadRevenue(); });
    
    // 加载人员信息
    loadUsers();

	  $.init();
  });
  
})(jQuery);

