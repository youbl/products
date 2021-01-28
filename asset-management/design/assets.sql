/*
SQLyog Ultimate
MySQL - 5.7.27-log : Database - assets
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
/*Table structure for table `assetclass` */

DROP TABLE IF EXISTS `assetclass`;

CREATE TABLE `assetclass` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'Id',
  `className` varchar(20) NOT NULL COMMENT '分类名',
  `CreationTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `LastModificationTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后修改时间',
  `description` varchar(200) NOT NULL COMMENT '说明',
  PRIMARY KEY (`id`),
  UNIQUE KEY `unq_className` (`className`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='资产分类表';

/*Table structure for table `assets` */

DROP TABLE IF EXISTS `assets`;

CREATE TABLE `assets` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'Id',
  `code` varchar(20) NOT NULL COMMENT '资产编号',
  `assetName` varchar(100) NOT NULL COMMENT '资产名称',
  `classId` int(11) NOT NULL COMMENT '资产分类id',
  `description` varchar(500) NOT NULL COMMENT '说明',
  `buyTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '购买时间',
  `price` int(11) NOT NULL COMMENT '购买价格，单位分',
  `state` int(11) NOT NULL COMMENT '状态 0可用，1借出，2故障，3报废',
  `place` varchar(50) NOT NULL COMMENT '库存位置编号',
  `CreationTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `LastModificationTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后修改时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `unq_code` (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='资产信息表';

/*Table structure for table `department` */

DROP TABLE IF EXISTS `department`;

CREATE TABLE `department` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'Id',
  `departmentName` varchar(20) NOT NULL COMMENT '部门名称',
  `description` varchar(200) NOT NULL COMMENT '说明',
  `CreationTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `LastModificationTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后修改时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `unq_name` (`departmentName`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='部门信息表';

/*Table structure for table `operatelog` */

DROP TABLE IF EXISTS `operatelog`;

CREATE TABLE `operatelog` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'Id',
  `code` varchar(20) NOT NULL COMMENT '关联资产',
  `userId` int(11) NOT NULL COMMENT '关联用户',
  `type` varchar(20) NOT NULL COMMENT '日志类型',
  `description` varchar(500) NOT NULL COMMENT '说明',
  `operator` int(11) NOT NULL COMMENT '操作人',
  `CreationTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_code` (`code`),
  KEY `idx_user` (`userId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='日志表';

/*Table structure for table `users` */

DROP TABLE IF EXISTS `users`;

CREATE TABLE `users` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'Id',
  `code` varchar(20) NOT NULL COMMENT '工号',
  `account` varchar(20) NOT NULL COMMENT '登录名',
  `userName` varchar(20) NOT NULL COMMENT '姓名',
  `department` int(11) NOT NULL COMMENT '所属部门',
  `password` varchar(50) NOT NULL COMMENT '加密后的密码',
  `phone` varchar(2) NOT NULL COMMENT '联系电话',
  `sex` tinyint(4) NOT NULL COMMENT '性别 0男 1女',
  `CreationTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `LastModificationTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后修改时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `unq_account` (`account`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='员工信息表';

/*Table structure for table `validcode` */

DROP TABLE IF EXISTS `validcode`;

CREATE TABLE `validcode` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'Id',
  `code` varchar(20) NOT NULL COMMENT '验证码',
  `sn` varchar(20) NOT NULL COMMENT '验证码序号',
  `type` tinyint(4) NOT NULL DEFAULT '0' COMMENT '类型，0图形，1短信',
  `enableErrNum` tinyint(4) NOT NULL DEFAULT '1' COMMENT '允许错误次数',
  `CreationTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `LastModificationTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后修改时间',
  `description` varchar(200) NOT NULL COMMENT '说明',
  PRIMARY KEY (`id`),
  KEY `idx_sn` (`sn`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='验证码表';

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
