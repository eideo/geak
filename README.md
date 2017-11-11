# geak
极客密室管理系统

## 模块说明
 - geak-core : 核心工具包
 - geak-erp-webapp : 极客员工ERP-WEB工程
 - geak-member-webapp : 极客会员WEB工程
 - database : mysql数据库备份文件
 
## 编译说明
使用 maven 构建系统，在项目根目录执行 mvn install 命令即可。

## 基本运行配置
  1. 建立Mysql数据库geak_v2；
  2. 将初始化数据 database/geak_v2_2017-11-11.sql 导入geak_v2库；
  3. 建立Mysql用户:Geak，密码:Geak2016（数据库连接参数在相应的spring配置文件中修改即可）
  4. 在 geak-erp-webapp 目录下执行 mvn jetty:run 即可启动极客ERP-WEB工程
  5. 访问 http://localhost:8080/index.html 
