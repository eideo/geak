-- Authority&Authenticate 

-- 删除已有的表
DROP TABLE IF EXISTS aa_user;
DROP TABLE IF EXISTS aa_role;
DROP TABLE IF EXISTS aa_user_role_relation;
DROP TABLE IF EXISTS aa_resource;
DROP TABLE IF EXISTS aa_permission;

-- 系统用户表
CREATE TABLE aa_user (
  id       VARCHAR(36) NOT NULL COMMENT '用户标识-主键',
  account  VARCHAR(50) NOT NULL COMMENT '用户账户名-唯一',
  name     VARCHAR(50) NOT NULL COMMENT '用户姓名',
  password VARCHAR(50) NOT NULL COMMENT '用户密码',
  state    VARCHAR(20) NOT NULL COMMENT '用户状态:NORMAL|LOCKED...',
  CONSTRAINT pk_aa_user PRIMARY KEY (id),
  CONSTRAINT uk_aa_user UNIQUE (account)
) COMMENT = '系统用户表';


-- 系统角色表
CREATE TABLE aa_role (
  id    VARCHAR(36) NOT NULL COMMENT '角色标识-主键',
  name  VARCHAR(50) NOT NULL COMMENT '角色名称-唯一',
  state VARCHAR(20)          COMMENT '角色状态:ENABLED|DISABLED...',
  note  VARCHAR(200)         COMMENT '角色描述',
  CONSTRAINT pk_aa_role PRIMARY KEY (id),
  CONSTRAINT uk_aa_role UNIQUE (name)
) COMMENT = '系统角色表';


-- 用户角色关联表
CREATE TABLE aa_user_role_relation (
  user_id VARCHAR(36) NOT NULL COMMENT '用户标识-外键',
  role_id VARCHAR(36) NOT NULL COMMENT '角色标识-外键',
  CONSTRAINT pk_aa_user_role_relation PRIMARY KEY (user_id, role_id),
  CONSTRAINT fk_aa_user_role_relation_user FOREIGN KEY (user_id) REFERENCES aa_user(id)
    ON DELETE CASCADE 
    ON UPDATE CASCADE,
  CONSTRAINT fk_aa_user_role_relation_role FOREIGN KEY (role_id) REFERENCES aa_role(id)
    ON DELETE CASCADE 
    ON UPDATE CASCADE
) COMMENT = '系统用户和角色关联表';


-- 系统资源表
CREATE TABLE aa_resource (
  id    VARCHAR(36)  NOT NULL COMMENT '资源标识-主键',
  name  VARCHAR(200) NOT NULL COMMENT '资源名称',
  type  VARCHAR(50)  NOT NULL COMMENT '资源类型',
  state VARCHAR(20)           COMMENT '资源状态:ENABLED|DISABLED...',
  note  VARCHAR(200)          COMMENT '资源描述',
  CONSTRAINT pk_aa_resource PRIMARY KEY (id)
) COMMENT = '系统角色表';


-- 系统权限表
CREATE TABLE aa_permission (
  id            VARCHAR(36)  NOT NULL COMMENT '权限标识-主键',
  owner_id      VARCHAR(36)  NOT NULL COMMENT '权限所有者标识-可与多表主键关联',
  resource_id   VARCHAR(36)  NOT NULL COMMENT '权限所保护资源的标识-外键',
  action        VARCHAR(200) NOT NULL COMMENT '权限是表示的操作',
  expired_date  INT          NOT NULL COMMENT '权限过期的时间',
  state         VARCHAR(20)           COMMENT '权限状态:ENABLED|DISABLED|EXPIRED...',
  note          VARCHAR(200)          COMMENT '权限描述',
  CONSTRAINT pk_aa_permission PRIMARY KEY (id),
  -- 资源+操作=权限
  -- CONSTRAINT uk_aa_permission UNIQUE (resource_id, action),
  CONSTRAINT fk_aa_permission FOREIGN KEY (resource_id) REFERENCES aa_resource(id)
    ON DELETE CASCADE 
    ON UPDATE CASCADE
) COMMENT = '系统权限表';


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
-- 用户表
-- ----------------------------
DROP TABLE IF EXISTS geak_user;
CREATE TABLE geak_user (
  id         varchar(64) NOT NULL  COMMENT '用户标识',
  company_id int         NOT NULL  COMMENT '业务所属门店',
  PRIMARY KEY (id),
  CONSTRAINT fk_user_company FOREIGN KEY (company_id) REFERENCES geak_company (id) ON DELETE CASCADE ON UPDATE CASCADE
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
  confirmed_date   datetime        NULL                COMMENT '预约的确认时间',
  cancelled_date   datetime        NULL                COMMENT '预约的取消时间',
  customer_count   int         NOT NULL                COMMENT '预约客户的数量',
  customer_id      int         NOT NULL                COMMENT '预约客户标识',
  state            varchar(16) NOT NULL                COMMENT '预约状态',
  company_id       int         NOT NULL                COMMENT '预约所属公司(门店)标识',
  PRIMARY KEY (id),
  CONSTRAINT fk_appointment_company  FOREIGN KEY (company_id)  REFERENCES geak_company  (id) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT fk_appointment_customer FOREIGN KEY (customer_id) REFERENCES geak_customer (id) ON DELETE CASCADE ON UPDATE CASCADE
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
  company_id     int         NOT NULL                COMMENT '订单所属公司(门店)标识',
  appointment_id int             NULL                COMMENT '订单对应预约的标识,为空表示没有对应订单',
  PRIMARY KEY (id),
  CONSTRAINT fk_order_company  FOREIGN KEY (company_id)  REFERENCES geak_company  (id) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT fk_order_customer FOREIGN KEY (customer_id) REFERENCES geak_customer (id) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT fk_order_business FOREIGN KEY (business_id) REFERENCES geak_business (id) ON DELETE CASCADE ON UPDATE CASCADE
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

-- ----------------------------
-- 重要操作日志表
-- ----------------------------
DROP TABLE IF EXISTS geak_action_log;
CREATE TABLE geak_action_log (
  id           int          NOT NULL AUTO_INCREMENT COMMENT '操作标识(自增)',
  user_id      varchar(36)  NOT NULL COMMENT '操作人标识',
  action       varchar(32)  NOT NULL COMMENT '操作名称',
  name         varchar(255) NOT NULL COMMENT '操作对象',
  content      text         NOT NULL COMMENT '操作内容',
  created_date datetime     NOT NULL COMMENT '操作时间',
  PRIMARY KEY (id)
);