/*
Navicat MySQL Data Transfer

Source Server         : localMysql
Source Server Version : 50617
Source Host           : localhost:3306
Source Database       : telemarketingdb

Target Server Type    : MYSQL
Target Server Version : 50617
File Encoding         : 65001

Date: 2017-12-04 13:27:52
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `allow_ip`
-- ----------------------------
DROP TABLE IF EXISTS `allow_ip`;
CREATE TABLE `allow_ip` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `location` varchar(50) DEFAULT NULL,
  `ip` varchar(20) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of allow_ip
-- ----------------------------
INSERT INTO `allow_ip` VALUES ('18', 'auto_18723477723', '0:0:0:0:0:0:0:1');
INSERT INTO `allow_ip` VALUES ('19', 'auto_18782404805', '0:0:0:0:0:0:0:1');

-- ----------------------------
-- Table structure for `business_type`
-- ----------------------------
DROP TABLE IF EXISTS `business_type`;
CREATE TABLE `business_type` (
  `id` tinyint(4) NOT NULL AUTO_INCREMENT,
  `type_name` varchar(50) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of business_type
-- ----------------------------
INSERT INTO `business_type` VALUES ('1', '无业务');
INSERT INTO `business_type` VALUES ('2', '工薪贷');
INSERT INTO `business_type` VALUES ('3', '车抵押货');
INSERT INTO `business_type` VALUES ('4', '公积金贷');
INSERT INTO `business_type` VALUES ('5', '优势贷');
INSERT INTO `business_type` VALUES ('6', '按揭房放大贷');
INSERT INTO `business_type` VALUES ('7', '按揭车放大贷');
INSERT INTO `business_type` VALUES ('8', '物业贷');
INSERT INTO `business_type` VALUES ('9', '汽车信用贷');
INSERT INTO `business_type` VALUES ('10', '一抵');
INSERT INTO `business_type` VALUES ('11', '二抵');
INSERT INTO `business_type` VALUES ('12', '全款车抵押');
INSERT INTO `business_type` VALUES ('13', '押车');
INSERT INTO `business_type` VALUES ('14', '短期拆借');
INSERT INTO `business_type` VALUES ('15', '垫资过桥');
INSERT INTO `business_type` VALUES ('16', '0首付购车购房');
INSERT INTO `business_type` VALUES ('17', '信用卡');
INSERT INTO `business_type` VALUES ('18', '网贷');

-- ----------------------------
-- Table structure for `busireport`
-- ----------------------------
DROP TABLE IF EXISTS `busireport`;
CREATE TABLE `busireport` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `dateStr` date NOT NULL,
  `dept_name` varchar(50) NOT NULL,
  `user_phone` varchar(11) NOT NULL,
  `user_name` varchar(20) NOT NULL,
  `up_down_id` varchar(50) NOT NULL,
  `business_name` varchar(50) NOT NULL,
  `state` tinyint(4) NOT NULL,
  `counts` int(11) NOT NULL,
  `addtime` datetime NOT NULL,
  PRIMARY KEY (`id`),
  KEY `up_down_id` (`up_down_id`),
  KEY `addtime` (`addtime`),
  KEY `state` (`state`),
  KEY `dateStr` (`dateStr`),
  KEY `user_phone` (`user_phone`),
  KEY `user_name` (`user_name`)
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of busireport
-- ----------------------------

-- ----------------------------
-- Table structure for `customer`
-- ----------------------------
DROP TABLE IF EXISTS `customer`;
CREATE TABLE `customer` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `phone` varchar(11) NOT NULL,
  `cus_name` varchar(20) NOT NULL,
  `id_person` varchar(20) DEFAULT NULL,
  `address` varchar(100) DEFAULT '',
  `who_use` varchar(11) DEFAULT '000000',
  `who_use_name` varchar(20) DEFAULT '0',
  `who_get_time` datetime DEFAULT NULL,
  `who_up_down_id` varchar(50) NOT NULL,
  `share_use` varchar(500) DEFAULT '',
  `share_use_name` varchar(500) DEFAULT '',
  `share_get_time` datetime DEFAULT NULL,
  `add_time` datetime DEFAULT NULL,
  `update_state_time` datetime DEFAULT NULL,
  `data_from` varchar(50) DEFAULT '',
  `business_name` varchar(50) DEFAULT NULL,
  `remark` varchar(500) DEFAULT '',
  `state` tinyint(4) NOT NULL DEFAULT '0' COMMENT '见状态表',
  `is_common` tinyint(4) DEFAULT '0' COMMENT '是否在公共池 0不在 1在',
  `in_common_time` datetime DEFAULT NULL COMMENT '放到公共池时间',
  `who_put_common` varchar(11) DEFAULT NULL COMMENT '谁把当前记录放入公共池',
  `who_put_common_name` varchar(20) DEFAULT NULL,
  `has_recovery` tinyint(4) DEFAULT '0' COMMENT '是否回收 0：否 1：是',
  `recovery_from` varchar(20) DEFAULT '',
  `recovery_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `phone` (`phone`),
  KEY `cus_name` (`cus_name`),
  KEY `who_use` (`who_use`),
  KEY `who_up_down_id` (`who_up_down_id`),
  KEY `share_use` (`share_use`(255)),
  KEY `update_state_time` (`update_state_time`),
  KEY `add_time` (`add_time`),
  KEY `state` (`state`),
  KEY `data_from` (`data_from`),
  KEY `business_name` (`business_name`),
  KEY `who_use_name` (`who_use_name`)
) ENGINE=InnoDB AUTO_INCREMENT=2483 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of customer
-- ----------------------------

-- ----------------------------
-- Table structure for `customer_state`
-- ----------------------------
DROP TABLE IF EXISTS `customer_state`;
CREATE TABLE `customer_state` (
  `id` int(11) NOT NULL,
  `name` varchar(30) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of customer_state
-- ----------------------------
INSERT INTO `customer_state` VALUES ('0', '未联系');
INSERT INTO `customer_state` VALUES ('1', '无意向');
INSERT INTO `customer_state` VALUES ('2', '空号停机');
INSERT INTO `customer_state` VALUES ('3', '暂不需要可以跟踪');
INSERT INTO `customer_state` VALUES ('4', '意向客户');
INSERT INTO `customer_state` VALUES ('5', '上门');
INSERT INTO `customer_state` VALUES ('6', '已签单');
INSERT INTO `customer_state` VALUES ('7', '已放款');
INSERT INTO `customer_state` VALUES ('8', '已结账');
INSERT INTO `customer_state` VALUES ('9', '退单');
INSERT INTO `customer_state` VALUES ('10', '退单后可跟踪');
INSERT INTO `customer_state` VALUES ('11', '未接');
INSERT INTO `customer_state` VALUES ('12', '近一个月需要');
INSERT INTO `customer_state` VALUES ('13', '预计上门');
INSERT INTO `customer_state` VALUES ('14', '近三个月需要');

-- ----------------------------
-- Table structure for `login_record`
-- ----------------------------
DROP TABLE IF EXISTS `login_record`;
CREATE TABLE `login_record` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_phone` varchar(11) NOT NULL,
  `user_name` varchar(20) NOT NULL,
  `up_down_id` varchar(50) NOT NULL,
  `dept_name` varchar(20) NOT NULL,
  `login_time` datetime NOT NULL,
  `login_ip` varchar(25) NOT NULL,
  `remark` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=138 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of login_record
-- ----------------------------
INSERT INTO `login_record` VALUES ('4', '18782233226', '邓红', '1000-1001-1002-1006-', '1分中心营销1部', '2017-08-24 10:32:07', '0:0:0:0:0:0:0:1', null);
INSERT INTO `login_record` VALUES ('5', '18782233226', '邓红', '1000-1001-1002-1006-', '1分中心营销1部', '2017-08-24 10:35:01', '0:0:0:0:0:0:0:1', null);
INSERT INTO `login_record` VALUES ('6', '18723477723', '超哥', '1000-1001-1002-', '营销1分中心', '2017-08-25 12:56:17', '0:0:0:0:0:0:0:1', null);
INSERT INTO `login_record` VALUES ('7', '18782233226', '邓红', '1000-1001-1002-1006-', '1分中心营销1部', '2017-08-25 13:02:24', '0:0:0:0:0:0:0:1', null);
INSERT INTO `login_record` VALUES ('8', '18782233226', '邓红', '1000-1001-1002-1006-', '1分中心营销1部', '2017-08-25 13:03:54', '0:0:0:0:0:0:0:1', null);
INSERT INTO `login_record` VALUES ('9', '18782233226', '邓红', '1000-1001-1002-1006-', '1分中心营销1部', '2017-08-29 14:54:46', '0:0:0:0:0:0:0:1', null);
INSERT INTO `login_record` VALUES ('10', '18782233226', '邓红', '1000-1001-1002-1006-', '1分中心营销1部', '2017-08-31 16:18:03', '0:0:0:0:0:0:0:1', null);
INSERT INTO `login_record` VALUES ('11', '18782233226', '邓红', '1000-1001-1002-1006-', '1分中心营销1部', '2017-08-31 17:16:13', '0:0:0:0:0:0:0:1', null);
INSERT INTO `login_record` VALUES ('12', '18782233226', '邓红', '1000-1001-1002-1006-', '1分中心营销1部', '2017-08-31 17:22:37', '0:0:0:0:0:0:0:1', null);
INSERT INTO `login_record` VALUES ('13', '18782233226', '邓红', '1000-1001-1002-1006-', '1分中心营销1部', '2017-08-31 17:27:59', '0:0:0:0:0:0:0:1', null);
INSERT INTO `login_record` VALUES ('14', '18782233226', '邓红', '1000-1001-1002-1006-', '1分中心营销1部', '2017-08-31 17:32:43', '0:0:0:0:0:0:0:1', null);
INSERT INTO `login_record` VALUES ('15', '18782233226', '邓红', '1000-1001-1002-1006-', '1分中心营销1部', '2017-09-01 14:57:25', '0:0:0:0:0:0:0:1', null);
INSERT INTO `login_record` VALUES ('16', '18782233226', '邓红', '1000-1001-1002-1006-', '1分中心营销1部', '2017-09-01 14:58:50', '192.168.3.64', null);
INSERT INTO `login_record` VALUES ('17', '18782233226', '邓红', '1000-1001-1002-1006-', '1分中心营销1部', '2017-09-01 15:02:47', '0:0:0:0:0:0:0:1', null);
INSERT INTO `login_record` VALUES ('18', '18782233226', '邓红', '1000-1001-1002-1006-', '1分中心营销1部', '2017-09-01 15:03:06', '192.168.3.64', null);
INSERT INTO `login_record` VALUES ('19', '18782233226', '邓红', '1000-1001-1002-1006-', '1分中心营销1部', '2017-09-01 15:06:08', '0:0:0:0:0:0:0:1', null);
INSERT INTO `login_record` VALUES ('20', '18782233226', '邓红', '1000-1001-1002-1006-', '1分中心营销1部', '2017-09-01 15:06:56', '0:0:0:0:0:0:0:1', null);
INSERT INTO `login_record` VALUES ('21', '18782233226', '邓红', '1000-1001-1002-1006-', '1分中心营销1部', '2017-09-01 15:07:31', '192.168.3.64', null);
INSERT INTO `login_record` VALUES ('22', '18782233226', '邓红', '1000-1001-1002-1006-', '1分中心营销1部', '2017-09-01 15:08:33', '192.168.3.64', null);
INSERT INTO `login_record` VALUES ('23', '18782404805', '系统管理员1', '1000-1009-', 'IT运维部', '2017-09-01 15:10:36', '192.168.3.64', null);
INSERT INTO `login_record` VALUES ('24', '18782233226', '邓红', '1000-1001-1002-1006-', '1分中心营销1部', '2017-09-11 14:23:59', '0:0:0:0:0:0:0:1', null);
INSERT INTO `login_record` VALUES ('25', '18782233226', '邓红', '1000-1001-1002-1006-', '1分中心营销1部', '2017-09-11 14:24:49', '0:0:0:0:0:0:0:1', null);
INSERT INTO `login_record` VALUES ('26', '18782404805', '系统管理员1', '1000-1009-', 'IT运维部', '2017-09-11 14:25:40', '0:0:0:0:0:0:0:1', null);
INSERT INTO `login_record` VALUES ('27', '18782233226', '邓红', '1000-1001-1002-1006-', '1分中心营销1部', '2017-09-11 14:26:11', '0:0:0:0:0:0:0:1', null);
INSERT INTO `login_record` VALUES ('28', '18782404805', '系统管理员1', '1000-1009-', 'IT运维部', '2017-09-11 14:26:55', '0:0:0:0:0:0:0:1', null);
INSERT INTO `login_record` VALUES ('29', '18782233226', '邓红', '1000-1001-1002-1006-', '1分中心营销1部', '2017-09-11 14:28:43', '0:0:0:0:0:0:0:1', null);
INSERT INTO `login_record` VALUES ('30', '18782233226', '邓红', '1000-1001-1002-1006-', '1分中心营销1部', '2017-09-11 14:39:09', '0:0:0:0:0:0:0:1', null);
INSERT INTO `login_record` VALUES ('31', '18782233226', '邓红', '1000-1001-1002-1006-', '1分中心营销1部', '2017-09-11 17:02:35', '0:0:0:0:0:0:0:1', null);
INSERT INTO `login_record` VALUES ('32', '18242342344', '张怡', '1000-1001-1002-1006-', '1分中心营销2部', '2017-09-11 17:06:28', '0:0:0:0:0:0:0:1', null);
INSERT INTO `login_record` VALUES ('33', '18723477723', '超哥', '1000-1001-1002-', '营销1分中心', '2017-09-11 17:06:55', '0:0:0:0:0:0:0:1', null);
INSERT INTO `login_record` VALUES ('34', '18782233226', '邓红', '1000-1001-1002-1006-', '1分中心营销1部', '2017-09-11 17:07:15', '0:0:0:0:0:0:0:1', null);
INSERT INTO `login_record` VALUES ('35', '18723477723', '超哥', '1000-1001-1002-', '营销1分中心', '2017-09-11 17:14:59', '0:0:0:0:0:0:0:1', null);
INSERT INTO `login_record` VALUES ('36', '18782233226', '邓红', '1000-1001-1002-1006-', '1分中心营销1部', '2017-09-11 17:17:29', '0:0:0:0:0:0:0:1', null);
INSERT INTO `login_record` VALUES ('37', '18723477723', '超哥', '1000-1001-1002-', '营销1分中心', '2017-09-11 17:19:36', '0:0:0:0:0:0:0:1', null);
INSERT INTO `login_record` VALUES ('38', '18782233226', '邓红', '1000-1001-1002-1006-', '1分中心营销1部', '2017-09-11 17:19:57', '0:0:0:0:0:0:0:1', null);
INSERT INTO `login_record` VALUES ('39', '18782233226', '邓红', '1000-1001-1002-1006-', '1分中心营销1部', '2017-09-11 17:47:03', '0:0:0:0:0:0:0:1', null);
INSERT INTO `login_record` VALUES ('40', '18782233226', '邓红', '1000-1001-1002-1006-', '1分中心营销1部', '2017-09-12 09:09:25', '0:0:0:0:0:0:0:1', null);
INSERT INTO `login_record` VALUES ('41', '18782233226', '邓红', '1000-1001-1002-1006-', '1分中心营销1部', '2017-09-12 10:03:08', '0:0:0:0:0:0:0:1', null);
INSERT INTO `login_record` VALUES ('42', '18782233226', '邓红', '1000-1001-1002-1006-', '1分中心营销1部', '2017-09-12 14:38:02', '0:0:0:0:0:0:0:1', null);
INSERT INTO `login_record` VALUES ('43', '18782233226', '邓红', '1000-1001-1002-1006-', '1分中心营销1部', '2017-09-12 18:08:09', '0:0:0:0:0:0:0:1', null);
INSERT INTO `login_record` VALUES ('44', '18723477723', '超哥', '1000-1001-1002-', '营销1分中心', '2017-09-12 18:08:41', '0:0:0:0:0:0:0:1', null);
INSERT INTO `login_record` VALUES ('45', '18723477723', '超哥', '1000-1001-1002-', '营销1分中心', '2017-09-12 18:12:45', '0:0:0:0:0:0:0:1', null);
INSERT INTO `login_record` VALUES ('46', '18782233226', '邓红', '1000-1001-1002-1006-', '1分中心营销1部', '2017-09-13 11:39:05', '0:0:0:0:0:0:0:1', null);
INSERT INTO `login_record` VALUES ('47', '18782233226', '邓红', '1000-1001-1002-1006-', '1分中心营销1部', '2017-09-13 11:46:20', '0:0:0:0:0:0:0:1', null);
INSERT INTO `login_record` VALUES ('48', '18782233226', '邓红', '1000-1001-1002-1006-', '1分中心营销1部', '2017-09-13 11:50:06', '0:0:0:0:0:0:0:1', null);
INSERT INTO `login_record` VALUES ('49', '18782233226', '邓红', '1000-1001-1002-1006-', '1分中心营销1部', '2017-09-13 12:42:15', '0:0:0:0:0:0:0:1', null);
INSERT INTO `login_record` VALUES ('50', '18782233226', '邓红', '1000-1001-1002-1006-', '1分中心营销1部', '2017-09-13 12:48:52', '0:0:0:0:0:0:0:1', null);
INSERT INTO `login_record` VALUES ('51', '18782233226', '邓红', '1000-1001-1002-1006-', '1分中心营销1部', '2017-09-13 12:56:14', '0:0:0:0:0:0:0:1', null);
INSERT INTO `login_record` VALUES ('52', '18782233226', '邓红', '1000-1001-1002-1006-', '1分中心营销1部', '2017-09-13 13:05:10', '0:0:0:0:0:0:0:1', null);
INSERT INTO `login_record` VALUES ('53', '18782233226', '邓红', '1000-1001-1002-1006-', '1分中心营销1部', '2017-09-13 17:28:31', '0:0:0:0:0:0:0:1', null);
INSERT INTO `login_record` VALUES ('54', '18782233226', '邓红', '1000-1001-1002-1006-', '1分中心营销1部', '2017-09-13 17:33:25', '0:0:0:0:0:0:0:1', null);
INSERT INTO `login_record` VALUES ('55', '18782233226', '邓红', '1000-1001-1002-1006-', '1分中心营销1部', '2017-09-13 19:12:39', '0:0:0:0:0:0:0:1', null);
INSERT INTO `login_record` VALUES ('56', '18782233226', '邓红', '1000-1001-1002-1006-', '1分中心营销1部', '2017-09-13 19:16:51', '0:0:0:0:0:0:0:1', null);
INSERT INTO `login_record` VALUES ('57', '18782233226', '邓红', '1000-1001-1002-1006-', '1分中心营销1部', '2017-09-13 19:19:10', '0:0:0:0:0:0:0:1', null);
INSERT INTO `login_record` VALUES ('58', '18782233226', '邓红', '1000-1001-1002-1006-', '1分中心营销1部', '2017-09-13 19:38:15', '0:0:0:0:0:0:0:1', null);
INSERT INTO `login_record` VALUES ('59', '18782233226', '邓红', '1000-1001-1002-1006-', '1分中心营销1部', '2017-09-13 22:29:40', '0:0:0:0:0:0:0:1', null);
INSERT INTO `login_record` VALUES ('60', '18723477723', '超哥', '1000-1001-1002-', '营销1分中心', '2017-09-13 22:30:12', '0:0:0:0:0:0:0:1', null);
INSERT INTO `login_record` VALUES ('61', '18723477723', '超哥', '1000-1001-1002-', '营销1分中心', '2017-09-19 12:48:31', '0:0:0:0:0:0:0:1', null);
INSERT INTO `login_record` VALUES ('62', '18723477723', '超哥', '1000-1001-1002-', '营销1分中心', '2017-09-20 10:17:07', '0:0:0:0:0:0:0:1', null);
INSERT INTO `login_record` VALUES ('63', '18723477723', '超哥', '1000-1001-1002-', '营销1分中心', '2017-09-21 10:25:36', '0:0:0:0:0:0:0:1', null);
INSERT INTO `login_record` VALUES ('64', '18782404805', '系统管理员1', '1000-1009-', 'IT运维部', '2017-09-21 10:25:57', '0:0:0:0:0:0:0:1', null);
INSERT INTO `login_record` VALUES ('65', '18782404805', '系统管理员1', '1000-1009-', 'IT运维部', '2017-09-21 10:34:02', '0:0:0:0:0:0:0:1', null);
INSERT INTO `login_record` VALUES ('66', '18782404805', '系统管理员1', '1000-1009-', 'IT运维部', '2017-09-21 10:36:06', '0:0:0:0:0:0:0:1', null);
INSERT INTO `login_record` VALUES ('67', '18723477723', '超哥', '1000-1001-1002-', '营销1分中心', '2017-09-21 10:44:42', '0:0:0:0:0:0:0:1', null);
INSERT INTO `login_record` VALUES ('68', '18782404805', '系统管理员1', '1000-1009-', 'IT运维部', '2017-09-21 10:45:13', '0:0:0:0:0:0:0:1', null);
INSERT INTO `login_record` VALUES ('69', '18723477723', '超哥', '1000-1001-1002-', '营销1分中心', '2017-09-21 10:45:32', '0:0:0:0:0:0:0:1', null);
INSERT INTO `login_record` VALUES ('70', '18782404805', '系统管理员1', '1000-1009-', 'IT运维部', '2017-09-21 10:45:46', '0:0:0:0:0:0:0:1', null);
INSERT INTO `login_record` VALUES ('71', '18723477723', '超哥', '1000-1001-1002-', '营销1分中心', '2017-09-21 10:45:57', '0:0:0:0:0:0:0:1', null);
INSERT INTO `login_record` VALUES ('72', '18782404805', '系统管理员1', '1000-1009-', 'IT运维部', '2017-09-21 10:54:14', '0:0:0:0:0:0:0:1', null);
INSERT INTO `login_record` VALUES ('73', '18782233226', '邓红', '1000-1001-1002-1006-', '1分中心营销1部', '2017-09-21 10:58:51', '0:0:0:0:0:0:0:1', null);
INSERT INTO `login_record` VALUES ('74', '18723477723', '超哥', '1000-1001-1002-', '营销1分中心', '2017-09-21 10:58:59', '0:0:0:0:0:0:0:1', null);
INSERT INTO `login_record` VALUES ('75', '18782404805', '系统管理员1', '1000-1009-', 'IT运维部', '2017-09-21 11:00:19', '0:0:0:0:0:0:0:1', null);
INSERT INTO `login_record` VALUES ('76', '18782404805', '系统管理员1', '1000-1009-', 'IT运维部', '2017-09-21 11:02:10', '0:0:0:0:0:0:0:1', null);
INSERT INTO `login_record` VALUES ('77', '18723477723', '超哥', '1000-1001-1002-', '营销1分中心', '2017-09-21 11:02:22', '0:0:0:0:0:0:0:1', null);
INSERT INTO `login_record` VALUES ('78', '18723477723', '超哥', '1000-1001-1002-', '营销1分中心', '2017-09-21 11:08:18', '0:0:0:0:0:0:0:1', null);
INSERT INTO `login_record` VALUES ('79', '18723477723', '超哥', '1000-1001-1002-', '营销1分中心', '2017-09-21 11:11:15', '0:0:0:0:0:0:0:1', null);
INSERT INTO `login_record` VALUES ('80', '18723477723', '超哥', '1000-1001-1002-', '营销1分中心', '2017-09-21 11:37:53', '0:0:0:0:0:0:0:1', null);
INSERT INTO `login_record` VALUES ('81', '18242342344', '张怡', '1000-1001-1002-1006-', '1分中心营销2部', '2017-09-21 11:59:26', '0:0:0:0:0:0:0:1', null);
INSERT INTO `login_record` VALUES ('82', '18782404805', '系统管理员1', '1000-1009-', 'IT运维部', '2017-09-21 11:59:42', '0:0:0:0:0:0:0:1', null);
INSERT INTO `login_record` VALUES ('83', '18242342344', '张怡', '1000-1001-1002-1006-', '1分中心营销2部', '2017-09-21 12:00:02', '0:0:0:0:0:0:0:1', null);
INSERT INTO `login_record` VALUES ('84', '18242342344', '张怡', '1000-1001-1002-1006-', '1分中心营销2部', '2017-09-21 12:09:18', '0:0:0:0:0:0:0:1', null);
INSERT INTO `login_record` VALUES ('85', '18782404805', '系统管理员1', '1000-1009-', 'IT运维部', '2017-09-21 12:10:01', '0:0:0:0:0:0:0:1', null);
INSERT INTO `login_record` VALUES ('86', '18242342344', '张怡', '1000-1001-1002-1006-', '1分中心营销2部', '2017-09-21 12:10:16', '0:0:0:0:0:0:0:1', null);
INSERT INTO `login_record` VALUES ('87', '18242342344', '张怡', '1000-1001-1002-1006-', '1分中心营销2部', '2017-09-21 12:39:49', '0:0:0:0:0:0:0:1', null);
INSERT INTO `login_record` VALUES ('88', '18723477723', '超哥', '1000-1001-1002-', '营销1分中心', '2017-09-21 12:40:07', '0:0:0:0:0:0:0:1', null);
INSERT INTO `login_record` VALUES ('89', '18242342344', '张怡', '1000-1001-1002-1006-', '1分中心营销2部', '2017-09-21 12:40:30', '0:0:0:0:0:0:0:1', null);
INSERT INTO `login_record` VALUES ('90', '18723477723', '超哥', '1000-1001-1002-', '营销1分中心', '2017-09-21 12:40:41', '0:0:0:0:0:0:0:1', null);
INSERT INTO `login_record` VALUES ('91', '18242342344', '张怡', '1000-1001-1002-1006-', '1分中心营销2部', '2017-09-21 12:40:58', '0:0:0:0:0:0:0:1', null);
INSERT INTO `login_record` VALUES ('92', '18782233226', '邓红', '1000-1001-1002-1006-', '1分中心营销1部', '2017-09-21 12:41:06', '0:0:0:0:0:0:0:1', null);
INSERT INTO `login_record` VALUES ('93', '18242342344', '张怡', '1000-1001-1002-1006-', '1分中心营销2部', '2017-09-21 12:41:18', '0:0:0:0:0:0:0:1', null);
INSERT INTO `login_record` VALUES ('94', '15677883343', '权证主管', '1000-1001-1010-', '权证中心', '2017-09-21 14:53:20', '0:0:0:0:0:0:0:1', null);
INSERT INTO `login_record` VALUES ('95', '18242342344', '张怡', '1000-1001-1002-1006-', '1分中心营销2部', '2017-09-21 15:17:41', '0:0:0:0:0:0:0:1', null);
INSERT INTO `login_record` VALUES ('96', '18723477723', '超哥', '1000-1001-1002-', '营销1分中心', '2017-09-21 15:50:49', '0:0:0:0:0:0:0:1', null);
INSERT INTO `login_record` VALUES ('97', '18242342344', '张怡', '1000-1001-1002-1006-', '1分中心营销2部', '2017-09-21 15:50:58', '0:0:0:0:0:0:0:1', null);
INSERT INTO `login_record` VALUES ('98', '18242342344', '张怡', '1000-1001-1002-1006-', '1分中心营销2部', '2017-09-21 15:51:21', '0:0:0:0:0:0:0:1', null);
INSERT INTO `login_record` VALUES ('99', '18242342344', '张怡', '1000-1001-1002-1006-', '1分中心营销2部', '2017-09-21 15:51:28', '0:0:0:0:0:0:0:1', null);
INSERT INTO `login_record` VALUES ('100', '18242342344', '张怡', '1000-1001-1002-1006-', '1分中心营销2部', '2017-09-21 15:51:33', '0:0:0:0:0:0:0:1', null);
INSERT INTO `login_record` VALUES ('101', '18723477723', '超哥', '1000-1001-1002-', '营销1分中心', '2017-09-22 21:04:34', '0:0:0:0:0:0:0:1', null);
INSERT INTO `login_record` VALUES ('102', '18242342344', '张怡', '1000-1001-1002-1006-', '1分中心营销2部', '2017-09-22 21:04:39', '0:0:0:0:0:0:0:1', null);
INSERT INTO `login_record` VALUES ('103', '18723477723', '超哥', '1000-1001-1002-', '营销1分中心', '2017-09-22 21:04:50', '0:0:0:0:0:0:0:1', null);
INSERT INTO `login_record` VALUES ('104', '18242342344', '张怡', '1000-1001-1002-1006-', '1分中心营销2部', '2017-09-22 21:06:09', '0:0:0:0:0:0:0:1', null);
INSERT INTO `login_record` VALUES ('105', '18782233226', '邓红', '1000-1001-1002-1006-', '1分中心营销1部', '2017-09-22 21:09:10', '0:0:0:0:0:0:0:1', null);
INSERT INTO `login_record` VALUES ('106', '18242342344', '张怡', '1000-1001-1002-1006-', '1分中心营销2部', '2017-09-22 21:09:23', '0:0:0:0:0:0:0:1', null);
INSERT INTO `login_record` VALUES ('107', '18242342344', '张怡', '1000-1001-1002-1006-', '1分中心营销2部', '2017-09-23 12:01:55', '0:0:0:0:0:0:0:1', null);
INSERT INTO `login_record` VALUES ('108', '18782233226', '邓红', '1000-1001-1002-1006-', '1分中心营销1部', '2017-09-23 12:03:49', '0:0:0:0:0:0:0:1', null);
INSERT INTO `login_record` VALUES ('109', '18782233226', '邓红', '1000-1001-1002-1006-', '1分中心营销1部', '2017-09-23 12:10:22', '0:0:0:0:0:0:0:1', null);
INSERT INTO `login_record` VALUES ('110', '18782233226', '邓红', '1000-1001-1002-1006-', '1分中心营销1部', '2017-09-23 12:14:12', '0:0:0:0:0:0:0:1', null);
INSERT INTO `login_record` VALUES ('111', '18242342344', '张怡', '1000-1001-1002-1006-', '1分中心营销2部', '2017-09-23 12:14:25', '0:0:0:0:0:0:0:1', null);
INSERT INTO `login_record` VALUES ('112', '18242342344', '张怡', '1000-1001-1002-1006-', '1分中心营销2部', '2017-09-23 12:26:12', '0:0:0:0:0:0:0:1', null);
INSERT INTO `login_record` VALUES ('113', '18242342344', '张怡', '1000-1001-1002-1006-', '1分中心营销2部', '2017-09-25 13:40:09', '0:0:0:0:0:0:0:1', null);
INSERT INTO `login_record` VALUES ('114', '18723477723', '超哥', '1000-1001-1002-', '营销1分中心', '2017-09-25 13:40:14', '0:0:0:0:0:0:0:1', null);
INSERT INTO `login_record` VALUES ('115', '18782404805', '系统管理员1', '1000-1009-', 'IT运维部', '2017-09-25 13:49:29', '0:0:0:0:0:0:0:1', null);
INSERT INTO `login_record` VALUES ('116', '18242342344', '张怡', '1000-1001-1002-1006-', '1分中心营销2部', '2017-09-25 13:57:05', '0:0:0:0:0:0:0:1', null);
INSERT INTO `login_record` VALUES ('117', '18242342344', '张怡', '1000-1001-1002-1006-', '1分中心营销2部', '2017-09-26 18:00:15', '0:0:0:0:0:0:0:1', null);
INSERT INTO `login_record` VALUES ('118', '18723477723', '超哥', '1000-1001-1002-', '营销1分中心', '2017-09-26 18:03:09', '0:0:0:0:0:0:0:1', null);
INSERT INTO `login_record` VALUES ('119', '18723477723', '超哥', '1000-1001-1002-', '营销1分中心', '2017-09-26 18:05:19', '0:0:0:0:0:0:0:1', null);
INSERT INTO `login_record` VALUES ('120', '18723477723', '超哥', '1000-1001-1002-', '营销1分中心', '2017-09-26 18:08:37', '0:0:0:0:0:0:0:1', null);
INSERT INTO `login_record` VALUES ('121', '18723477723', '超哥', '1000-1001-1002-', '营销1分中心', '2017-09-27 13:50:15', '0:0:0:0:0:0:0:1', null);
INSERT INTO `login_record` VALUES ('122', '18782404805', '系统管理员1', '1000-1009-', 'IT运维部', '2017-09-27 13:50:22', '0:0:0:0:0:0:0:1', null);
INSERT INTO `login_record` VALUES ('123', '18723477723', '超哥', '1000-1001-1002-', '营销1分中心', '2017-09-28 17:08:34', '0:0:0:0:0:0:0:1', null);
INSERT INTO `login_record` VALUES ('124', '18723477723', '超哥', '1000-1001-1002-', '营销1分中心', '2017-09-28 17:15:39', '0:0:0:0:0:0:0:1', null);
INSERT INTO `login_record` VALUES ('125', '18723477723', '超哥', '1000-1001-1002-', '营销1分中心', '2017-09-28 17:20:20', '0:0:0:0:0:0:0:1', null);
INSERT INTO `login_record` VALUES ('126', '18242342344', '张怡', '1000-1001-1002-1006-', '1分中心营销2部', '2017-09-29 11:43:34', '0:0:0:0:0:0:0:1', null);
INSERT INTO `login_record` VALUES ('127', '18242342344', '张怡', '1000-1001-1002-1006-', '1分中心营销2部', '2017-09-29 12:01:02', '0:0:0:0:0:0:0:1', null);
INSERT INTO `login_record` VALUES ('128', '18242342344', '张怡', '1000-1001-1002-1006-', '1分中心营销2部', '2017-09-29 14:59:16', '0:0:0:0:0:0:0:1', null);
INSERT INTO `login_record` VALUES ('129', '18782233226', '邓红', '1000-1001-1002-1006-', '1分中心营销1部', '2017-09-29 15:21:55', '0:0:0:0:0:0:0:1', null);
INSERT INTO `login_record` VALUES ('130', '18782233226', '邓红', '1000-1001-1002-1006-', '1分中心营销1部', '2017-09-29 15:32:51', '0:0:0:0:0:0:0:1', null);
INSERT INTO `login_record` VALUES ('131', '18782233226', '邓红', '1000-1001-1002-1006-', '1分中心营销1部', '2017-10-09 18:20:18', '0:0:0:0:0:0:0:1', null);
INSERT INTO `login_record` VALUES ('132', '18782233226', '邓红', '1000-1001-1002-1006-', '1分中心营销1部', '2017-10-09 18:34:54', '0:0:0:0:0:0:0:1', null);
INSERT INTO `login_record` VALUES ('133', '18723477723', '超哥', '1000-1001-1002-', '营销1分中心', '2017-10-30 17:58:33', '0:0:0:0:0:0:0:1', null);
INSERT INTO `login_record` VALUES ('134', '18242342344', '张怡', '1000-1001-1002-1006-', '1分中心营销2部', '2017-10-31 10:15:47', '0:0:0:0:0:0:0:1', null);
INSERT INTO `login_record` VALUES ('135', '18782404805', '系统管理员1', '1000-1009-', 'IT运维部', '2017-10-31 10:16:51', '0:0:0:0:0:0:0:1', null);
INSERT INTO `login_record` VALUES ('136', '13423433322', '主管2', '1000-1001-1002-1007-', '1分中心营销2部', '2017-10-31 10:17:57', '0:0:0:0:0:0:0:1', null);
INSERT INTO `login_record` VALUES ('137', '18782233226', '邓红', '1000-1001-1002-1006-', '1分中心营销1部', '2017-10-31 10:18:30', '0:0:0:0:0:0:0:1', null);

-- ----------------------------
-- Table structure for `pt_dept`
-- ----------------------------
DROP TABLE IF EXISTS `pt_dept`;
CREATE TABLE `pt_dept` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `dept_name` varchar(20) NOT NULL,
  `parent_id` int(11) NOT NULL,
  `up_down_id` varchar(50) NOT NULL DEFAULT '',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1018 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of pt_dept
-- ----------------------------
INSERT INTO `pt_dept` VALUES ('1000', '集团最高层', '1000', '1000-');
INSERT INTO `pt_dept` VALUES ('1001', '总体营运部', '1000', '1000-1001-');
INSERT INTO `pt_dept` VALUES ('1002', '营销1分中心', '1001', '1000-1001-1002-');
INSERT INTO `pt_dept` VALUES ('1003', '营销2分中心', '1001', '1000-1001-1003-');
INSERT INTO `pt_dept` VALUES ('1004', '营销3分中心', '1001', '1000-1001-1004-');
INSERT INTO `pt_dept` VALUES ('1005', '营销4分中心', '1001', '1000-1001-1005-');
INSERT INTO `pt_dept` VALUES ('1006', '1分中心营销1部', '1002', '1000-1001-1002-1006-');
INSERT INTO `pt_dept` VALUES ('1007', '1分中心营销2部', '1002', '1000-1001-1002-1007-');
INSERT INTO `pt_dept` VALUES ('1008', '1分中心营销3部', '1002', '1000-1001-1002-1008-');
INSERT INTO `pt_dept` VALUES ('1009', 'IT运维部', '1000', '1000-1009-');
INSERT INTO `pt_dept` VALUES ('1010', '权证中心', '1001', '1000-1001-1010');
INSERT INTO `pt_dept` VALUES ('1011', '1分中心营销4部', '1002', '1000-1001-1002-1011-');
INSERT INTO `pt_dept` VALUES ('1012', '2分中心营销1部', '1003', '1000-1001-1003-1012-');
INSERT INTO `pt_dept` VALUES ('1013', '2分中心营销2部', '1003', '1000-1001-1003-1013-');
INSERT INTO `pt_dept` VALUES ('1014', '2分中心营销3部', '1003', '1000-1001-1003-1014-');
INSERT INTO `pt_dept` VALUES ('1015', '2分中心营销4部', '1003', '1000-1001-1003-1015-');
INSERT INTO `pt_dept` VALUES ('1017', '贷后部', '1001', '1001-1017-');

-- ----------------------------
-- Table structure for `pt_role`
-- ----------------------------
DROP TABLE IF EXISTS `pt_role`;
CREATE TABLE `pt_role` (
  `role_code` varchar(20) NOT NULL,
  `role_name` varchar(20) NOT NULL,
  PRIMARY KEY (`role_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of pt_role
-- ----------------------------
INSERT INTO `pt_role` VALUES ('ADMIN', '管理员');
INSERT INTO `pt_role` VALUES ('BackLineLeader', '权证主管');
INSERT INTO `pt_role` VALUES ('BackLiner', '权证组员');
INSERT INTO `pt_role` VALUES ('CEO', '集团总裁');
INSERT INTO `pt_role` VALUES ('COO', '首席营运官');
INSERT INTO `pt_role` VALUES ('CSO', '销售总监');
INSERT INTO `pt_role` VALUES ('CustomerService', '销售客服');
INSERT INTO `pt_role` VALUES ('SaleBacker', '贷后组员');
INSERT INTO `pt_role` VALUES ('SaleBackLeader', '贷后主管');
INSERT INTO `pt_role` VALUES ('TeamLeader', '销售团队主管');

-- ----------------------------
-- Table structure for `pt_user`
-- ----------------------------
DROP TABLE IF EXISTS `pt_user`;
CREATE TABLE `pt_user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `phone` varchar(11) NOT NULL,
  `user_pwd` varchar(32) NOT NULL,
  `role_code` varchar(20) NOT NULL,
  `dept_id` int(11) NOT NULL,
  `real_name` varchar(20) NOT NULL,
  `pic_url` varchar(200) NOT NULL DEFAULT '',
  `sex` bit(1) NOT NULL DEFAULT b'1' COMMENT '1男 0女',
  `add_time` datetime NOT NULL,
  `login_time` datetime DEFAULT NULL,
  `state` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0在职 1离职',
  `up_down_id` varchar(50) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `phone` (`phone`),
  KEY `role_code` (`role_code`),
  KEY `dept_id` (`dept_id`),
  KEY `state` (`state`),
  KEY `up_down_id` (`up_down_id`),
  CONSTRAINT `pt_user_ibfk_1` FOREIGN KEY (`role_code`) REFERENCES `pt_role` (`role_code`),
  CONSTRAINT `pt_user_ibfk_2` FOREIGN KEY (`dept_id`) REFERENCES `pt_dept` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of pt_user
-- ----------------------------
INSERT INTO `pt_user` VALUES ('1', '18242342344', 'E10ADC3949BA59ABBE56E057F20F883E', 'CustomerService', '1007', '张怡', 'default_header.png', '', '2017-05-25 10:17:35', '2017-10-31 10:15:47', '0', '1000-1001-1002-1007-');
INSERT INTO `pt_user` VALUES ('2', '18723477723', 'E10ADC3949BA59ABBE56E057F20F883E', 'CSO', '1002', '超哥', 'dd87f445-d70a-4f2e-a5b8-f2e74d7a37de.jpg', '', '2017-05-25 10:21:38', '2017-10-30 17:58:33', '0', '1000-1001-1002-');
INSERT INTO `pt_user` VALUES ('3', '18782233226', 'E10ADC3949BA59ABBE56E057F20F883E', 'TeamLeader', '1006', '邓红', 'default_header.png', '', '2017-05-25 10:19:45', '2017-10-31 10:18:30', '0', '1000-1001-1002-1006-');
INSERT INTO `pt_user` VALUES ('4', '18782404805', 'E10ADC3949BA59ABBE56E057F20F883E', 'ADMIN', '1009', '系统管理员1', 'default_header.png', '', '2017-05-24 09:55:09', '2017-10-31 10:16:51', '0', '1000-1009-');
INSERT INTO `pt_user` VALUES ('5', '13423433322', 'E10ADC3949BA59ABBE56E057F20F883E', 'TeamLeader', '1007', '主管2', 'default_header.png', '', '2017-06-02 09:57:13', '2017-10-31 10:17:57', '0', '1000-1001-1002-1007-');
INSERT INTO `pt_user` VALUES ('6', '15677883343', 'E10ADC3949BA59ABBE56E057F20F883E', 'BackLineLeader', '1010', '权证主管', 'default_header.png', '', '2017-06-05 12:07:48', '2017-09-21 14:53:20', '0', '1000-1001-1010-');
INSERT INTO `pt_user` VALUES ('7', '15677886654', 'E10ADC3949BA59ABBE56E057F20F883E', 'BackLiner', '1010', '权证组员1', 'default_header.png', '', '2017-06-05 14:09:33', '2017-07-31 17:00:25', '0', '1000-1001-1010-');
INSERT INTO `pt_user` VALUES ('8', '13987662342', 'E10ADC3949BA59ABBE56E057F20F883E', 'CustomerService', '1006', '李三', 'default_header.png', '', '2017-07-16 11:30:15', '2017-07-18 09:32:22', '0', '1000-1001-1002-1006-');
INSERT INTO `pt_user` VALUES ('9', '12342342344', 'E10ADC3949BA59ABBE56E057F20F883E', 'CEO', '1000', '123', 'default_header.png', '', '2017-07-17 13:09:27', null, '0', '1000-');
INSERT INTO `pt_user` VALUES ('10', '12363453453', 'E10ADC3949BA59ABBE56E057F20F883E', 'COO', '1001', '233', 'default_header.png', '', '2017-07-17 13:09:51', null, '0', '1000-1001-');
INSERT INTO `pt_user` VALUES ('11', '13567344252', 'E10ADC3949BA59ABBE56E057F20F883E', 'SaleBacker', '1017', '贷后人1', 'default_header.png', '', '2017-07-26 15:04:07', '2017-07-31 17:04:38', '0', '1001-1017-');

-- ----------------------------
-- Table structure for `remind`
-- ----------------------------
DROP TABLE IF EXISTS `remind`;
CREATE TABLE `remind` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `time_beg` datetime NOT NULL,
  `time_end` datetime NOT NULL,
  `topic` varchar(100) NOT NULL,
  `context` varchar(800) NOT NULL,
  `user_name` varchar(20) NOT NULL,
  `user_id` varchar(20) NOT NULL,
  `cus_phone` varchar(11) DEFAULT NULL,
  `cus_name` varchar(20) DEFAULT NULL,
  `up_down_id` varchar(50) NOT NULL,
  `remind_type` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0:对自己  1：对自己和下属',
  `state` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0:正常  1：关闭',
  `add_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `time_beg` (`time_beg`),
  KEY `time_end` (`time_end`),
  KEY `user_id` (`user_id`),
  KEY `up_down_id` (`up_down_id`),
  KEY `state` (`state`),
  KEY `remind_type` (`remind_type`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of remind
-- ----------------------------

-- ----------------------------
-- Table structure for `remindstate`
-- ----------------------------
DROP TABLE IF EXISTS `remindstate`;
CREATE TABLE `remindstate` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `remind_id` int(11) NOT NULL,
  `user_id` varchar(20) NOT NULL,
  `state` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0:未读   1：已读',
  `add_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `remind_id` (`remind_id`),
  KEY `user_id` (`user_id`),
  KEY `state` (`state`),
  KEY `remind_id_2` (`remind_id`,`user_id`,`state`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of remindstate
-- ----------------------------

-- ----------------------------
-- Table structure for `soundreport`
-- ----------------------------
DROP TABLE IF EXISTS `soundreport`;
CREATE TABLE `soundreport` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `dateStr` date NOT NULL,
  `dept_name` varchar(50) NOT NULL,
  `user_phone` varchar(11) NOT NULL,
  `user_name` varchar(20) NOT NULL,
  `up_down_id` varchar(50) NOT NULL,
  `direction` varchar(8) NOT NULL,
  `num` int(11) NOT NULL COMMENT '数量',
  `totalsec` int(11) NOT NULL,
  `addtime` datetime NOT NULL,
  PRIMARY KEY (`id`),
  KEY `up_down_id` (`up_down_id`),
  KEY `addtime` (`addtime`),
  KEY `state` (`num`),
  KEY `dateStr` (`dateStr`),
  KEY `user_phone` (`user_phone`),
  KEY `user_name` (`user_name`)
) ENGINE=MyISAM AUTO_INCREMENT=19 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of soundreport
-- ----------------------------

-- ----------------------------
-- Table structure for `sound_record`
-- ----------------------------
DROP TABLE IF EXISTS `sound_record`;
CREATE TABLE `sound_record` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `file_name` varchar(100) NOT NULL,
  `customer_phone` varchar(11) DEFAULT NULL,
  `local_phone` varchar(11) NOT NULL,
  `direction` varchar(4) DEFAULT '呼出',
  `user_id` varchar(11) NOT NULL,
  `user_name` varchar(20) NOT NULL,
  `up_down_id` varchar(50) NOT NULL,
  `sound_time` datetime DEFAULT NULL,
  `sound_length` int(11) NOT NULL,
  `add_time` datetime NOT NULL,
  `remark` varchar(500) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `customer_phone` (`customer_phone`),
  KEY `local_phone` (`local_phone`),
  KEY `user_id` (`user_id`),
  KEY `user_name` (`user_name`),
  KEY `up_down_id` (`up_down_id`),
  KEY `sound_time` (`sound_time`),
  KEY `sound_length` (`sound_length`),
  KEY `direction` (`direction`)
) ENGINE=MyISAM AUTO_INCREMENT=20 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sound_record
-- ----------------------------

-- ----------------------------
-- Table structure for `track_record`
-- ----------------------------
DROP TABLE IF EXISTS `track_record`;
CREATE TABLE `track_record` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `customer_id` int(11) NOT NULL,
  `user_id` varchar(11) NOT NULL,
  `user_name` varchar(20) NOT NULL,
  `content` varchar(2000) NOT NULL,
  `add_time` datetime NOT NULL,
  PRIMARY KEY (`id`),
  KEY `customer_id` (`customer_id`),
  KEY `user_id` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=138 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of track_record
-- ----------------------------
