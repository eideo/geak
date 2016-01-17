-- ----------------------------
-- 公司表(门店)
-- ----------------------------
DROP TABLE IF EXISTS geak_company;
CREATE TABLE geak_company (
  id      int         NOT NULL AUTO_INCREMENT COMMENT '门店主键标识(自增)',
  name    varchar(64) NOT NULL                COMMENT '门店名称',
  alias   varchar(64)     NULL                COMMENT '门店别名',
  address varchar(256)    NULL                COMMENT '门店地址',
  PRIMARY KEY (id)
);

-- ----------------------------
-- 业务表(主题)
-- ----------------------------
DROP TABLE IF EXISTS geak_business;
CREATE TABLE geak_business (
  id         int         NOT NULL AUTO_INCREMENT COMMENT '业务主键标识(自增)',
  name       varchar(32) NOT NULL                COMMENT '业务名称',
  alias      varchar(32)     NULL                COMMENT '业务别名',
  note       varchar(256)    NULL                COMMENT '业务备注',
  company_id int         NOT NULL                COMMENT '业务所属门店',
  PRIMARY KEY (id),
  CONSTRAINT fk_business_company FOREIGN KEY (company_id) REFERENCES geak_company (id) ON DELETE CASCADE ON UPDATE CASCADE
);

-- ----------------------------
-- 客户表
-- ----------------------------
DROP TABLE IF EXISTS geak_customer;
CREATE TABLE geak_customer (
  id        int         NOT NULL AUTO_INCREMENT COMMENT '客户主键标识(自增)',
  name      varchar(32) NOT NULL                COMMENT '客户姓名',
  telephone varchar(32) NOT NULL                COMMENT '客户电话',
  sex       char(1)     NOT NULL                COMMENT '客户性别(M|F|-)',
  PRIMARY KEY (id)
);

-- ----------------------------
-- 支付模式表
-- ----------------------------
DROP TABLE IF EXISTS geak_payment_mode;
CREATE TABLE geak_payment_mode (
  id   int         NOT NULL AUTO_INCREMENT COMMENT '支付渠道标识(自增)',
  name varchar(32) NOT NULL                COMMENT '支付渠道名称',
  PRIMARY KEY (id)
);

-- ----------------------------
-- 业务优惠活动计划表
-- ----------------------------
DROP TABLE IF EXISTS geak_promotion_plan;
CREATE TABLE geak_promotion_plan (
  id   int         NOT NULL AUTO_INCREMENT COMMENT '促销计划主键标识(自增)',
  name varchar(32) NOT NULL                COMMENT '促销计划名称',
  PRIMARY KEY (id)
);

-- ----------------------------
--  预约表
-- ----------------------------
DROP TABLE IF EXISTS geak_appointment;
CREATE TABLE geak_appointment (
  id               int         NOT NULL AUTO_INCREMENT COMMENT '预约主键标识(自增)',
  appointment_date datetime    NOT NULL                COMMENT '预约的具体时间',
  customer_count   int         NOT NULL                COMMENT '预约客户的数量',
  customer_id      int         NOT NULL                COMMENT '预约客户标识',
  state            varchar(16) NOT NULL                COMMENT '预约状态',
  PRIMARY KEY (id)
);

-- ----------------------------
-- 预约所含的业务表
-- ----------------------------
DROP TABLE IF EXISTS geak_appointment_business;
CREATE TABLE geak_appointment_business (
  appointment_id int NOT NULL COMMENT '预约标识',
  business_id    int NOT NULL COMMENT '业务标识',
  PRIMARY KEY (appointment_id,business_id),
  CONSTRAINT fk_appointment_business1 FOREIGN KEY (appointment_id) REFERENCES geak_appointment (id) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT fk_appointment_business2 FOREIGN KEY (business_id) REFERENCES geak_business (id) ON DELETE CASCADE ON UPDATE CASCADE
);

-- ----------------------------
-- 订单表
-- ----------------------------
DROP TABLE IF EXISTS geak_order;
CREATE TABLE geak_order (
  id             int         NOT NULL AUTO_INCREMENT COMMENT '订单标识(自增)',
  entrance_date  datetime        NULL                COMMENT '进场时间',
  exit_date      datetime        NULL                COMMENT '退场时间',
  state          varchar(32) NOT NULL                COMMENT '订单状态',
  customer_count int         NOT NULL                COMMENT '参与的用户总数',
  customer_type  varchar(32) NOT NULL                COMMENT '参与的主体用户群类型',
  customer_id    int         NOT NULL                COMMENT '订单客户标识',
  business_id    int         NOT NULL                COMMENT '订单对应的业务标识',
  PRIMARY KEY (id)
);

-- ----------------------------
-- 订单支付明细表
-- ----------------------------
DROP TABLE IF EXISTS geak_order_payment;
CREATE TABLE geak_order_payment (
  order_id   int NOT NULL COMMENT '订单标识',
  payment_id int NOT NULL COMMENT '支付渠道标识',
  amount     int NOT NULL COMMENT '消费金额(分)',
  PRIMARY KEY (order_id,payment_id),
  CONSTRAINT fk_order_payment1 FOREIGN KEY (order_id) REFERENCES geak_order (id) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT fk_order_payment2 FOREIGN KEY (payment_id) REFERENCES geak_payment_mode (id) ON DELETE CASCADE ON UPDATE CASCADE
);

-- ----------------------------
-- 订单活动明细表
-- ----------------------------
DROP TABLE IF EXISTS geak_order_promotion;
CREATE TABLE geak_order_promotion (
  order_id     int NOT NULL COMMENT '订单标识',
  promotion_id int NOT NULL COMMENT '促销活动标识',
  count        int NOT NULL COMMENT '促销量',
  PRIMARY KEY (order_id,promotion_id),
  CONSTRAINT pk_order_promotion1 FOREIGN KEY (order_id) REFERENCES geak_order (id) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT pk_order_promotion2 FOREIGN KEY (promotion_id) REFERENCES geak_promotion_plan (id) ON DELETE CASCADE ON UPDATE CASCADE
);