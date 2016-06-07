-- 删除业务表
DROP TABLE IF EXISTS geak_order_payment;
DROP TABLE IF EXISTS geak_order_promotion;
DROP TABLE IF EXISTS geak_payment_mode;
DROP TABLE IF EXISTS geak_promotion_plan;
DROP TABLE IF EXISTS geak_appointment_business;
DROP TABLE IF EXISTS geak_appointment;
DROP TABLE IF EXISTS geak_order;
DROP TABLE IF EXISTS geak_customer;
DROP TABLE IF EXISTS geak_business;

DROP TABLE IF EXISTS geak_product;
DROP TABLE IF EXISTS geak_user_company;
DROP TABLE IF EXISTS geak_user;
DROP TABLE IF EXISTS geak_company;
DROP TABLE IF EXISTS geak_action_log;

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