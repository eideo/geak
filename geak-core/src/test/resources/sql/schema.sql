-- Authority&Authenticate 

-- 删除已有的表
DROP TABLE IF EXISTS aa_user_role_relation;
DROP TABLE IF EXISTS aa_permission;
DROP TABLE IF EXISTS aa_user;
DROP TABLE IF EXISTS aa_role;
DROP TABLE IF EXISTS aa_resource;

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
DROP TABLE IF EXISTS geak_user;
DROP TABLE IF EXISTS geak_company;
DROP TABLE IF EXISTS geak_action_log;

-- ----------------------------
-- 公司表(门店)
-- ----------------------------
CREATE TABLE geak_company (
  id      int         NOT NULL AUTO_INCREMENT COMMENT '门店主键标识(自增)',
  name    varchar(64) NOT NULL                COMMENT '门店名称',
  alias   varchar(64)     NULL                COMMENT '门店别名',
  address varchar(256)    NULL                COMMENT '门店地址',
  PRIMARY KEY (id)
) COMMENT = '公司表'
;

-- ----------------------------
-- 极客用户表
-- ----------------------------
CREATE TABLE geak_user (
  id         VARCHAR(36) NOT NULL COMMENT '用户标识-主键',
  account    VARCHAR(50) NOT NULL COMMENT '用户账户名-唯一',
  name     	 VARCHAR(50) NOT NULL COMMENT '用户姓名',
  password 	 VARCHAR(50) NOT NULL COMMENT '用户密码',
  state    	 VARCHAR(20) NOT NULL COMMENT '用户状态:NORMAL|LOCKED...',
  company_id int         NOT NULL COMMENT '用户所属门店',
  PRIMARY KEY (id),
  CONSTRAINT fk_user_company FOREIGN KEY (company_id) REFERENCES geak_company (id) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT uk_geak_user UNIQUE (account)
) COMMENT = '系统用户表'
;