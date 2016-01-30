<!DOCTYPE html>
<html>
  <head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="initial-scale=1, maximum-scale=1">
    <meta name="apple-mobile-web-app-capable" content="yes">
    <meta name="apple-mobile-web-app-status-bar-style" content="black">

    <title>极客密室</title>

    <link rel="shortcut icon" href="/favicon.ico">
    <link rel="stylesheet" href="css/light7.min.css">
    <link rel="stylesheet" href="css/geak.css">
    <style> 
      #list_payment input, #list_promotion input{ display: inline; width:45%;} 
      .row .col-33 { width: 30%; margin-left: 3%; }
      .row .col-29 { width: 26%; margin-left: 3%; }
      .row .col-38 { width: 35%; margin-left: 3%; }
    </style>
    <script type="text/javascript">
      var USER = {"id":"1", "name":"麻文强" };
      var COMPANY = {"id":"1", "name":"大南门店" };
    </script>
  </head>
  <body class="theme-green">
    <!-- page 容器 -->
    <div id="page_list" class="page page-current">
      <!-- 标题栏 -->
      <header class="bar bar-nav">
        <button id="btn_refresh" class="button button-link button-nav pull-left">
          <span class="icon icon-refresh"></span> 刷新
        </button>
        <button id="btn_create" class="button button-link button-nav pull-right">
          新建 <span class="icon icon-right"></span>
        </button>
        <h1 class="title">近期接待</h1>
      </header>

      <!-- 工具栏 -->
      <nav class="bar bar-tab">
        <a class="external tab-item" href="appointments.html">
          <span class="icon icon-edit"></span>
          <span class="tab-label">预约</span>
        </a>
        <a class="external tab-item active" href="#">
          <span class="icon icon-emoji"></span>
          <span class="tab-label">接待</span>
        </a>
      </nav>

      <!-- 这里是页面内容区 -->
      <div class="content pull-to-refresh-content infinite-scroll">
        <div class="pull-to-refresh-layer">
          <div class="preloader"></div>
          <div class="pull-to-refresh-arrow"></div>
        </div><!-- /.pull-to-refresh-layer -->
        <div class="list-block cards-list">
          <ul id="list"></ul>
        </div><!-- /.cards-list -->
        <div class="infinite-scroll-preloader">
          <div class="preloader" style="display:none;"></div>
          <a id="btn_more" class="button button-round">加载更多过往接待数据...</a>
        </div><!-- /.infinite-scroll-preloader -->
      </div><!-- /.content -->
    </div><!-- /.page -->

    <div id="page_detail" class="page">
      <header class="bar bar-nav">
        <button id="btn_back" class="button button-link button-nav pull-left">
          <span class="icon icon-left"></span> 返回
        </button>
        <button id="btn_save" class="button button-primary button-nav pull-right"> 保存 </button>
        <h1 class="title">接待详情</h1>
      </header>
      <div class="content">
        <input id="item_id" type="hidden" />
        <input id="item_state" type="hidden" />
        <div class="content-block-title color-success">接待主题及时间</div>
        <div class="list-block">
          <ul>
            <li id="list_business"></li>
            <li>
              <div class="item-content">
                <div class="item-inner">
                  <div class="item-title label color-primary">入场时间</div>
                  <div class="item-input">
                    <input id="item_entrance_datetime" type="text" />
                  </div>
                </div>
              </div>
              <div class="item-content">
                <div class="item-inner">
                  <div class="item-title label color-warning">离场时间</div>
                  <div class="item-input">
                    <input id="item_exit_datetime" type="text" />
                  </div>
                </div>
              </div>
              <div class="item-content">
                <div class="item-inner">
                  <div class="item-title label">预约时间</div>
                  <div class="item-input">
                    <input id="item_datetime" type="text" readonly="readonly"/>
                  </div>
                </div>
              </div>
            </li>
          </ul>
        </div><!-- /.list-block -->
        <div class="content-block-title color-success">支付信息</div>
        <div class="list-block">
          <ul>
            <li id="list_payment"></li>
          </ul>
        </div><!-- /.list-block -->
        <div class="content-block-title color-success">接待玩家信息</div>
        <div class="list-block">
          <ul>
            <li>
              <div class="item-content">
                <div class="item-inner">
                  <div class="item-title label">玩家姓名</div>
                  <div class="item-input">
                    <input id="item_customer_name" type="text"  />
                  </div>
                  <div class="item-input">
                    <select id="item_customer_sex">
                      <option value="M">先生</option>
                      <option value="F">女士</option>
                    </select>
                  </div>
                </div>
              </div>
            </li>
            <li>
              <div class="item-content">
                <div class="item-inner">
                  <div class="item-title label">接待人数</div>
                  <div class="item-input">
                    <input id="item_customer_count" type="text" />
                  </div>
                </div>
              </div>
            </li>
            <li>
              <div class="item-content">
                <div class="item-inner">
                  <div class="item-title label">联系方式</div>
                  <div class="item-input">
                    <input id="item_customer_tele" type="text"/>
                  </div>
                </div>
              </div>
            </li>
          </ul>
        </div><!-- /.list-block -->
        <div class="content-block-title color-success">优惠信息</div>
        <div class="list-block">
          <ul>
            <li id="list_promotion"></li>
          </ul>
        </div><!-- /.list-block -->
        <div class="content-block-title color-success">来源渠道</div>
        <div class="list-block">
          <ul>
            <li id="list_source"></li>
          </ul>
        </div><!-- /.list-block -->
      </div><!-- /.content-block -->
    </div><!-- /.page page-detail -->
    
    <script type="text/x-tmpl" id="tmpl_card">
      {% for (var i=0; i<o.length; i++) { %}
        {% include('tmpl_card_item', o[i]); %}
      {% } %}
    </script>
    <script type="text/x-tmpl" id="tmpl_card_item">
      <li id="card_{%= o.id %}" class="card" data-id="{%= o.id %}" data-datetime="{%= o.datetime %}">
        <div class="card-header">
          <label class="pull-left">{%= moment(new Date(o.datetime)).format("MM月DD日 HH:mm") %}</label>
          <label class="pull-right">{%= o.businesses[0].alias %}
            {% for (var j=1; j<o.businesses.length; j++) { %}
              | {%= o.businesses[j].alias %}
            {% } %}
          </label>
        </div>
        <div class="card-content">
          <div class="card-content-inner row">
            <div class="col-80">
              {%= o.customer.name %}  
              {% if (o.customer.sex == 'M')print('先生'); else print('女士'); %}
              ({%= o.customer.telephone %}) 
            </div>
            <div class="col-20" style="text-align:right;"><b>{%= o.customerCount %}</b> 人</div>
          </div>
        </div>
        <div class="card-footer">
          {% if (o.state == 'NEW') { %}
              <label class="color-warning pull-left">待确认</label>
              <button class="button button-warning button-fill pull-right" data-id="{%= o.id %}">
                确认到场</button>
          {% } %}
          {% if (o.state == 'CONFIRMED') { %}
              <label class="color-success pull-left">已到场：
                {%= moment(new Date(o.confirmedDatetime)).format("MM月DD日 HH:mm") %}
              </label>
          {% } %}
          {% if (o.state == 'CANCELLED') { %}
              <label class="color-danger pull-left">已取消：
                {%= moment(new Date(o.cancelledDatetime)).format("MM月DD日 HH:mm") %}
              </label>
          {% } %}
        </div>
      </li>
    </script>
    <script type="text/x-tmpl" id="tmpl_business">
      {% for (var i=0; i<o.length; i++) { %}
        {% if (i%3 == 0) { %}
          {% if (i > 0) { %}
              </div>
            </div>
          {% } %} 
            <div class="item-content">
              <div class="item-inner row">
        {% } %} 
                <div class="col-33">
                  <label>{%= o[i].alias %}</label>
                  <label class="label-switch">                    
                    <input id="_b_{%= o[i].id %}" type="checkbox" 
                      value="{%= o[i].id %}" data-alias="{%= o[i].alias %}" />
                    <div class="checkbox"></div>
                  </label>
                </div>
      {% } %}
              </div>
            </div>
    </script>
    <script type="text/x-tmpl" id="tmpl_source">
      {% var _w = [32,30,38]; for (var i=0; i<o.length; i++) { %}
        {% if (i%3 == 0) { %}
          {% if (i > 0) { %}
              </div>
            </div>
          {% } %} 
            <div class="item-content">
              <div class="item-inner row">
        {% } %} 
                <div class="col-{%= _w[i%3] %}">
                  <label>{%= o[i] %}</label>
                  <label class="label-switch">                    
                    <input id="_s_{%= i %}" type="checkbox" value="{%= o[i] %}" />
                    <div class="checkbox"></div>
                  </label>
                </div>
      {% } %}
              </div>
            </div>
    </script>
    <script type="text/x-tmpl" id="tmpl_payment">
      {% for (var i=0; i<o.length; i++) { %}
        {% if (i%3 == 0) { %}
          {% if (i > 0) { %}
              </div>
            </div>
          {% } %} 
            <div class="item-content">
              <div class="item-inner row">
        {% } %} 
                <div class="col-33">
                  <label>{%= o[i].name %}</label>
                  <input id="_m_{%= o[i].id %}" type="text" value="0" />
                </div>
      {% } %}
              </div>
            </div>
    </script>
    <script type="text/x-tmpl" id="tmpl_promotion">
      {% for (var i=0; i<o.length; i++) { %}
        {% if (i%3 == 0) { %}
          {% if (i > 0) { %}
              </div>
            </div>
          {% } %} 
            <div class="item-content">
              <div class="item-inner row">
        {% } %} 
                <div class="col-33">
                  <label>{%= o[i].name %}</label>
                  <input id="_p_{%= o[i].id %}" type="text" value="0" />
                </div>
      {% } %}
              </div>
            </div>
    </script>

    <!-- 你的html代码 -->
    <script type='text/javascript' src='js/jquery.min.js'></script>
    <script type='text/javascript' src='js/light7.min.js'></script>
    <script type='text/javascript' src='js/moment.min.js'></script>
    <script type='text/javascript' src='js/tmpl.min.js'></script>
    <script type='text/javascript' src='js/json2.min.js'></script>
    <script type='text/javascript' src='js/geak-order.js'></script>
  </body>
</html>







