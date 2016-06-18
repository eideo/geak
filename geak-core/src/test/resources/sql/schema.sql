-- 删除业务表
DROP TABLE IF EXISTS geak_order_product;
DROP TABLE IF EXISTS geak_order_payment;
DROP TABLE IF EXISTS geak_order_promotion;
DROP TABLE IF EXISTS geak_payment_mode;
DROP TABLE IF EXISTS geak_promotion_plan;
DROP TABLE IF EXISTS geak_appointment_business;
DROP TABLE IF EXISTS geak_appointment;
DROP TABLE IF EXISTS geak_order;
DROP TABLE IF EXISTS geak_customer;
DROP TABLE IF EXISTS geak_business;

DROP TABLE IF EXISTS geak_member_deposit;
DROP TABLE IF EXISTS geak_member;
DROP TABLE IF EXISTS geak_product;
DROP TABLE IF EXISTS geak_user_company;
DROP TABLE IF EXISTS geak_user;
DROP TABLE IF EXISTS geak_company;
DROP TABLE IF EXISTS geak_action_log;

-- ----------------------------
-- 重要操作日志表
-- ----------------------------
CREATE TABLE geak_action_log (
  id           int          NOT NULL AUTO_INCREMENT COMMENT '操作标识(自增)',
  user_id      varchar(36)  NOT NULL COMMENT '操作人标识',
  action       varchar(32)  NOT NULL COMMENT '操作名称',
  name         varchar(255) NOT NULL COMMENT '操作对象',
  content      text         NOT NULL COMMENT '操作内容',
  created_date datetime     NOT NULL COMMENT '操作时间',
  PRIMARY KEY (id)
) COMMENT = '操作日志表'
;


-- ----------------------------
-- 公司表(门店)
-- ----------------------------
CREATE TABLE geak_company (
  id      INT         NOT NULL AUTO_INCREMENT COMMENT '门店主键标识(自增)',
  name    VARCHAR(64) NOT NULL                COMMENT '门店名称',
  alias   VARCHAR(64) NOT NULL                COMMENT '门店别名',
  address VARCHAR(256)NOT NULL                COMMENT '门店地址',
  PRIMARY KEY (id)
) COMMENT = '公司表'
;


-- ----------------------------
-- 极客用户表
-- ----------------------------
CREATE TABLE geak_user (
  id         VARCHAR(36) NOT NULL COMMENT '用户标识-主键',
  account    VARCHAR(64) NOT NULL COMMENT '用户账户名-唯一',
  name     	 VARCHAR(64) NOT NULL COMMENT '用户姓名',
  phone      VARCHAR(16) NOT NULL COMMENT '用户手机',
  password 	 VARCHAR(64) NOT NULL COMMENT '用户密码',
  state    	 VARCHAR(16) NOT NULL COMMENT '用户状态:NORMAL|LOCKED...',
  company_id INT         NOT NULL COMMENT '用户所属门店',
  PRIMARY KEY (id),
  CONSTRAINT fk_gu_c FOREIGN KEY (company_id) REFERENCES geak_company (id) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT uk_gu_a UNIQUE (account)
) COMMENT = '系统用户表'
;
-- ----------------------------
-- 用户所属门店列表
-- ----------------------------
CREATE TABLE geak_user_company (
  user_id    VARCHAR(36) NOT NULL COMMENT '用户主键标识',
  company_id INT         NOT NULL COMMENT '门店主键标识',
  PRIMARY KEY (user_id, company_id),
  CONSTRAINT fk_guc_u FOREIGN KEY (user_id) REFERENCES geak_user (id) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT fk_guc_c FOREIGN KEY (company_id) REFERENCES geak_company (id) ON DELETE CASCADE ON UPDATE CASCADE
);


-- ----------------------------
-- 产品列表
-- ----------------------------
CREATE TABLE geak_product (
  id      INT         NOT NULL AUTO_INCREMENT COMMENT '产品主键标识(自增)',
  name    VARCHAR(64) NOT NULL                COMMENT '产品名称',
  alias   VARCHAR(64) NOT NULL                COMMENT '产品别名',
  type    VARCHAR(16) NOT NULL                COMMENT '产品类别',
  state   VARCHAR(16) NOT NULL                COMMENT '产品类别',
  price   INT     	  NOT NULL                COMMENT '产品价格',
  price0  INT         NOT NULL                COMMENT '产品原价',
  company_id INT      NOT NULL 				  COMMENT '产品所属门店',
  PRIMARY KEY (id),
  CONSTRAINT fk_gp_c FOREIGN KEY (company_id) REFERENCES geak_company (id) ON DELETE CASCADE ON UPDATE CASCADE
) COMMENT = '产品表'
;


-- ----------------------------
-- 会员表
-- ----------------------------
CREATE TABLE geak_member (
  id      INT         NOT NULL AUTO_INCREMENT COMMENT '会员主键标识(自增)',
--  account VARCHAR(64) NOT NULL                COMMENT '会员账号-唯一',
  name    VARCHAR(64) NOT NULL DEFAULT ''     COMMENT '会员名称',
 nickname VARCHAR(64) NOT NULL DEFAULT ''     COMMENT '会员昵称',
  phone   VARCHAR(16) NOT NULL DEFAULT ''     COMMENT '会员名称',
  sex     CHAR(1)     NOT NULL DEFAULT 'S'    COMMENT '会员性别(M|F|S:student)',
  state   VARCHAR(16) NOT NULL                COMMENT '当前状态',
  
  openid  VARCHAR(32) NOT NULL DEFAULT ''     COMMENT '微信openid',
  unionid VARCHAR(32) NOT NULL DEFAULT ''     COMMENT '微信unionid',
  head   VARCHAR(256) NOT NULL DEFAULT ''     COMMENT '会有头像url',
  
  subscribe_date DATETIME     NULL 		  	  COMMENT '关注时间',
  created_date   DATETIME NOT NULL 		      COMMENT '创建时间',
  
  balance INT     	  NOT NULL                COMMENT '账户余额',
  score	  INT         NOT NULL DEFAULT 0      COMMENT '会员积分',
  PRIMARY KEY (id),
  CONSTRAINT uk_gm_u UNIQUE (openid)
) COMMENT = '会员表'
;

CREATE INDEX i_member_openid ON geak_member(openid);

-- ----------------------------
-- 会员充值记录表
-- ----------------------------
CREATE TABLE geak_member_deposit (
  id            INT         NOT NULL AUTO_INCREMENT COMMENT '充值记录主键标识(自增)',
  amount        INT     	NOT NULL                COMMENT '充值金额',
  state         VARCHAR(16) NOT NULL DEFAULT ''     COMMENT '当前状态',
  record_type   VARCHAR(16) NOT NULL DEFAULT ''     COMMENT '充值类型',
  trade_no      VARCHAR(64) NOT NULL DEFAULT ''     COMMENT '支付单号',   
  trade_content VARCHAR(64) NOT NULL DEFAULT ''     COMMENT '支付内容',
  trade_type    VARCHAR(16) NOT NULL DEFAULT ''     COMMENT '支付类型',
  ip            VARCHAR(16) NOT NULL DEFAULT ''     COMMENT 'ip',
  
  begin_date    DATETIME    NOT NULL 		      	COMMENT '交易开始时间',
  over_date     DATETIME    NOT NULL 		  	  	COMMENT '交易结束时间',
  
  member_id     INT         NOT NULL                COMMENT '支付会员编号',
  openid        VARCHAR(32) NOT NULL                COMMENT '支付会员openid',
  PRIMARY KEY (id)
) COMMENT = '会员充值记录表'
;

CREATE INDEX i_md_trade ON geak_member_deposit(trade_no);


-- ----------------------------
-- 订单表
-- ----------------------------
CREATE TABLE geak_order (
  id             int         NOT NULL AUTO_INCREMENT COMMENT '订单标识(自增)',
  amount	     int         NOT NULL                COMMENT '订单实际支付的总价',
  state          varchar(32) NOT NULL                COMMENT '订单状态',
  company_id     int         NOT NULL                COMMENT '订单所属公司(门店)标识',
  member_id      int         NOT NULL DEFAULT 0      COMMENT '订单客户标识',
  member_name    varchar(16)     NULL                COMMENT '订单客户姓名',
  member_sex     char(1)     NOT NULL DEFAULT 'S'    COMMENT '玩家性别(M|F|S:student)',
  member_phone   varchar(16)     NULL                COMMENT '订单客户电话',
  member_count   int         NOT NULL DEFAULT 0      COMMENT '参与的用户总数',
  member_type    varchar(64)     NULL                COMMENT '参与的主体用户群类型',
  content        varchar(64)     NULL                COMMENT '订单主要内容',
  source         varchar(64)     NULL                COMMENT '客户来源渠道',
  note           varchar(255)    NULL                COMMENT '订单备注',
  promotion_note varchar(255)    NULL                COMMENT '其他促销活动说明',
  payment_mode   char(1)     NOT NULL DEFAULT '0'    COMMENT '支付模式',
  payment_note   varchar(255)    NULL                COMMENT '支付备注说明',
  payment_date   datetime        NULL                COMMENT '支付时间',
  entrance_date  datetime        NULL                COMMENT '进场时间',
  exit_date      datetime        NULL                COMMENT '退场时间',
  cancelled_date datetime        NULL                COMMENT '取消订单时间',
  created_date   datetime    NOT NULL                COMMENT '创建时间',
  PRIMARY KEY (id),
  CONSTRAINT fk_order_company  FOREIGN KEY (company_id)  REFERENCES geak_company  (id) ON DELETE CASCADE ON UPDATE CASCADE
) COMMENT = '订单表'
;

CREATE INDEX i_order_member ON geak_order(member_id);

-- ----------------------------
-- 订单支付明细表
-- ----------------------------
CREATE TABLE geak_order_payment (
  order_id int         NOT NULL COMMENT '订单标识',
  mode     varchar(32) NOT NULL COMMENT '支付模式',
  count    int         NOT NULL COMMENT '消费数量',
  -- note    varchar(255) NULL COMMENT '备注信息',
  PRIMARY KEY (order_id, mode),
  CONSTRAINT fk_order_payment FOREIGN KEY (order_id) REFERENCES geak_order(id) ON DELETE CASCADE ON UPDATE CASCADE
) COMMENT = '订单支付明细表'
;

-- ----------------------------
-- 订单优惠明细表
-- ----------------------------
CREATE TABLE geak_order_promotion (
  order_id  int         NOT NULL COMMENT '订单标识',
  mode      varchar(32) NOT NULL COMMENT '促销方案',
  count     int             NULL COMMENT '促销量',
  note      varchar(255)    NULL COMMENT '备注信息',
  PRIMARY KEY (order_id,mode),
  CONSTRAINT pk_order_promotion FOREIGN KEY (order_id) REFERENCES geak_order(id) ON DELETE CASCADE ON UPDATE CASCADE
) COMMENT = '订单优惠明细表'
;

-- ----------------------------
-- 订单产品明细表
-- ----------------------------
CREATE TABLE geak_order_product (
  order_id   int         NOT NULL COMMENT '订单标识',
  product_id int         NOT NULL COMMENT '产品标识',
  count      int         NOT NULL COMMENT '产品数量',
  price      int         NOT NULL COMMENT '产品价格',
  -- note    varchar(255) NULL COMMENT '备注信息',
  PRIMARY KEY (order_id,product_id),
  CONSTRAINT pk_order_product1 FOREIGN KEY (order_id) REFERENCES geak_order(id) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT pk_order_product2 FOREIGN KEY (product_id) REFERENCES geak_product(id) ON DELETE CASCADE ON UPDATE CASCADE
) COMMENT = '订单产品明细表'
;

