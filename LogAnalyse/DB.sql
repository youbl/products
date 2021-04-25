/*
SQLyog Ultimate
MySQL - 5.7.24-log : Database - logs
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
/*Table structure for table `configs` */

DROP TABLE IF EXISTS `configs`;

CREATE TABLE `configs` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
  `type` varchar(20) NOT NULL DEFAULT '' COMMENT '配置类型',
  `val` varchar(40) NOT NULL DEFAULT '' COMMENT '配置值',
  `creationTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '新增时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_app` (`type`,`val`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*Data for the table `configs` */

insert  into `configs`(`id`,`type`,`val`,`creationTime`) values 

(4975,'app','app','2021-04-23 16:54:39'),
(4976,'app','miniprogram','2021-04-23 16:54:39'),
(4977,'app','s2b','2021-04-23 16:54:39'),
(4978,'app','qc','2021-04-23 16:54:39'),
(4979,'app','configserver','2021-04-23 16:54:40'),
(4980,'app','sd-app','2021-04-23 16:54:40'),
(4981,'app','sdmember','2021-04-23 16:54:40'),
(4982,'app','distribution','2021-04-23 16:54:40'),
(4983,'app','mer','2021-04-23 16:54:40'),
(4984,'app','booking','2021-04-23 16:54:40'),
(4985,'app','open','2021-04-23 16:54:40'),
(4986,'app','shopmember','2021-04-23 16:54:40'),
(4987,'app','sdscm','2021-04-23 16:54:40'),
(4988,'app','scan','2021-04-23 16:54:40'),
(4989,'app','sd-frame','2021-04-23 16:54:40'),
(4990,'app','configs','2021-04-23 16:54:40'),
(4991,'app','user2-mall','2021-04-23 16:54:40'),
(4992,'app','cc','2021-04-23 16:54:40'),
(4993,'app','spapi','2021-04-23 16:54:40'),
(4994,'app','bi-api','2021-04-23 16:54:40'),
(4995,'app','mike-oauth','2021-04-23 16:54:40'),
(4996,'app','oss','2021-04-23 16:54:41'),
(4997,'app','static','2021-04-23 16:54:41'),
(4998,'app','omsapi','2021-04-23 16:54:41'),
(4999,'app','_','2021-04-23 16:54:41'),
(5000,'app','-','2021-04-23 16:54:41'),
(5001,'app','sdcloud','2021-04-23 16:54:41'),
(5002,'app','mike-third','2021-04-23 16:54:41'),
(5003,'app','queue','2021-04-23 16:54:41'),
(5004,'app','mike-onedata','2021-04-23 16:54:41'),
(5005,'app','appmember','2021-04-23 16:54:41'),
(5006,'app','shandian','2021-04-23 16:54:41'),
(5007,'app','waimai-activity','2021-04-23 16:54:41'),
(5008,'app','waimai-member','2021-04-23 16:54:41'),
(5009,'app','so','2021-04-23 16:54:41'),
(5010,'app','sdsaas','2021-04-23 16:54:41'),
(5011,'app','mergeexpress','2021-04-23 16:54:41'),
(5012,'app','report','2021-04-23 16:54:42'),
(5013,'app','mike-basepay','2021-04-23 16:54:42'),
(5014,'app','mobile','2021-04-23 16:54:42'),
(5015,'app','mike-mall','2021-04-23 16:54:42'),
(5016,'app','mike-thirdcrm','2021-04-23 16:54:42'),
(5017,'app','mike-frame','2021-04-23 16:54:42'),
(5018,'app','mergetakeout','2021-04-23 16:54:42'),
(5019,'app','oauth','2021-04-23 16:54:42'),
(5020,'app','user2-customer','2021-04-23 16:54:42'),
(5021,'app','user2-oauth','2021-04-23 16:54:42'),
(5022,'app','f','2021-04-23 16:54:42'),
(5023,'app','newm','2021-04-23 16:54:42'),
(5024,'app','mike-cashier','2021-04-23 16:54:42'),
(5025,'app','api','2021-04-23 16:54:42'),
(5026,'app','f-cashier','2021-04-23 16:54:42'),
(5027,'app','mike-calc','2021-04-23 16:54:42'),
(5028,'app','pay','2021-04-23 16:54:42'),
(5029,'app','wx','2021-04-23 16:54:42'),
(5030,'app','membertag','2021-04-23 16:54:43'),
(5031,'app','sdcloud-gateway','2021-04-23 16:54:43'),
(5032,'app','o','2021-04-23 16:54:43'),
(5033,'app','f-config','2021-04-23 16:54:43'),
(5034,'app','newqr','2021-04-23 16:54:43'),
(5035,'app','mp','2021-04-23 16:54:43'),
(5036,'app','mike-customer','2021-04-23 16:54:43'),
(5037,'app','scan-order-gateway','2021-04-23 16:54:43'),
(5038,'app','front','2021-04-23 16:54:43'),
(5039,'app','micromgr','2021-04-23 16:54:43'),
(5040,'app','trade','2021-04-23 16:54:43'),
(5041,'app','account','2021-04-23 16:54:43'),
(5042,'app','push','2021-04-23 16:54:43'),
(5043,'app','os','2021-04-23 16:54:43'),
(5044,'app','user2','2021-04-23 16:54:43'),
(5045,'app','od','2021-04-23 16:54:43'),
(5046,'app','user2-member','2021-04-23 16:54:43'),
(5047,'app','user2-activity','2021-04-23 16:54:43'),
(5048,'app','socket.io','2021-04-23 16:54:43'),
(5049,'app','sd-api','2021-04-23 16:54:43'),
(5050,'app','user2-bizcustomer','2021-04-23 16:54:43'),
(5051,'app','mc','2021-04-23 16:54:43'),
(5052,'app','mike-platform','2021-04-23 16:54:43'),
(5053,'frontExt','.js','2021-04-25 10:07:57'),
(5054,'frontExt','.html','2021-04-25 10:07:59'),
(5055,'frontExt','.htm','2021-04-25 10:08:10'),
(5056,'frontExt','.json','2021-04-25 10:12:24'),
(5057,'frontExt','.xml','2021-04-25 10:12:41'),
(5058,'frontExt','.css','2021-04-25 10:12:47'),
(5059,'frontExt','.ico','2021-04-25 10:13:05'),
(5060,'frontExt','.txt','2021-04-25 10:13:10'),
(5061,'frontExt','.png','2021-04-25 10:13:17'),
(5062,'frontExt','.jpg','2021-04-25 10:13:22'),
(5063,'frontExt','.jpeg','2021-04-25 10:13:28'),
(5064,'frontExt','.gif','2021-04-25 10:13:35'),
(5065,'frontExt','.bmp','2021-04-25 10:13:41');

/*Table structure for table `dateweek` */

DROP TABLE IF EXISTS `dateweek`;

CREATE TABLE `dateweek` (
  `day` int(11) NOT NULL DEFAULT '0' COMMENT '请求时间',
  `week` tinyint(4) NOT NULL DEFAULT '0' COMMENT '星期几',
  PRIMARY KEY (`day`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
insert  into `dateweek`(`day`,`week`) values 

(20210303,3),
(20210304,4),
(20210305,5),
(20210306,6),
(20210307,7),
(20210308,1),
(20210309,2),
(20210310,3),
(20210311,4),
(20210312,5),
(20210313,6),
(20210314,7),
(20210315,1),
(20210316,2),
(20210317,3),
(20210318,4),
(20210319,5),
(20210320,6),
(20210321,7),
(20210322,1),
(20210323,2),
(20210324,3),
(20210325,4),
(20210326,5),
(20210327,6),
(20210328,7),
(20210329,1),
(20210330,2),
(20210331,3),
(20210401,4),
(20210402,5),
(20210403,6),
(20210404,7),
(20210405,1),
(20210406,2),
(20210407,3),
(20210408,4),
(20210409,5),
(20210410,6),
(20210411,7),
(20210412,1),
(20210413,2),
(20210414,3),
(20210415,4),
(20210416,5),
(20210417,6),
(20210418,7),
(20210419,1),
(20210420,2),
(20210421,3),
(20210422,4),
(20210423,5),
(20210424,6),
(20210425,7),
(20210426,1),
(20210427,2),
(20210428,3),
(20210429,4),
(20210430,5);

/*Table structure for table `nginxapplog` */

DROP TABLE IF EXISTS `nginxapplog`;

CREATE TABLE `nginxapplog` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
  `hour` int(11) NOT NULL COMMENT '请求时间',
  `isfront` tinyint(4) NOT NULL COMMENT '是否前端访问',
  `app` varchar(40) NOT NULL COMMENT '请求地址',
  `num` int(11) NOT NULL COMMENT '请求次数',
  `day` int(11) NOT NULL COMMENT '文件名日期',
  `CreationTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '新增时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `unq_app` (`app`,`hour`,`isfront`,`day`),
  KEY `idx_num` (`num`),
  KEY `idx_hour` (`hour`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

/*Table structure for table `nginxlog` */

DROP TABLE IF EXISTS `nginxlog`;

CREATE TABLE `nginxlog` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
  `timeLocal` varchar(40) NOT NULL COMMENT '时间',
  `remoteAddr` varchar(20) NOT NULL COMMENT 'IP',
  `remoteUser` varchar(20) NOT NULL COMMENT '用户',
  `host` varchar(40) NOT NULL COMMENT '主机',
  `request` varchar(4000) NOT NULL COMMENT '请求方法和地址',
  `status` int(11) NOT NULL COMMENT '响应状态',
  `requestLength` int(11) NOT NULL COMMENT '请求长度',
  `bodyBytesSent` int(11) NOT NULL COMMENT '发送长度',
  `referer` varchar(4000) NOT NULL COMMENT 'referer',
  `userAgent` varchar(4000) NOT NULL COMMENT 'UA',
  `forwardedFor` varchar(100) NOT NULL COMMENT '代理IP',
  `upstreamAddr` varchar(40) NOT NULL COMMENT '后端IP+端口',
  `requestTime` int(11) NOT NULL COMMENT '请求时长',
  `upstreamTime` int(11) NOT NULL COMMENT '后端响应时长',
  `upstreamStatus` int(11) NOT NULL COMMENT '后端状态',
  `contentLength` int(11) NOT NULL COMMENT '内容长度',
  `httpContentLength` int(11) NOT NULL COMMENT 'http内容长',
  `sentContentLength` int(11) NOT NULL COMMENT '发送内容长',
  `fileName` varchar(200) NOT NULL COMMENT '采集源文件',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

/*Table structure for table `nginxtotallog` */

DROP TABLE IF EXISTS `nginxtotallog`;

CREATE TABLE `nginxtotallog` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
  `date` int(11) NOT NULL COMMENT '请求时间',
  `referer` varchar(4000) NOT NULL COMMENT 'referer',
  `request` varchar(4000) NOT NULL COMMENT '请求地址',
  `num` int(11) NOT NULL COMMENT '请求次数',
  PRIMARY KEY (`id`),
  KEY `idx_num` (`num`),
  KEY `idx_hour` (`date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

/*Table structure for table `tasks` */

DROP TABLE IF EXISTS `tasks`;

CREATE TABLE `tasks` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
  `filename` varchar(200) NOT NULL DEFAULT '' COMMENT '文件名',
  `state` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0未处理;1处理中;2完成',
  `num` int(11) NOT NULL DEFAULT '0' COMMENT '行数',
  `ymd` int(11) NOT NULL DEFAULT '0' COMMENT '日期',
  `creationTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '新增时间',
  `lastModificationTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后修改时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_file` (`filename`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
