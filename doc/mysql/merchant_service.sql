CREATE DATABASE IF NOT EXISTS flash_merchant_service DEFAULT CHARACTER SET = utf8mb4;
USE flash_merchant_service;

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for app
-- ----------------------------
DROP TABLE IF EXISTS `app`;
CREATE TABLE `app` (
  `ID` bigint(20) NOT NULL,
  `APP_ID` varchar(50) DEFAULT NULL,
  `APP_NAME` varchar(50) DEFAULT NULL COMMENT '应用名称',
  `MERCHANT_ID` bigint(20) DEFAULT NULL COMMENT '所属商户',
  `PUBLIC_KEY` varchar(50) DEFAULT NULL COMMENT '应用公钥(RSAWithSHA256)',
  `NOTIFY_URL` varchar(50) DEFAULT NULL COMMENT '授权回调地址',
  PRIMARY KEY (`ID`) USING BTREE,
  UNIQUE KEY `APP_ID` (`APP_ID`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for merchant
-- ----------------------------
DROP TABLE IF EXISTS `merchant`;
CREATE TABLE `merchant` (
  `ID` bigint(20) NOT NULL COMMENT '主键',
  `MERCHANT_NAME` varchar(50) DEFAULT NULL COMMENT '商户名称',
  `MERCHANT_NO` varchar(50) DEFAULT NULL COMMENT '企业编号',
  `MERCHANT_ADDRESS` varchar(255) DEFAULT NULL COMMENT '企业地址',
  `MERCHANT_TYPE` varchar(50) DEFAULT NULL COMMENT '商户类型',
  `BUSINESS_LICENSES_IMG` varchar(100) DEFAULT NULL COMMENT '营业执照（企业证明）',
  `ID_CARD_FRONT_IMG` varchar(100) DEFAULT NULL COMMENT '法人身份证正面照片',
  `ID_CARD_AFTER_IMG` varchar(100) DEFAULT NULL COMMENT '法人身份证反面照片',
  `USERNAME` varchar(50) DEFAULT NULL COMMENT '联系人姓名',
  `MOBILE` varchar(50) DEFAULT NULL COMMENT '联系人手机号(关联统一账号)',
  `CONTACTS_ADDRESS` varchar(255) DEFAULT NULL COMMENT '联系人地址',
  `AUDIT_STATUS` varchar(20) DEFAULT NULL COMMENT '审核状态 0-未申请,1-已申请待审核,2-审核通过,3-审核拒绝',
  `TENANT_ID` bigint(20) DEFAULT NULL COMMENT '租户ID,关联统一用户',
  PRIMARY KEY (`ID`) USING BTREE,
  UNIQUE KEY `MOBILE` (`MOBILE`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for staff
-- ----------------------------
DROP TABLE IF EXISTS `staff`;
CREATE TABLE `staff` (
  `ID` bigint(20) NOT NULL COMMENT '主键',
  `MERCHANT_ID` bigint(20) DEFAULT NULL COMMENT '商户ID',
  `FULL_NAME` varchar(50) DEFAULT NULL COMMENT '姓名',
  `POSITION` varchar(50) DEFAULT NULL COMMENT '职位',
  `USERNAME` varchar(50) DEFAULT NULL COMMENT '用户名(关联统一用户)',
  `MOBILE` varchar(50) DEFAULT NULL COMMENT '手机号(关联统一用户)',
  `STORE_ID` bigint(20) DEFAULT NULL COMMENT '员工所属门店',
  `LAST_LOGIN_TIME` datetime DEFAULT NULL COMMENT '最后一次登录时间',
  `STAFF_STATUS` bit(1) DEFAULT NULL COMMENT '0表示禁用，1表示启用',
  PRIMARY KEY (`ID`) USING BTREE,
  UNIQUE KEY `MERCHANT_ID` (`MERCHANT_ID`,`MOBILE`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for store
-- ----------------------------
DROP TABLE IF EXISTS `store`;
CREATE TABLE `store` (
  `ID` bigint(20) NOT NULL,
  `STORE_NAME` varchar(50) DEFAULT NULL COMMENT '门店名称',
  `STORE_NUMBER` bigint(20) DEFAULT NULL COMMENT '门店编号',
  `MERCHANT_ID` bigint(20) DEFAULT NULL COMMENT '所属商户',
  `PARENT_ID` bigint(20) DEFAULT NULL COMMENT '父门店',
  `STORE_STATUS` bit(1) DEFAULT NULL COMMENT '0表示禁用，1表示启用',
  `STORE_ADDRESS` varchar(50) DEFAULT NULL COMMENT '门店地址',
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for store_staff
-- ----------------------------
DROP TABLE IF EXISTS `store_staff`;
CREATE TABLE `store_staff` (
  `ID` bigint(20) NOT NULL,
  `STORE_ID` bigint(20) DEFAULT NULL COMMENT '门店标识',
  `STAFF_ID` bigint(20) DEFAULT NULL COMMENT '员工标识',
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

SET FOREIGN_KEY_CHECKS = 1;
