/*
SQLyog Ultimate
MySQL - 5.7.26 : Database - assets
*********************************************************************
*/


/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
/*Table structure for table `assetaudit` */

DROP TABLE IF EXISTS `assetaudit`;

CREATE TABLE `assetaudit` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'Id',
  `type` varchar(20) NOT NULL DEFAULT '' COMMENT '审核类型',
  `subtype` varchar(20) NOT NULL DEFAULT '' COMMENT '审核子类型',
  `description` varchar(100) NOT NULL DEFAULT '' COMMENT '申请说明',
  `classId` int(11) NOT NULL DEFAULT '0' COMMENT '资产分类id',
  `account` varchar(50) NOT NULL DEFAULT '' COMMENT '申请人',
  `returnTime` datetime NOT NULL COMMENT '归还时间',
  `state` int(11) NOT NULL DEFAULT '0' COMMENT '审核状态,0库存,1借出,2故障,3报废',
  `auditUser` varchar(20) NOT NULL DEFAULT '' COMMENT '审核人',
  `auditReason` varchar(100) NOT NULL DEFAULT '' COMMENT '审核意见',
  `CreationTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `LastModificationTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后修改时间',
  PRIMARY KEY (`id`),
  KEY `idx_code` (`auditUser`),
  KEY `idx_account` (`account`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='资产审核表';

/*Table structure for table `assetauditdetail` */

DROP TABLE IF EXISTS `assetauditdetail`;

CREATE TABLE `assetauditdetail` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'Id',
  `auditId` int(11) NOT NULL DEFAULT '0' COMMENT '审核表主键',
  `code` varchar(20) NOT NULL DEFAULT '' COMMENT '资产编码',
  `CreationTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_code` (`code`,`auditId`),
  KEY `idx_auditId` (`auditId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='资产审核分配表';

/*Table structure for table `assetclass` */

DROP TABLE IF EXISTS `assetclass`;

CREATE TABLE `assetclass` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'Id',
  `className` varchar(20) NOT NULL DEFAULT '' COMMENT '分类名',
  `description` varchar(200) NOT NULL DEFAULT '' COMMENT '说明',
  `admin` varchar(50) NOT NULL DEFAULT '' COMMENT '管理员',
  `totalAmount` int(11) NOT NULL DEFAULT '0' COMMENT '资产数量，用于资产code生成',
  `CreationTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `LastModificationTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后修改时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `unq_className` (`className`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='资产分类表';

/*Table structure for table `assets` */

DROP TABLE IF EXISTS `assets`;

CREATE TABLE `assets` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'Id',
  `code` varchar(20) NOT NULL DEFAULT '' COMMENT '资产编号',
  `assetName` varchar(100) NOT NULL DEFAULT '' COMMENT '资产名称',
  `classId` int(11) NOT NULL DEFAULT '0' COMMENT '资产分类id',
  `description` varchar(500) NOT NULL DEFAULT '' COMMENT '说明',
  `buyTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '购买时间',
  `price` int(11) NOT NULL DEFAULT '0' COMMENT '购买价格，单位分',
  `state` int(11) NOT NULL DEFAULT '0' COMMENT '状态 0可用，1借出，2故障，3报废',
  `place` varchar(50) NOT NULL DEFAULT '' COMMENT '库存位置编号',
  `account` varchar(50) NOT NULL DEFAULT '' COMMENT '保管人',
  `accountTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '最后借用时间',
  `CreationTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `LastModificationTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后修改时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `unq_code` (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='资产信息表';

/*Table structure for table `configs` */

DROP TABLE IF EXISTS `configs`;

CREATE TABLE `configs` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'Id',
  `type` varchar(20) NOT NULL DEFAULT '' COMMENT '配置类型',
  `code` varchar(20) NOT NULL DEFAULT '' COMMENT '配置键值',
  `description` varchar(100) NOT NULL DEFAULT '' COMMENT '配置说明',
  `CreationTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_type_code` (`type`,`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='配置表';

/*Table structure for table `department` */

DROP TABLE IF EXISTS `department`;

CREATE TABLE `department` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'Id',
  `departmentName` varchar(20) NOT NULL DEFAULT '' COMMENT '部门名称',
  `description` varchar(200) NOT NULL DEFAULT '' COMMENT '说明',
  `CreationTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `LastModificationTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后修改时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `unq_name` (`departmentName`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='部门信息表';

/*Table structure for table `operatelog` */

DROP TABLE IF EXISTS `operatelog`;

CREATE TABLE `operatelog` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'Id',
  `code` varchar(20) NOT NULL DEFAULT '' COMMENT '关联资产',
  `account` varchar(50) NOT NULL DEFAULT '' COMMENT '关联用户',
  `type` varchar(20) NOT NULL DEFAULT '' COMMENT '日志类型',
  `subType` varchar(20) NOT NULL DEFAULT '' COMMENT '子类型',
  `description` varchar(500) NOT NULL DEFAULT '' COMMENT '说明',
  `operator` varchar(50) NOT NULL DEFAULT '' COMMENT '操作人',
  `ip` varchar(50) NOT NULL DEFAULT '' COMMENT '操作IP',
  `CreationTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_code` (`code`),
  KEY `idx_user` (`account`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='日志表';

/*Table structure for table `users` */

DROP TABLE IF EXISTS `users`;

CREATE TABLE `users` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'Id',
  `code` varchar(20) NOT NULL DEFAULT '' COMMENT '工号',
  `account` varchar(20) NOT NULL DEFAULT '' COMMENT '登录名',
  `userName` varchar(20) NOT NULL DEFAULT '' COMMENT '姓名',
  `department` int(11) NOT NULL DEFAULT '0' COMMENT '所属部门',
  `password` varchar(50) NOT NULL DEFAULT '' COMMENT '加密后的密码',
  `phone` varchar(20) NOT NULL DEFAULT '' COMMENT '联系电话',
  `sex` tinyint(4) NOT NULL DEFAULT '0' COMMENT '性别 0男 1女',
  `roles` varchar(100) NOT NULL DEFAULT '' COMMENT '角色',
  `state` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0待审核 1禁用 8可用',
  `CreationTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `LastModificationTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后修改时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `unq_account` (`account`),
  UNIQUE KEY `unq_code` (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='员工信息表';

/*Table structure for table `validcode` */

DROP TABLE IF EXISTS `validcode`;

CREATE TABLE `validcode` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'Id',
  `code` varchar(20) NOT NULL DEFAULT '' COMMENT '验证码',
  `sn` varchar(20) NOT NULL DEFAULT '' COMMENT '验证码序号',
  `type` tinyint(4) NOT NULL DEFAULT '0' COMMENT '类型，0图形，1短信',
  `enableErrNum` tinyint(4) NOT NULL DEFAULT '1' COMMENT '允许错误次数',
  `CreationTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `LastModificationTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后修改时间',
  PRIMARY KEY (`id`),
  KEY `idx_sn` (`sn`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='验证码表';

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
