var today = moment().set({'hour': 0, 'minute': 0, 'second': 0, 'millisecond': 0});
var ticks = today.toDate().getTime();
window.QUERY_PARAM = {
  "start" : ticks,
  "end" : ticks,
  "timespan" : null,
  "business" : null
};
window.TIMES = [
  '09:30~10:30','10:45~11:45','12:00~13:00',
  '13:15~14:15','14:30~15:30','15:45~16:45',
  '17:00~18:00','18:25~19:15','19:30~20:30',
  '20:45~21:45','22:00~23:00','23:00~24:00'
];

(function($){
  var TIMES = window.TIMES;

  var DATES = [
    { "name":"今天", "span":function(){
      var today = moment().set({'hour': 0, 'minute': 0, 'second': 0, 'millisecond': 0});
      var ticks = today.toDate().getTime();
      return [ticks, ticks];
    } },
    { "name":"本周", "span":function(){
      var firstOfWeek = moment().isoWeekday(1).set({'hour':0,'minute':0,'second':0,'millisecond':0});
      var lastOfWeek = moment().isoWeekday(7).set({'hour':0,'minute':0,'second':0,'millisecond':0});
      return [firstOfWeek.toDate().getTime(), lastOfWeek.toDate().getTime()];
    } },
    { "name":"本月", "span":function(){
      var firstOfMonth = moment().set({'date':1,'hour':0,'minute':0,'second':0,'millisecond':0});
      var lastOfMonth = moment().add(1, 'months').date(1).subtract(1,'days').set({'hour':0,'minute':0,'second':0,'millisecond':0});
      return [firstOfMonth.toDate().getTime(), lastOfMonth.toDate().getTime()];
    } }
  ];

  function doSearch() {
    $.closeModal();
    
    // 计算日期
    var dateSpan = DATES[$("#search_date .date-item.active").data("index")];
    $("#txt_search_date").html(dateSpan.name);
    var spans = dateSpan.span();
    window.QUERY_PARAM["start"] = spans[0];
    window.QUERY_PARAM["end"] = spans[1];

    // 计算时间
    window.QUERY_PARAM["timespan"] = null;
    var $timeAll = $("#search_time .time-all");
    if($timeAll.hasClass("active")) {
      $("#txt_search_time").html($timeAll.text());
    } else {
      var $spans = $("#search_time .time-span.active");
      var spanCount = $spans.length;
      if(spanCount == 1) {
        $("#txt_search_time").html($spans.text());
        window.QUERY_PARAM["timespan"] = [$spans.text()];
      } else if(spanCount > 1) {
        $("#txt_search_time").html("<b style='color:red'>"+spanCount+"</b> 段时间");
        window.QUERY_PARAM["timespan"] = [];
        $spans.each(function(){
          window.QUERY_PARAM["timespan"].push($(this).text());
        });
      }
    }

    // 计算主题
    window.QUERY_PARAM["business"] = null;
    var $businessAll = $("#search_business .business-all");
    if($businessAll.hasClass("active")) {
      $("#txt_search_business").html($businessAll.text());
    } else {
      var $businesses = $("#search_business .business-item.active");
      var businessCount = $businesses.length;
      if(businessCount == 1) {
        $("#txt_search_business").html($businesses.text());
        window.QUERY_PARAM["business"] = [$businesses.data("id")];
      } else if(businessCount > 1) {
        $("#txt_search_business").html("<b style='color:red'>"+businessCount+"</b> 个主题");
        window.QUERY_PARAM["business"] = [];
        $businesses.each(function(){
          window.QUERY_PARAM["business"].push($(this).data("id"));
        });
      }
    }

    // 在各自业务代码中定义
    window.QUERY_LIST();
  }

  // begin init
  $(function(){
    if(COMPANIES.length == 0) {
      $("#btn_refresh").show();
      $("#btn_change").hide();
    } else {
      $("#btn_refresh").hide();
      $("#btn_change").show();
    }
	  
	  
    // 初始化时间选择器
    $("#search_time").prepend(tmpl("tmpl_search_time", TIMES));
    $("#search_time .time-span").click(function(){
      $(this).toggleClass("active");
      if($("#search_time .time-span.active").length > 0) {
        $("#search_time .time-all").removeClass("active");
      } else {
        $("#search_time .time-all").addClass("active");
      }
    });
    $("#search_time .time-all").click(function(){
      $(this).addClass("active");
      $("#search_time .time-span").removeClass("active");
      doSearch();
    });
    $("#search_time .time-ok, #search_business .business-ok").click(function(){
      doSearch();
    });

    // 初始化主题选择器
    $.get("/businesses?company=" + COMPANY.id, function(list){
      $("#search_business").prepend(tmpl("tmpl_search_business", list));
      $("#search_business .business-item").click(function(){
        $(this).toggleClass("active");
        if($("#search_business .business-item.active").length > 0) {
          $("#search_business .business-all").removeClass("active");
        } else {
          $("#search_business .business-all").addClass("active");
        }
      });
      $("#search_business .business-all").click(function(){
        $(this).addClass("active");
        $("#search_business .business-item").removeClass("active");
        doSearch();
      });
    });

    // 初始化日期选择器
    $("#search_date").prepend(tmpl("tmpl_search_date", DATES));
    $("#search_date .date-item:first").addClass("active");
    $("#search_date .date-item").click(function(){
      $("#search_date .date-item").removeClass("active");
      $(this).addClass("active");
      doSearch();
    });
  }); // end init
})(jQuery);






