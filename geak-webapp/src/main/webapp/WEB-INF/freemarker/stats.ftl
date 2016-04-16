<!DOCTYPE html>
<html>
  <head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="initial-scale=1, maximum-scale=1">
    <meta name="apple-mobile-web-app-capable" content="yes">
    <meta name="apple-mobile-web-app-status-bar-style" content="black">

    <title>极客密室 | ${user.company.name} | ${user.name}</title>

    <link rel="shortcut icon" href="/favicon.ico">
    <link rel="stylesheet" href="css/light7.min.css">
    <link rel="stylesheet" href="css/geak.css">
    <script type="text/javascript">
      var USER = {"id":"${user.id}", "name":"${user.name}" };
      var COMPANY = {"id":"${user.company.id}", "name":"${user.company.name}" };
      var COMPANIES = [];
      <#if user.companies?? && user.companies?size &gt; 0>
        <#list user.companies as company>
        COMPANIES.push({"id":"${company.id}", "name":"${company.name}" });
        </#list>
      </#if>
    </script>
  </head>
  <body>
    <!-- page 容器 -->
    <div id="page_list" class="page page-current">
      <!-- 标题栏 -->
      <header class="bar bar-nav bar-standard">
        <h1 class="title">统计查询</h1>
      </header>
      <!-- 工具栏 -->
      <nav class="bar bar-tab">
        <a class="external tab-item" href="appointments.html?page=appointments">
          <span class="icon icon-edit"></span>
          <span class="tab-label">预约</span>
        </a>
        <a class="external tab-item" href="orders.html?page=orders">
          <span class="icon icon-emoji"></span>
          <span class="tab-label">接待</span>
        </a>
        <a class="external tab-item active" href="#">
          <span class="icon icon-app"></span>
          <span class="tab-label">统计</span>
        </a>
      </nav>

      <!-- 这里是页面内容区 -->
      <div class="content">
        <div class="buttons-tab">
          <a href="#tab1" class="tab-link active button">实时统计</a>
          <a href="#tab2" class="tab-link button">历史统计</a>
        </div>
        <div class="tabs">
          <div id="tab1" class="tab active">
            <div class="searchbar row no-gutter" style="padding-left: 3%">
              <div class="search-input col-40">
                <input id='search_begin1' type="text" data-toggle="date" readonly="readonly" placeholder='起始时间'/>
              </div>
              <div class="col-5">&nbsp;~&nbsp;</div>
              <div class="search-input col-40">
                <input id='search_end1' type="text" data-toggle="date" readonly="readonly" placeholder='结束时间'/>
              </div>
              <a id="btn_search1" class="button button-fill button-primary col-10"><span class="icon icon-search"></span></a>
            </div> <!-- /.searchbar -->      
            <div id="card_status"></div>   
          </div><!-- /.tab1 -->
          <div id="tab2" class="tab">
            <div class="searchbar row no-gutter" style="padding-left: 3%">
              <div class="search-input col-40">
                <input id='search_begin2' type="text" data-toggle="date" readonly="readonly" placeholder='起始时间'/>
              </div>
              <div class="col-5">&nbsp;~&nbsp;</div>
              <div class="search-input col-40">
                <input id='search_end2' type="text" data-toggle="date" readonly="readonly" placeholder='结束时间'/>
              </div>
              <a id="btn_search2" class="button button-fill button-primary col-10"><span class="icon icon-search"></span></a>
            </div> <!-- /.searchbar -->      
            <div id="card_revenue"></div> 
          </div><!-- /.tab2 -->
        </div><!-- /.tabs -->
      </div><!-- /.content -->
    </div><!-- /.page -->



    <script type="text/x-tmpl" id="tmpl_status">
      {% for (var i=0; i<o.length; i++) { %}
        <div class="card">
          <div class="card-header"><a href="appointments.html?company={%= o[i].companyId %}" class="item-title list-button item-link external">
            {%= o[i].companyId %}.&nbsp;{%= o[i].companyName %}
          </a>
          <label class="item-after">收入：￥{%= o[i].totalIncome %}</label></div>
          <div class="card-footer">
            <a href="#" class="link">人数统计</a>
            <a href="#" class="link color-success">已接待：{%= o[i].customer1 %}</a>
            <a href="#" class="link color-danger" >正接待：{%= o[i].customer2 %}</a>
            <a href="#" class="link color-warning">已预约：{%= o[i].customer3 %}</a>
          </div>
          <div class="card-footer">
            <a href="#" class="link">场次统计</a>
            <a href="#" class="link color-success">已接待：{%= o[i].count1 %}</a>
            <a href="#" class="link color-danger" >正接待：{%= o[i].count2 %}</a>
            <a href="#" class="link color-warning">已预约：{%= o[i].count3 %}</a>
          </div>
        </div>
      {% } %}
    </script>
    <script type="text/x-tmpl" id="tmpl_revenue">
      {% for (var i=0; i<o.length; i++) { %}
        <div class="card">
          <div class="card-header"><a href="appointments.html?company={%= o[i].companyId %}" class="list-button item-link external">
            {%= o[i].companyId %}.&nbsp;{%= o[i].companyName %}
          </a>
          </div>
          <div class="card-content">
            <div class="card-content-inner">
              <div class="row no-gutter">
                <div class="col-33"><span class="color-success">场数：</span><b class="bg-success">{%= o[i].totalCount %}</b></div>
                <div class="col-33"><span class="color-primary">人数：</span><b class="bg-primary">{%= o[i].totalCustomer %}</b></div>
                <div class="col-33"><span class="color-warning">收入：</span><b class="bg-warning">￥{%= o[i].totalIncome %}</b></div>
              </div>
            </div>
          </div>
        </div>
      {% } %}
    </script>

    <!-- 你的html代码 -->
    <script type='text/javascript' src='js/jquery.min.js'></script>
    <script type='text/javascript' src='js/light7.min.js'></script>
    <script type='text/javascript' src='js/moment.min.js'></script>
    <script type='text/javascript' src='js/tmpl.min.js'></script>
    <script type='text/javascript' src='js/json2.min.js'></script>
    <script type='text/javascript' src='js/geak-stats.js'></script>
  </body>
</html>







