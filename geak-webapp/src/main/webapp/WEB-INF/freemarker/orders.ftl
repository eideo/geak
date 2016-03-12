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
        <button id="btn_refresh" class="button button-link button-nav pull-left">
          <span class="icon icon-refresh"></span> 刷新
        </button>
        <button id="btn_change" class="button button-link button-nav pull-left open-popover" 
            data-popover=".popover-change-company" style="display:none">
          <span class="icon icon-menu"></span> ${user.company.name}
        </button>
        <button id="btn_create" class="button button-link button-nav pull-right">
          新建 <span class="icon icon-right"></span>
        </button>
        <h1 class="title">近期接待</h1>
      </header>
      <nav class="bar bar-header-secondary">
        <div class="buttons-row">
          <button id="btn_search_date" class="button open-popover" data-popover=".popover-search-date">
            <span id="txt_search_date">今天</span> <span class="icon icon-down"></span>
          </button>
          <button id="btn_search_time" class="button open-popover" data-popover=".popover-search-time">
            <span id="txt_search_time">所有时间</span> <span class="icon icon-down"></span>
          </button>
          <button id="btn_search_business" class="button open-popover" data-popover=".popover-search-business">
            <span id="txt_search_business">所有主题</span> <span class="icon icon-down"></span>
          </button>
        </div>
      </nav>

      <!-- 工具栏 -->
      <nav class="bar bar-tab">
        <a class="external tab-item" href="appointments.html?page=appointments">
          <span class="icon icon-edit"></span>
          <span class="tab-label">预约</span>
        </a>
        <a class="external tab-item active" href="#">
          <span class="icon icon-emoji"></span>
          <span class="tab-label">接待</span>
        </a>
      </nav>

      <!-- 这里是页面内容区 -->
      <div class="content pull-to-refresh-content">
        <div class="pull-to-refresh-layer">
          <div class="preloader"></div>
          <div class="pull-to-refresh-arrow"></div>
        </div><!-- /.pull-to-refresh-layer -->

        <div class="content-block-title color-danger">未支付的接待</div>
        <div class="list-block cards-list">
          <ul id="list_new"></ul>
          <ul id="list_new_empty"><li class="card"><div class="card-header">暂无未支付的接待</div></li></ul>
        </div><!-- /.cards-list -->

        <div class="content-block-title color-warning">已支付的接待</div>
        <div class="list-block cards-list">
          <ul id="list_payed"></ul>
          <ul id="list_payed_empty"><li class="card"><div class="card-header">暂无已支付的接待</div></li></ul>
        </div><!-- /.cards-list -->

        <div class="content-block-title color-primary">已入场的接待</div>
        <div class="list-block cards-list">
          <ul id="list_entranced"></ul>
          <ul id="list_entranced_empty"><li class="card"><div class="card-header">暂无已入场的接待</div></li></ul>
        </div><!-- /.cards-list -->

        <div class="content-block-title color-success">已离场的接待</div>
        <div class="list-block cards-list">
          <ul id="list_exited"></ul>
          <ul id="list_exited_empty"><li class="card"><div class="card-header">暂无已离场的接待</div></li></ul>
        </div><!-- /.cards-list -->

        <div class="content-block-title">已取消的接待</div>
        <div class="list-block cards-list">
          <ul id="list_cancelled"></ul>
          <ul id="list_cancelled_empty"><li class="card"><div class="card-header">暂无已取消的接待</div></li></ul>
        </div><!-- /.cards-list -->
      </div><!-- /.content -->
    </div><!-- /.page -->

    <div id="page_detail" class="page">
      <header class="bar bar-nav">
        <button id="btn_back" class="button button-link button-nav pull-left">
          <span class="icon icon-left"></span> 返回
        </button>
        <button id="btn_exit" class="button button-success button-nav pull-right"> 确认离场 </button>
        <button id="btn_extrnace" class="button button-warning button-nav pull-right"> 确认入场 </button>
        <button id="btn_save" class="button button-primary button-nav pull-right"> 保存 </button>
        <button id="btn_cancel" class="button button-danger button-nav pull-right"> 取消接待 </button>
        <h1 class="title">接待详情</h1>
      </header>
      <div class="content">
        <input id="item_id" type="hidden" />
        <input id="item_state" type="hidden" />
        <div class="content-block-title color-success">
          <div class="pull-left">接待主题及时间</div>
          <div class="pull-right">当前状态：<b id="state_name">未支付</b></div>
        </div>
        <div class="list-block">
          <ul id="list_business"></ul>
        </div>
        <div class="list-block">
          <ul>
            <li>
              <div class="item-content">
                <div class="item-inner">
                  <div class="item-title label color-primary">入场时间</div>
                  <div class="item-input">
                    <input id="item_entrance_datetime" type="text" readonly="readonly"/>
                  </div>
                </div>
              </div>
            </li>
            <li>
              <div class="item-content">
                <div class="item-inner">
                  <div class="item-title label color-success">离场时间</div>
                  <div class="item-input">
                    <input id="item_exit_datetime" type="text" readonly="readonly"/>
                  </div>
                </div>
              </div>
            </li>
            <li>
              <div class="item-content">
                <div class="item-inner">
                  <div class="item-title label">到场时间</div>
                  <div class="item-input">
                    <input id="item_datetime" type="text" readonly="readonly"/>
                  </div>
                </div>
              </div>
            </li>
            <li>
              <div class="item-content">
                <div class="item-inner">
                  <div class="item-title label color-danger">取消时间</div>
                  <div class="item-input">
                    <input id="item_cancel_datetime" type="text" readonly="readonly"/>
                  </div>
                </div>
              </div>
            </li>
          </ul>
        </div><!-- /.list-block -->
        <div class="content-block-title color-success">支付总额：￥ <input id="item_total_price" type="text" /></div>
        <div class="list-block">
          <ul id="list_payment"></ul>
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
                      <option value="S">同学</option>
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
          <ul id="list_promotion">
            <li class="row">
              <div class="col-50"> 
                <div class="item-content">
                  <div class="item-inner">
                    <label class="item-title label">打车劵</label>
                    <label class="item-input">
                      <input id="_p_6" type="text" value="" data-id="6" style="text-align:right">
                    </label>
                    <label class="item-after">元</label>
                  </div>
                </div>
              </div>
              <div class="col-50"> 
                <div class="item-content">
                  <div class="item-inner">
                    <label class="item-title label">来源</label>
                    <div class="item-input">
                      <select id="_p_6_note">
                        <option value="" selected="selected">无</option>
                      </select>
                    </div>
                  </div>
                </div>
              </div>
            </li>
            <li>
              <div class="item-content">
                <div class="item-inner">
                  <div class="item-title label">其他优惠</div>
                  <div class="item-input">
                    <input id="item_promotion_note" type="text" />
                  </div>
                </div>
              </div>
            </li>
          </ul>
        </div><!-- /.list-block -->
        <div class="content-block-title color-success">玩家身份</div>
        <div class="list-block">
          <ul id="list_customer_type"></ul>
        </div><!-- /.list-block -->
        <div class="content-block-title color-success">来源渠道</div>
        <div class="list-block">
          <ul id="list_source"></ul>
        </div><!-- /.list-block -->
      </div><!-- /.content-block -->
    </div><!-- /.page page-detail -->
    
    <div class="popover popover-change-company">
      <div class="popover-angle"></div>
      <div class="popover-inner">
        <div class="list-block">
          <ul>
          <#if user.companies?? && user.companies?size &gt; 0>
            <#list user.companies as company>
            <li><a href="orders.html?company=${company.id}" class="list-button item-link external">${company.name}</a></li>
            </#list>
          </#if>
          </ul>
        </div>
      </div>
    </div>

    <div class="popover popover-search-time">
      <div class="popover-angle"></div>
      <div class="popover-inner">
        <div class="list-block">
          <ul id="search_time">
            <li class="row">
              <a href="#" class="col-33 list-button item-link time-cancel close-popover">取消</a>
              <a href="#" class="col-33 list-button item-link time-all active">所有时间</a>
              <a href="#" class="col-33 list-button item-link time-ok">确定</a>
            </li>
          </ul>
        </div>
      </div>
    </div>
    <div class="popover popover-search-business">
      <div class="popover-angle"></div>
      <div class="popover-inner">
        <div class="list-block">
          <ul id="search_business">
            <li><a href="#" class="list-button item-link business-all active">所有主题</a></li>
            <li><a href="#" class="list-button item-link business-ok">确定</a></li>
          </ul>
        </div>
      </div>
    </div>
    <div class="popover popover-search-date">
      <div class="popover-angle"></div>
      <div class="popover-inner">
        <div class="list-block">
          <ul id="search_date"></ul>
        </div>
      </div>
    </div>
    
    <script type="text/x-tmpl" id="tmpl_card">
      {% for (var i=0; i<o.length; i++) { %}
        {% include('tmpl_card_item', o[i]); %}
      {% } %}
    </script>
    <script type="text/x-tmpl" id="tmpl_card_item">
      <li id="card_{%= o.id %}" class="card" data-id="{%= o.id %}" data-datetime="{%= o.createdDatetime %}">
        <div class="card-header">
          <label class="item-title">{%= moment(new Date(o.createdDatetime)).format("MM月DD日 HH:mm") %}</label>
          <label class="item-after item-business">
            {% if(o.business && o.business.alias) { %}
              {%= o.business.alias %}
            {% } else { %}
              <b class="color-danger">未定</b>
            {% } %}
          </label>
        </div>
        <div class="card-content">
          <div class="card-content-inner row">
            <div class="col-80 item-name">
              {%= o.customer.name %}  
              {% if (o.customer.sex == 'M')print('先生'); else if(o.customer.sex == 'F') print('女士'); else print('同学'); %}
              ({%= o.customer.telephone %}) 
            </div>
            <div class="col-20" style="text-align:right;"><b>{%= o.customerCount %}</b> 人</div>
          </div>
        </div>
        <div class="card-footer">
          {% if (o.state == 'NEW') { %}
              <button class="cancel button button-danger button-fill item-title" data-id="{%= o.id %}">
                取消接待</button>
              <button class="pay item-after button button-primary button-fill" data-id="{%= o.id %}">
                确认支付</button>
          {% } %}
          {% if (o.state == 'PAYED') { %}
              <label class="item-title color-warning">等待入场</label>
              <button class="entrance item-after button button-warning button-fill" data-id="{%= o.id %}">
                确认入场</button>
          {% } %}
          {% if (o.state == 'ENTRANCED') { %}
              <label class="item-title color-primary">等待离场，入场时间：
                {%= moment(new Date(o.entranceDatetime)).format("MM月DD日 HH:mm") %}
              </label>
              <button class="exit item-after button button-primary button-fill" data-id="{%= o.id %}">
                确认离场</button>
          {% } %}
          {% if (o.state == 'EXITED') { %}
              <label class="item-title color-success">已离场：
                {%= moment(new Date(o.exitDatetime)).format("MM月DD日 HH:mm") %}
              </label>
          {% } %}
          {% if (o.state == 'CANCELLED') { %}
              <label class="item-title">已取消：
                {%= moment(new Date(o.cancelledDatetime)).format("MM月DD日 HH:mm") %}
              </label>
          {% } %}
        </div>
      </li>
    </script>
    <script type="text/x-tmpl" id="tmpl_business">
      {% for (var i=0; i<o.length; i++) { %}
        {% if (i%2 == 0) { %}
          {% if (i > 0) { %}
        </li>{% } %} 
        <li class="row">
        {% } %}
          <div class="col-50"> 
            <div class="item-content">
              <div class="item-inner">
                <label class="item-title">{%= o[i].alias %}</label>
                <label class="item-after label-switch">                    
                  <input id="_b_{%= o[i].id %}" type="checkbox" value="{%= o[i].id %}" data-alias="{%= o[i].alias %}" />
                  <div class="checkbox"></div>
                </label>
              </div>
            </div>
          </div>
      {% } %}
        </li>
    </script>
    <script type="text/x-tmpl" id="tmpl_customer_type">
      {% for (var i=0; i<o.length; i++) { %}
        {% if (i%2 == 0) { %}
          {% if (i > 0) { %}
        </li>{% } %} 
        <li class="row">
        {% } %}
          <div class="col-50"> 
            <div class="item-content">
              <div class="item-inner">
                <label class="item-title">{%= o[i] %}</label>
                <label class="item-after label-switch">                    
                  <input id="_t_{%= i %}" type="checkbox" value="{%= o[i] %}" />
                  <div class="checkbox"></div>
                </label>
              </div>
            </div>
          </div>
      {% } %}
        </li>
    </script>
    <script type="text/x-tmpl" id="tmpl_source">
      {% for (var i=0; i<o.length; i++) { %}
        {% if (i%2 == 0) { %}
          {% if (i > 0) { %}
        </li>{% } %} 
        <li class="row">
        {% } %}
          <div class="col-50"> 
            <div class="item-content">
              <div class="item-inner">
                <label class="item-title">{%= o[i] %}</label>
                <label class="item-after label-switch">                    
                  <input id="_s_{%= i %}" type="checkbox" value="{%= o[i] %}" />
                  <div class="checkbox"></div>
                </label>
              </div>
            </div>
          </div>
      {% } %}
        </li>
    </script>
    <script type="text/x-tmpl" id="tmpl_payment">
      {% for (var i=0; i<o.length; i++) { %}
        {% if (i%2 == 0) { %}
          {% if (i > 0) { %}
        </li>{% } %} 
        <li class="row">
        {% } %}
          <div class="col-50"> 
            <div class="item-content">
              <div class="item-inner">
                <label class="item-title label">{%= o[i].name %}</label>
                <label class="item-input">                    
                  <input id="_m_{%= o[i].id %}" type="text" data-price="{%= o[i].price %}" data-id="{%= o[i].id %}" />
                </label>
                <label class="item-after">{% if(o[i].price == 1) print("元"); else print("张"); %}</label>
              </div>
            </div>
          </div>
      {% } %}
        </li>
    </script>
    <script type="text/x-tmpl" id="tmpl_promotion">
      {% for (var i=0; i<o.length; i++) { %}
        {% if (i%2 == 0) { %}
          {% if (i > 0) { %}
        </li>{% } %} 
        <li class="row">
        {% } %}
          <div class="col-50"> 
            <div class="item-content">
              <div class="item-inner">
                <label class="item-title label">{%= o[i].name %}</label>
                <label class="item-input">                    
                  <input id="_p_{%= o[i].id %}" type="text" value="" data-id="{%= o[i].id %}" />
                </label>
              </div>
            </div>
          </div>
      {% } %}
        </li>
    </script>
    <script type="text/x-tmpl" id="tmpl_search_time">
      {% for (var i=0; i<o.length; i++) { %}
        {% if (i%3 == 0) { %}
          {% if (i > 0) { %}
        </li>{% } %} 
        <li class="row">
        {% } %}
          <a href="#" class="col-33 list-button item-link time-span">{%= o[i] %}</a>
      {% } %}
        </li>
    </script>
    <script type="text/x-tmpl" id="tmpl_search_business">
      {% for (var i=0; i<o.length; i++) { %}
        <li><a href="#" class="list-button item-link business-item" data-id="{%= o[i].id %}">{%= o[i].alias %}</a></li>
      {% } %}
    </script>
    <script type="text/x-tmpl" id="tmpl_search_date">
      {% for (var i=0; i<o.length; i++) { %}
        <li><a href="#" class="list-button item-link date-item" data-index="{%=i%}">{%= o[i].name %}</a></li>
      {% } %}
    </script>

    <!-- 你的html代码 -->
    <script type='text/javascript' src='js/jquery.min.js'></script>
    <script type='text/javascript' src='js/light7.min.js'></script>
    <script type='text/javascript' src='js/moment.min.js'></script>
    <script type='text/javascript' src='js/tmpl.min.js'></script>
    <script type='text/javascript' src='js/json2.min.js'></script>
    <script type='text/javascript' src='js/geak.js'></script>
    <script type='text/javascript' src='js/geak-order.js'></script>
  </body>
</html>







