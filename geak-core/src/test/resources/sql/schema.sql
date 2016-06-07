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
);