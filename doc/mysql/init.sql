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
  `STAFF_STATUS` bit(1) DEFAULT NULL COMMENT '0表示禁用, 1表示启用',
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
  `STORE_STATUS` bit(1) DEFAULT NULL COMMENT '0表示禁用, 1表示启用',
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


CREATE DATABASE IF NOT EXISTS flash_transaction DEFAULT CHARACTER SET = utf8mb4;
USE flash_transaction;

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for app_platform_channel
-- ----------------------------
DROP TABLE IF EXISTS `app_platform_channel`;
CREATE TABLE `app_platform_channel` (
  `ID` bigint(20) NOT NULL,
  `APP_ID` varchar(50) DEFAULT NULL COMMENT '应用id',
  `PLATFORM_CHANNEL` varchar(50) DEFAULT NULL COMMENT '平台支付渠道',
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='说明了应用选择了平台中的哪些支付渠道';

-- ----------------------------
-- Table structure for pay_channel
-- ----------------------------
DROP TABLE IF EXISTS `pay_channel`;
CREATE TABLE `pay_channel` (
  `ID` bigint(20) NOT NULL,
  `CHANNEL_NAME` varchar(50) DEFAULT NULL COMMENT '原始支付渠道名称',
  `CHANNEL_CODE` varchar(50) DEFAULT NULL COMMENT '原始支付渠道编码',
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of pay_channel
-- ----------------------------
BEGIN;
INSERT INTO `pay_channel` VALUES (1, '微信JSAPI', 'WX_JSAPI');
INSERT INTO `pay_channel` VALUES (2, '支付宝手机网站支付', 'ALIPAY_WAP');
INSERT INTO `pay_channel` VALUES (3, '支付宝条码支付', 'ALIPAY_BAR_CODE');
INSERT INTO `pay_channel` VALUES (4, '微信付款码支付', 'WX_MICROPAY');
COMMIT;

-- ----------------------------
-- Table structure for pay_channel_param
-- ----------------------------
DROP TABLE IF EXISTS `pay_channel_param`;
CREATE TABLE `pay_channel_param` (
  `ID` bigint(20) NOT NULL,
  `CHANNEL_NAME` varchar(50) DEFAULT NULL COMMENT '自定义渠道名称',
  `MERCHANT_ID` bigint(20) DEFAULT NULL COMMENT '商户ID',
  `PAY_CHANNEL` varchar(50) DEFAULT NULL COMMENT '原始支付渠道编码',
  `PARAM` text COMMENT '支付参数',
  `APP_PLATFORM_CHANNEL_ID` bigint(20) DEFAULT NULL COMMENT '应用所选择的平台支付渠道ID',
  PRIMARY KEY (`ID`) USING BTREE,
  UNIQUE KEY `PAY_CHANNEL` (`PAY_CHANNEL`,`APP_PLATFORM_CHANNEL_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='某商户针对某一种原始支付渠道的配置参数';

-- ----------------------------
-- Table structure for pay_order
-- ----------------------------
DROP TABLE IF EXISTS `pay_order`;
CREATE TABLE `pay_order` (
  `ID` bigint(20) NOT NULL,
  `TRADE_NO` varchar(50) NOT NULL COMMENT '聚合支付订单号',
  `MERCHANT_ID` bigint(20) NOT NULL COMMENT '所属商户',
  `STORE_ID` bigint(20) DEFAULT NULL COMMENT '商户下门店',
  `APP_ID` varchar(50) NOT NULL COMMENT '所属应用',
  `PAY_CHANNEL` varchar(50) DEFAULT NULL COMMENT '原始支付渠道编码',
  `PAY_CHANNEL_MCH_ID` varchar(50) DEFAULT NULL COMMENT '原始渠道商户id',
  `PAY_CHANNEL_MCH_APP_ID` varchar(50) DEFAULT NULL COMMENT '原始渠道商户应用id',
  `PAY_CHANNEL_TRADE_NO` varchar(50) DEFAULT NULL COMMENT '原始渠道订单号',
  `CHANNEL` varchar(50) DEFAULT NULL COMMENT '聚合支付的渠道',
  `OUT_TRADE_NO` varchar(50) DEFAULT NULL COMMENT '商户订单号',
  `SUBJECT` varchar(50) DEFAULT NULL COMMENT '商品标题',
  `BODY` varchar(256) DEFAULT NULL COMMENT '订单描述',
  `CURRENCY` varchar(50) DEFAULT NULL COMMENT '币种CNY',
  `TOTAL_AMOUNT` int(11) DEFAULT NULL COMMENT '订单总金额, 单位为分',
  `OPTIONAL` varchar(256) DEFAULT NULL COMMENT '用户自定义的参数,商户自定义数据',
  `ANALYSIS` varchar(256) DEFAULT NULL COMMENT '用于统计分析的数据,用户自定义',
  `EXTRA` varchar(512) DEFAULT NULL COMMENT '特定渠道发起时额外参数',
  `TRADE_STATE` varchar(50) DEFAULT NULL COMMENT '交易状态支付状态,0-订单生成,1-支付中(目前未使用),2-支付成功,3-业务处理完成,4-关闭',
  `ERROR_CODE` varchar(50) DEFAULT NULL COMMENT '渠道支付错误码',
  `ERROR_MSG` varchar(256) DEFAULT NULL COMMENT '渠道支付错误信息',
  `DEVICE` varchar(50) DEFAULT NULL COMMENT '设备',
  `CLIENT_IP` varchar(50) DEFAULT NULL COMMENT '客户端IP',
  `CREATE_TIME` datetime DEFAULT NULL COMMENT '创建时间',
  `UPDATE_TIME` datetime DEFAULT NULL COMMENT '更新时间',
  `EXPIRE_TIME` datetime DEFAULT NULL COMMENT '订单过期时间',
  `PAY_SUCCESS_TIME` datetime DEFAULT NULL COMMENT '支付成功时间',
  PRIMARY KEY (`ID`) USING BTREE,
  UNIQUE KEY `unique_TRADE_NO` (`TRADE_NO`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for payment_bill
-- ----------------------------
DROP TABLE IF EXISTS `payment_bill`;
CREATE TABLE `payment_bill` (
  `id` bigint(20) NOT NULL,
  `merchant_id` bigint(20) NOT NULL COMMENT '商户id',
  `merchant_name` varchar(60) NOT NULL COMMENT '商户名称',
  `merchant_app_id` bigint(20) NOT NULL COMMENT '商户应用Id',
  `merchant_order_no` varchar(60) NOT NULL COMMENT '商户订单号',
  `channel_order_no` varchar(60) NOT NULL COMMENT '渠道订单号',
  `product_name` varchar(255) NOT NULL COMMENT '商品名称',
  `create_time` varchar(60) DEFAULT NULL COMMENT '创建时间',
  `pos_time` varchar(60) NOT NULL COMMENT '交易时间',
  `equipment_no` varchar(60) DEFAULT NULL COMMENT '终端号',
  `user_account` varchar(60) DEFAULT NULL COMMENT '用户账号/标识信息',
  `total_amount` decimal(10,2) DEFAULT NULL COMMENT '订单金额',
  `trade_amount` decimal(10,2) NOT NULL COMMENT '实际交易金额',
  `discount_amount` decimal(10,2) DEFAULT NULL COMMENT '折扣金额',
  `service_fee` decimal(10,4) DEFAULT NULL COMMENT '手续费',
  `refund_order_no` varchar(60) DEFAULT NULL COMMENT '退款单号',
  `refund_money` decimal(10,2) DEFAULT NULL COMMENT '退款金额',
  `platform_channel` varchar(50) NOT NULL COMMENT '原始支付渠道',
  `trade_state` varchar(255) DEFAULT NULL COMMENT '交易状态: SUCCESS—支付成功, REFUND—转入退款, REVOKED—已撤销',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for platform_channel
-- ----------------------------
DROP TABLE IF EXISTS `platform_channel`;
CREATE TABLE `platform_channel` (
  `ID` bigint(20) NOT NULL,
  `CHANNEL_NAME` varchar(50) DEFAULT NULL COMMENT '平台支付渠道名称',
  `CHANNEL_CODE` varchar(50) DEFAULT NULL COMMENT '平台支付渠道编码',
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of platform_channel
-- ----------------------------
BEGIN;
INSERT INTO `platform_channel` VALUES (1, '闪聚B扫C', 'shanju_b2c');
INSERT INTO `platform_channel` VALUES (2, '闪聚C扫B', 'shanju_c2b');
COMMIT;

-- ----------------------------
-- Table structure for platform_pay_channel
-- ----------------------------
DROP TABLE IF EXISTS `platform_pay_channel`;
CREATE TABLE `platform_pay_channel` (
  `ID` bigint(20) NOT NULL,
  `PLATFORM_CHANNEL` varchar(20) DEFAULT NULL COMMENT '平台支付渠道编码',
  `PAY_CHANNEL` varchar(20) DEFAULT NULL COMMENT '原始支付渠道名称',
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of platform_pay_channel
-- ----------------------------
BEGIN;
INSERT INTO `platform_pay_channel` VALUES (1, 'shanju_b2c', 'WX_MICROPAY');
INSERT INTO `platform_pay_channel` VALUES (2, 'shanju_b2c', 'ALIPAY_BAR_CODE');
INSERT INTO `platform_pay_channel` VALUES (5, 'shanju_c2b', 'WX_JSAPI');
INSERT INTO `platform_pay_channel` VALUES (6, 'shanju_c2b', 'ALIPAY_WAP');
COMMIT;

-- ----------------------------
-- Table structure for refund_order
-- ----------------------------
DROP TABLE IF EXISTS `refund_order`;
CREATE TABLE `refund_order` (
  `ID` bigint(20) NOT NULL,
  `REFUND_NO` varchar(50) DEFAULT NULL COMMENT '聚合支付退款订单号',
  `TRADE_NO` varchar(50) DEFAULT NULL COMMENT '聚合支付订单号',
  `MERCHANT_ID` bigint(20) DEFAULT NULL COMMENT '所属商户',
  `APP_ID` varchar(50) DEFAULT NULL COMMENT '所属应用',
  `PAY_CHANNEL` varchar(50) DEFAULT NULL COMMENT '原始支付渠道编码',
  `PAY_CHANNEL_MCH_ID` varchar(50) DEFAULT NULL COMMENT '原始渠道商户id',
  `PAY_CHANNEL_TRADE_NO` varchar(50) DEFAULT NULL COMMENT '原始渠道订单号',
  `PAY_CHANNEL_REFUND_NO` varchar(50) DEFAULT NULL COMMENT '原始渠道退款订单号',
  `CHANNEL` varchar(50) DEFAULT NULL COMMENT '聚合支付的渠道',
  `OUT_TRADE_NO` varchar(50) DEFAULT NULL COMMENT '商户订单号',
  `OUT_REFUND_NO` varchar(50) DEFAULT NULL COMMENT '商户退款订单号',
  `PAY_CHANNEL_USER` varchar(50) DEFAULT NULL COMMENT '原始渠道用户标识,如微信openId,支付宝账号',
  `PAY_CHANNEL_USERNAME` varchar(50) DEFAULT NULL COMMENT '原始渠道用户姓名',
  `CURRENCY` varchar(50) DEFAULT NULL COMMENT '币种CNY',
  `TOTAL_AMOUNT` int(11) DEFAULT NULL COMMENT '订单总金额, 单位为分',
  `REFUND_AMOUNT` int(11) DEFAULT NULL COMMENT '退款金额,单位分',
  `OPTIONAL` varchar(256) DEFAULT NULL COMMENT '用户自定义的参数,商户自定义数据',
  `ANALYSIS` varchar(256) DEFAULT NULL COMMENT '用于统计分析的数据,用户自定义',
  `EXTRA` varchar(512) DEFAULT NULL COMMENT '特定渠道发起时额外参数',
  `REFUND_STATE` varchar(50) DEFAULT NULL COMMENT '退款状态:0-订单生成,1-退款中,2-退款成功,3-退款失败,4-业务处理完成',
  `REFUND_RESULT` varchar(50) DEFAULT NULL COMMENT '退款结果:0-不确认结果,1-等待手动处理,2-确认成功,3-确认失败',
  `ERROR_CODE` varchar(50) DEFAULT NULL COMMENT '渠道支付错误码',
  `ERROR_MSG` varchar(256) DEFAULT NULL COMMENT '渠道支付错误信息',
  `DEVICE` varchar(50) DEFAULT NULL COMMENT '设备',
  `CLIENT_IP` varchar(50) DEFAULT NULL COMMENT '客户端IP',
  `CREATE_TIME` datetime DEFAULT NULL COMMENT '创建时间',
  `UPDATE_TIME` datetime DEFAULT NULL COMMENT '更新时间',
  `EXPIRE_TIME` datetime DEFAULT NULL COMMENT '订单过期时间',
  `REFUND_SUCCESS_TIME` datetime DEFAULT NULL COMMENT '退款成功时间',
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

SET FOREIGN_KEY_CHECKS = 1;


CREATE DATABASE IF NOT EXISTS flash_uaa DEFAULT CHARACTER SET = utf8mb4;
USE flash_uaa;

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for oauth_client_details
-- ----------------------------
DROP TABLE IF EXISTS `oauth_client_details`;
CREATE TABLE `oauth_client_details` (
  `client_id` varchar(255) NOT NULL COMMENT '客户端标识',
  `resource_ids` varchar(255) DEFAULT NULL COMMENT '接入资源列表',
  `client_secret` varchar(255) DEFAULT NULL COMMENT '客户端秘钥',
  `scope` varchar(255) DEFAULT NULL,
  `authorized_grant_types` varchar(255) DEFAULT NULL,
  `web_server_redirect_uri` varchar(255) DEFAULT NULL,
  `authorities` varchar(255) DEFAULT NULL,
  `access_token_validity` int(11) DEFAULT NULL,
  `refresh_token_validity` int(11) DEFAULT NULL,
  `additional_information` longtext,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `archived` tinyint(4) DEFAULT NULL,
  `trusted` tinyint(4) DEFAULT NULL,
  `autoapprove` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`client_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='接入客户端信息';

-- ----------------------------
-- Records of oauth_client_details
-- ----------------------------
BEGIN;
INSERT INTO `oauth_client_details` VALUES ('merchant-platform', 'shanju-resource', '123456', 'read', 'client_credentials,password,authorization_code,implicit,refresh_token', NULL, 'ROLE_MERCHANT,ROLE_USER', 31536000, 259200, NULL, '2021-08-27 12:15:34', 0, 0, 'false');
INSERT INTO `oauth_client_details` VALUES ('operation-platform', 'shanju-resource', '123456', 'read', 'client_credentials,password,authorization_code,implicit,refresh_token', NULL, 'ROLE_OPERATION', 31536000, 259200, NULL, '2021-08-27 12:15:35', 0, 0, 'false');
INSERT INTO `oauth_client_details` VALUES ('portal-site', 'shanju-resource', '123456', 'read', 'client_credentials,password,authorization_code,implicit,refresh_token', NULL, 'ROLE_PORTAL', 7200, 259200, NULL, '2021-08-27 12:15:38', 0, 0, 'false');
INSERT INTO `oauth_client_details` VALUES ('shanju-resource', 'shanju-resource', '123456', 'read', 'client_credentials,password,authorization_code,implicit,refresh_token', NULL, 'ROLE_API', 7200, 259200, NULL, '2021-08-27 12:15:15', 0, 0, 'false');
COMMIT;


-- ----------------------------
-- Table structure for oauth_code
-- ----------------------------
DROP TABLE IF EXISTS `oauth_code`;
CREATE TABLE `oauth_code` (
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `code` varchar(255) NOT NULL COMMENT '授权码',
  `authentication` longblob COMMENT '认证信息',
  PRIMARY KEY (`code`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='授权码';

SET FOREIGN_KEY_CHECKS = 1;


CREATE DATABASE IF NOT EXISTS flash_user DEFAULT CHARACTER SET = utf8mb4;
USE flash_user;

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for account
-- ----------------------------
DROP TABLE IF EXISTS `account`;
CREATE TABLE `account` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `USERNAME` varchar(50) DEFAULT NULL COMMENT '用户名',
  `MOBILE` varchar(50) NOT NULL COMMENT '手机号',
  `PASSWORD` varchar(50) DEFAULT NULL COMMENT '密码',
  `SALT` varchar(50) DEFAULT NULL COMMENT '盐',
  PRIMARY KEY (`ID`) USING BTREE,
  UNIQUE KEY `mobile_idx` (`MOBILE`),
  UNIQUE KEY `username_idx` (`USERNAME`)
) ENGINE=InnoDB AUTO_INCREMENT=1206857411246428199 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of account
-- ----------------------------
BEGIN;
INSERT INTO `account` VALUES (1111111672565051234, 'shanju-operation', '13711111111', 'fcc58e79d1481a65a754e3a94aff31c5', 'srxBW');
COMMIT;

-- ----------------------------
-- Table structure for account_role
-- ----------------------------
DROP TABLE IF EXISTS `account_role`;
CREATE TABLE `account_role` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `USERNAME` varchar(50) DEFAULT NULL COMMENT '用户名',
  `ROLE_CODE` varchar(50) DEFAULT NULL COMMENT '角色编码',
  `TENANT_ID` bigint(20) DEFAULT NULL COMMENT '租户id',
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1301761810982985185 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='账号-角色关系';

-- ----------------------------
-- Records of account_role
-- ----------------------------
BEGIN;
INSERT INTO `account_role` VALUES (1301761810982985069, 'shanju-operation', 'r_sj_operation_admin', 1111111672565051234);
COMMIT;

-- ----------------------------
-- Table structure for authorization_privilege
-- ----------------------------
DROP TABLE IF EXISTS `authorization_privilege`;
CREATE TABLE `authorization_privilege` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `NAME` varchar(50) DEFAULT NULL COMMENT '权限名称',
  `CODE` varchar(50) DEFAULT NULL COMMENT '权限编码',
  `PRIVILEGE_GROUP_ID` bigint(20) DEFAULT NULL COMMENT '所属权限组id',
  PRIMARY KEY (`ID`) USING BTREE,
  UNIQUE KEY `priv_unq_code` (`CODE`)
) ENGINE=InnoDB AUTO_INCREMENT=109 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='权限';

-- ----------------------------
-- Records of authorization_privilege
-- ----------------------------
BEGIN;
INSERT INTO `authorization_privilege` VALUES (1, '工作台', 'sj_m_console', 1);
INSERT INTO `authorization_privilege` VALUES (2, '应用管理', 'sj_m_app_list', 4);
INSERT INTO `authorization_privilege` VALUES (3, '应用交易总览', 'sj_m_transaction_list', 4);
INSERT INTO `authorization_privilege` VALUES (4, '应用财务对账', 'sj_m_account_check', 4);
INSERT INTO `authorization_privilege` VALUES (5, '开始支付', 'sj_m_payment', 4);
INSERT INTO `authorization_privilege` VALUES (6, '账户中心', 'sj_m_account_list', 5);
INSERT INTO `authorization_privilege` VALUES (7, '企业认证', 'sj_m_enterprise_auth', 5);
INSERT INTO `authorization_privilege` VALUES (8, '门店管理', 'sj_m_store_list', 6);
INSERT INTO `authorization_privilege` VALUES (9, '成员管理', 'sj_m_staff_list', 6);
INSERT INTO `authorization_privilege` VALUES (10, '会员管理', 'sj_o_member_list', 7);
INSERT INTO `authorization_privilege` VALUES (11, '企业管理', 'sj_o_entreprise_list', 7);
INSERT INTO `authorization_privilege` VALUES (12, '审核管理', 'sj_o_audit', 7);
INSERT INTO `authorization_privilege` VALUES (13, '服务类型管理', 'sj_o_service_type', 8);
INSERT INTO `authorization_privilege` VALUES (14, '往来对账', 'sj_o_account_check', 9);
INSERT INTO `authorization_privilege` VALUES (15, '管理员管理', 'sj_o_admin_list', 10);
INSERT INTO `authorization_privilege` VALUES (16, '角色管理', 'sj_o_role_list', 10);
INSERT INTO `authorization_privilege` VALUES (17, '应用创建', 'sj_m_app_create', 4);
INSERT INTO `authorization_privilege` VALUES (18, '设置默认支付', 'sj_m_payment_set', 4);
INSERT INTO `authorization_privilege` VALUES (19, '门店新增', 'sj_m_store_create', 6);
INSERT INTO `authorization_privilege` VALUES (20, '门店查询', 'sj_m_store_query', 6);
INSERT INTO `authorization_privilege` VALUES (21, '成员新增', 'sj_m_staff_create', 6);
INSERT INTO `authorization_privilege` VALUES (22, '成员查询', 'sj_m_staff_query', 6);
INSERT INTO `authorization_privilege` VALUES (23, '会员查询', 'sj_o_member_query', 7);
INSERT INTO `authorization_privilege` VALUES (24, '企业查询', 'sj_o_enterprise_query', 7);
INSERT INTO `authorization_privilege` VALUES (25, '企业新建', 'sj_o_enterprise_create', 7);
INSERT INTO `authorization_privilege` VALUES (26, '服务类型新建', 'sj_o_service_create', 8);
INSERT INTO `authorization_privilege` VALUES (27, '服务类型查询', 'sj_o_service_query', 8);
INSERT INTO `authorization_privilege` VALUES (28, '管理员新建', 'sj_o_admin_create', 10);
INSERT INTO `authorization_privilege` VALUES (29, '管理员查询', 'sj_o_admin_query', 10);
INSERT INTO `authorization_privilege` VALUES (30, '角色新建', 'sj_o_role_create', 10);
INSERT INTO `authorization_privilege` VALUES (59, '角色查询', 'sj_o_role_query', 10);
INSERT INTO `authorization_privilege` VALUES (60, '角色权限保存', 'sj_o_role_save', 10);
INSERT INTO `authorization_privilege` VALUES (61, '企业认证的申请', 'sj_m_auth_apply', 7);
INSERT INTO `authorization_privilege` VALUES (62, '工作台续费', 'sj_m_console_renew', 1);
INSERT INTO `authorization_privilege` VALUES (63, '工作台升级', 'sj_m_console_upgrade', 1);
INSERT INTO `authorization_privilege` VALUES (64, '应用保存', 'sj_m_app_save', 4);
INSERT INTO `authorization_privilege` VALUES (65, '应用编辑', 'sj_m_app_modify', 4);
INSERT INTO `authorization_privilege` VALUES (66, '支付参数保存', 'sj_m_payparam_save', 4);
INSERT INTO `authorization_privilege` VALUES (67, '设置默认支付', 'sj_m_pay_set', 4);
INSERT INTO `authorization_privilege` VALUES (68, '默认支付保存', 'sj_m_pay_save', 4);
INSERT INTO `authorization_privilege` VALUES (69, 'C扫B二维码生成', 'sj_m_c2b_qrcode', 4);
INSERT INTO `authorization_privilege` VALUES (70, 'B扫C订单生成', 'sj_m_b2c_order', 4);
INSERT INTO `authorization_privilege` VALUES (71, '线上支付参数展示', 'sj_m_h5_view', 4);
INSERT INTO `authorization_privilege` VALUES (72, '购买套餐', 'sj_m_bundle_buy', 5);
INSERT INTO `authorization_privilege` VALUES (73, '企业认证资料提交', 'sj_m_enterprise_info_submit', 5);
INSERT INTO `authorization_privilege` VALUES (74, '企业认证资料取消', 'sj_m_enterprise_info_cancel', 5);
INSERT INTO `authorization_privilege` VALUES (76, '通过企业认证', 'sj_o_enterprise_auth_pass', 7);
INSERT INTO `authorization_privilege` VALUES (77, '驳回企业认证', 'sj_o_enterprise_auth_rejection', 7);
INSERT INTO `authorization_privilege` VALUES (81, '门店管理-编辑', 'sj_m_store_edit', 6);
INSERT INTO `authorization_privilege` VALUES (82, '成员管理-编辑', 'sj_m_staff_edit', 6);
INSERT INTO `authorization_privilege` VALUES (83, '管理员管理-编辑', 'sj_o_admin_edit', 10);
INSERT INTO `authorization_privilege` VALUES (84, '角色管理-编辑', 'sj_o_role_edit', 10);
INSERT INTO `authorization_privilege` VALUES (85, '门店管理-保存', 'sj_m_store_save', 6);
INSERT INTO `authorization_privilege` VALUES (86, '门店管理-删除', 'sj_m_store_del', 6);
INSERT INTO `authorization_privilege` VALUES (87, '成员管理-保存', 'sj_m_staff_save', 6);
INSERT INTO `authorization_privilege` VALUES (102, '账户管理', 'sj_m_account', 1);
INSERT INTO `authorization_privilege` VALUES (103, '支付应用管理', 'sj_m_app', 1);
INSERT INTO `authorization_privilege` VALUES (104, '组织管理', 'sj_m_organization', 1);
INSERT INTO `authorization_privilege` VALUES (105, '企业管理', 'sj_o_entreprise', 2);
INSERT INTO `authorization_privilege` VALUES (106, '服务类型管理', 'sj_o_service', 2);
INSERT INTO `authorization_privilege` VALUES (107, '系统管理', 'sj_o_sys', 2);
INSERT INTO `authorization_privilege` VALUES (108, '账单管理', 'sj_o_account', 2);
COMMIT;

-- ----------------------------
-- Table structure for authorization_privilege_group
-- ----------------------------
DROP TABLE IF EXISTS `authorization_privilege_group`;
CREATE TABLE `authorization_privilege_group` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `PARENT_ID` bigint(20) DEFAULT NULL COMMENT '父id',
  `NAME` varchar(50) DEFAULT NULL COMMENT '权限组名称',
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=24 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='权限组';

-- ----------------------------
-- Records of authorization_privilege_group
-- ----------------------------
BEGIN;
INSERT INTO `authorization_privilege_group` VALUES (1, 0, '商户平台');
INSERT INTO `authorization_privilege_group` VALUES (2, 0, '运营平台');
INSERT INTO `authorization_privilege_group` VALUES (3, 0, '门户网站');
INSERT INTO `authorization_privilege_group` VALUES (4, 1, '应用管理');
INSERT INTO `authorization_privilege_group` VALUES (5, 1, '账户管理');
INSERT INTO `authorization_privilege_group` VALUES (6, 1, '组织管理');
INSERT INTO `authorization_privilege_group` VALUES (7, 2, '企业管理');
INSERT INTO `authorization_privilege_group` VALUES (8, 2, '服务类型管理');
INSERT INTO `authorization_privilege_group` VALUES (9, 2, '账单管理');
INSERT INTO `authorization_privilege_group` VALUES (10, 2, '系统管理');
INSERT INTO `authorization_privilege_group` VALUES (12, 0, '商户平台app端');
COMMIT;

-- ----------------------------
-- Table structure for authorization_role
-- ----------------------------
DROP TABLE IF EXISTS `authorization_role`;
CREATE TABLE `authorization_role` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `NAME` varchar(50) DEFAULT NULL COMMENT '角色名称',
  `CODE` varchar(50) DEFAULT NULL COMMENT '角色编码',
  `TENANT_ID` bigint(20) DEFAULT NULL COMMENT '租户id',
  PRIMARY KEY (`ID`) USING BTREE,
  UNIQUE KEY `tenant_id_code_unique` (`CODE`,`TENANT_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=75 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='角色信息';

-- ----------------------------
-- Records of authorization_role
-- ----------------------------
BEGIN;
INSERT INTO `authorization_role` VALUES (56, '运营管理员', 'r_sj_operation_admin', 1111111672565051234);
COMMIT;

-- ----------------------------
-- Table structure for authorization_role_privilege
-- ----------------------------
DROP TABLE IF EXISTS `authorization_role_privilege`;
CREATE TABLE `authorization_role_privilege` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `ROLE_ID` bigint(20) DEFAULT NULL COMMENT '角色id',
  `PRIVILEGE_ID` bigint(20) DEFAULT NULL COMMENT '权限id',
  PRIMARY KEY (`ID`) USING BTREE,
  UNIQUE KEY `role_priv_unique` (`ROLE_ID`,`PRIVILEGE_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=1492 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='角色-权限关系';

-- ----------------------------
-- Records of authorization_role_privilege
-- ----------------------------
BEGIN;
INSERT INTO `authorization_role_privilege` VALUES (615, 56, 10);
INSERT INTO `authorization_role_privilege` VALUES (616, 56, 11);
INSERT INTO `authorization_role_privilege` VALUES (617, 56, 12);
INSERT INTO `authorization_role_privilege` VALUES (618, 56, 13);
INSERT INTO `authorization_role_privilege` VALUES (619, 56, 14);
INSERT INTO `authorization_role_privilege` VALUES (620, 56, 15);
INSERT INTO `authorization_role_privilege` VALUES (621, 56, 16);
INSERT INTO `authorization_role_privilege` VALUES (622, 56, 23);
INSERT INTO `authorization_role_privilege` VALUES (623, 56, 24);
INSERT INTO `authorization_role_privilege` VALUES (624, 56, 25);
INSERT INTO `authorization_role_privilege` VALUES (625, 56, 26);
INSERT INTO `authorization_role_privilege` VALUES (626, 56, 27);
INSERT INTO `authorization_role_privilege` VALUES (627, 56, 28);
INSERT INTO `authorization_role_privilege` VALUES (628, 56, 29);
INSERT INTO `authorization_role_privilege` VALUES (629, 56, 30);
INSERT INTO `authorization_role_privilege` VALUES (630, 56, 59);
INSERT INTO `authorization_role_privilege` VALUES (631, 56, 60);
INSERT INTO `authorization_role_privilege` VALUES (632, 56, 76);
INSERT INTO `authorization_role_privilege` VALUES (633, 56, 77);
INSERT INTO `authorization_role_privilege` VALUES (634, 56, 83);
INSERT INTO `authorization_role_privilege` VALUES (635, 56, 84);
INSERT INTO `authorization_role_privilege` VALUES (1224, 56, 105);
INSERT INTO `authorization_role_privilege` VALUES (1225, 56, 106);
INSERT INTO `authorization_role_privilege` VALUES (1226, 56, 107);
INSERT INTO `authorization_role_privilege` VALUES (1227, 56, 108);
COMMIT;

-- ----------------------------
-- Table structure for bundle
-- ----------------------------
DROP TABLE IF EXISTS `bundle`;
CREATE TABLE `bundle` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `NAME` varchar(50) DEFAULT NULL COMMENT '套餐名称',
  `CODE` varchar(50) DEFAULT NULL COMMENT '套餐编码',
  `TENANT_TYPE_CODE` varchar(50) DEFAULT NULL COMMENT '租户类型编码',
  `ABILITY` longtext COMMENT '套餐包含功能描述,JSON格式的角色与权限',
  `NUMBER_OF_INVOCATION` int(11) NOT NULL COMMENT 'API调用次数/月',
  `NUMBER_OF_CONCURRENT` int(11) NOT NULL COMMENT '并发数/秒',
  `NUMBER_OF_APP` int(11) NOT NULL COMMENT '允许创建应用数量',
  `COMMENT` varchar(200) DEFAULT NULL COMMENT '套餐说明',
  `INITIALIZE` int(1) DEFAULT NULL COMMENT '是否为初始化套餐',
  PRIMARY KEY (`ID`) USING BTREE,
  UNIQUE KEY `bundle_unq_code` (`CODE`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of bundle
-- ----------------------------
BEGIN;
INSERT INTO `bundle` VALUES (1, '系统管理根租户初始化套餐', 'admin', 'admin', NULL, 0, 0, 0, NULL, 1);
INSERT INTO `bundle` VALUES (2, '闪聚支付-商户初始化版', 'shanju-merchant', 'shanju-merchant', '[{\"name\":\"商户管理员\",\"code\": \"r_001\",\"privilegeCodes\": [\"sj_m_account\",\"sj_m_app\",\"sj_m_organization\",\"sj_m_account_check\",\"sj_m_account_list\",\"sj_m_app_create\",\"sj_m_app_list\",\"sj_m_app_modify\",\"sj_m_app_save\",\"sj_m_auth_apply\",\"sj_m_b2c_order\",\"sj_m_bundle_buy\",\"sj_m_c2b_qrcode\",\"sj_m_console\",\"sj_m_console_renew\",\"sj_m_console_upgrade\",\"sj_m_enterprise_auth\",\"sj_m_enterprise_info_cancel\",\"sj_m_enterprise_info_submit\",\"sj_m_h5_view\",\"sj_m_payment\",\"sj_m_payment_set\",\"sj_m_payparam_save\",\"sj_m_pay_save\",\"sj_m_pay_set\",\"sj_m_staff_create\",\"sj_m_staff_del\",\"sj_m_staff_edit\",\"sj_m_staff_list\",\"sj_m_staff_query\",\"sj_m_staff_save\",\"sj_m_store_create\",\"sj_m_store_del\",\"sj_m_store_edit\",\"sj_m_store_list\",\"sj_m_store_query\",\"sj_m_store_save\",\"sj_m_transaction_list\",\"sj_o_account_check\",\"sj_o_admin_create\",\"sj_o_admin_edit\",\"sj_o_admin_list\",\"sj_o_admin_query\",\"sj_o_audit\",\"sj_o_enterprise_auth_pass\",\"sj_o_enterprise_auth_rejection\",\"sj_o_enterprise_create\",\"sj_o_enterprise_query\",\"sj_o_entreprise_list\",\"sj_o_member_list\",\"sj_o_member_query\",\"sj_o_role_create\",\"sj_o_role_edit\",\"sj_o_role_list\",\"sj_o_role_query\",\"sj_o_role_save\",\"sj_o_service_create\",\"sj_o_service_query\",\"sj_o_service_type\"]},{\"name\": \"商户门店收银员\",\"code\": \"r_002\",\"privilegeCodes\":  [\"sj_m_account_check\",\"sj_m_transaction_list\"]}]', 1000, 10, 1, NULL, 1);
INSERT INTO `bundle` VALUES (3, '闪聚支付-运营后台初始化套餐', 'shanju-operation', 'shanju-operation', '[{\"name\":\"运营后台管理员\",\"code\": \"r_001\",\"privilegeCodes\":[\"sj_o_entreprise\",\"sj_o_service\",\"sj_o_sys\",\"sj_o_account\",\"sj_o_account_check\",\"sj_o_admin_create\",\"sj_o_admin_edit\",\"sj_o_admin_list\",\"sj_o_admin_query\",\"sj_o_audit\",\"sj_o_enterprise_auth_pass\",\"sj_o_enterprise_auth_rejection\",\"sj_o_enterprise_create\",\"sj_o_enterprise_query\",\"sj_o_entreprise_list\",\"sj_o_member_list\",\"sj_o_member_query\",\"sj_o_role_create\",\"sj_o_role_edit\",\"sj_o_role_list\",\"sj_o_role_query\",\"sj_o_role_save\",\"sj_o_service_create\",\"sj_o_service_query\",\"sj_o_service_type\"]}]', 1000, 10, 1, NULL, 1);
COMMIT;

-- ----------------------------
-- Table structure for resource_application
-- ----------------------------
DROP TABLE IF EXISTS `resource_application`;
CREATE TABLE `resource_application` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `NAME` varchar(50) DEFAULT NULL COMMENT '应用名称',
  `CODE` varchar(50) DEFAULT NULL COMMENT '应用编码',
  `TENANT_ID` bigint(20) DEFAULT NULL COMMENT '租户id',
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1194570708080922687 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='应用信息';

-- ----------------------------
-- Records of resource_application
-- ----------------------------
BEGIN;
INSERT INTO `resource_application` VALUES (1, '闪聚支付-商户平台', 'merchant-platform', 1);
INSERT INTO `resource_application` VALUES (2, '闪聚支付-运营平台', 'operation-platform', 1);
INSERT INTO `resource_application` VALUES (3, '门户', 'portal-site', 1);
INSERT INTO `resource_application` VALUES (4, '商户平台app版', 'merchant-platform-app', 1);
COMMIT;

-- ----------------------------
-- Table structure for resource_button
-- ----------------------------
DROP TABLE IF EXISTS `resource_button`;
CREATE TABLE `resource_button` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `PARENT_ID` bigint(20) DEFAULT NULL COMMENT '父id',
  `TITLE` varchar(50) NOT NULL COMMENT '按钮标题',
  `URL` varchar(200) DEFAULT NULL COMMENT '链接url',
  `ICON` varchar(50) DEFAULT NULL COMMENT '图标',
  `SORT` int(11) NOT NULL COMMENT '排序',
  `COMMENT` varchar(200) DEFAULT NULL COMMENT '说明',
  `STATUS` int(11) NOT NULL COMMENT '状态',
  `APPLICATION_CODE` varchar(50) DEFAULT NULL COMMENT '所属应用编码',
  `PRIVILEGE_CODE` varchar(50) DEFAULT NULL COMMENT '绑定权限',
  `PCODE` varchar(200) DEFAULT NULL COMMENT '用于鉴权',
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1241761810982985032 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='按钮资源';

-- ----------------------------
-- Records of resource_button
-- ----------------------------
BEGIN;
INSERT INTO `resource_button` VALUES (1241761810982985001, NULL, '工作台-升级', NULL, NULL, 2, NULL, 1, 'merchant-platform', 'sj_m_console_upgrade', 'sj_m_console_upgrade');
INSERT INTO `resource_button` VALUES (1241761810982985002, NULL, '工作台-续费', NULL, NULL, 1, NULL, 1, 'merchant-platform', 'sj_m_console_renew', 'sj_m_console_renew');
INSERT INTO `resource_button` VALUES (1241761810982985003, NULL, '创建应用', NULL, NULL, 1, NULL, 1, 'merchant-platform', 'sj_m_app_create', 'sj_m_app_create');
INSERT INTO `resource_button` VALUES (1241761810982985004, NULL, '保存应用', NULL, NULL, 1, NULL, 1, 'merchant-platform', 'sj_m_app_save', 'sj_m_app_save');
INSERT INTO `resource_button` VALUES (1241761810982985005, NULL, '编辑应用', NULL, NULL, 1, NULL, 1, 'merchant-platform', 'sj_m_app_edit', 'sj_m_app_edit');
INSERT INTO `resource_button` VALUES (1241761810982985010, NULL, '支付参数保存', NULL, NULL, 1, NULL, 1, 'merchant-platform', 'sj_m_payparam_save', 'sj_m_payparam_save');
INSERT INTO `resource_button` VALUES (1241761810982985011, NULL, '设置默认支付', NULL, NULL, 1, NULL, 1, 'merchant-platform', 'sj_m_pay_set', 'sj_m_pay_set');
INSERT INTO `resource_button` VALUES (1241761810982985012, NULL, '保存默认支付', NULL, NULL, 1, NULL, 1, 'merchant-platform', 'sj_m_pay_save', 'sj_m_pay_save');
INSERT INTO `resource_button` VALUES (1241761810982985013, NULL, 'C扫B二维码', NULL, NULL, 1, NULL, 1, 'merchant-platform', 'sj_m_c2b_qrcode', 'sj_m_c2b_qrcode');
INSERT INTO `resource_button` VALUES (1241761810982985014, NULL, 'B扫C订单', NULL, NULL, 1, NULL, 1, 'merchant-platform', 'sj_m_b2c_order', 'sj_m_b2c_order');
INSERT INTO `resource_button` VALUES (1241761810982985015, NULL, '购买套餐', NULL, NULL, 1, NULL, 1, 'merchant-platform', 'sj_m_bundle_buy', 'sj_m_bundle_buy');
INSERT INTO `resource_button` VALUES (1241761810982985017, NULL, '企业认证资料提交', NULL, NULL, 1, NULL, 1, 'merchant-platform', 'sj_m_enterprise_info_submit', 'sj_m_enterprise_info_submit');
INSERT INTO `resource_button` VALUES (1241761810982985018, NULL, '会员查询', NULL, NULL, 1, NULL, 1, 'merchant-platform', 'sj_o_member_query', 'sj_o_member_query');
INSERT INTO `resource_button` VALUES (1241761810982985019, NULL, '审核管理企业查询', NULL, NULL, 1, NULL, 1, 'merchant-platform', 'sj_o_enterprise_query', 'sj_o_enterprise_query');
INSERT INTO `resource_button` VALUES (1241761810982985021, NULL, '门店管理-新建按钮', NULL, NULL, 1, NULL, 1, 'merchant-platform', 'sj_m_store_create', 'sj_m_store_create');
INSERT INTO `resource_button` VALUES (1241761810982985022, NULL, '门店管理-编辑按钮', NULL, NULL, 1, NULL, 1, 'merchant-platform', 'sj_m_store_edit', 'sj_m_store_edit');
INSERT INTO `resource_button` VALUES (1241761810982985023, NULL, '门店管理-删除按钮', NULL, NULL, 1, NULL, 1, 'merchant-platform', 'sj_m_store_del', 'sj_m_store_del');
INSERT INTO `resource_button` VALUES (1241761810982985024, NULL, '门店管理-查询按钮', NULL, NULL, 1, NULL, 1, 'merchant-platform', 'sj_m_store_query', 'sj_m_store_query');
INSERT INTO `resource_button` VALUES (1241761810982985025, NULL, '门店管理-保存按钮', NULL, NULL, 1, NULL, 1, 'merchant-platform', 'sj_m_store_save', 'sj_m_store_save');
INSERT INTO `resource_button` VALUES (1241761810982985026, NULL, '成员管理-新建按钮', NULL, NULL, 1, NULL, 1, 'merchant-platform', 'sj_m_staff_create', 'sj_m_staff_create');
INSERT INTO `resource_button` VALUES (1241761810982985027, NULL, '成员管理-编辑按钮', NULL, NULL, 1, NULL, 1, 'merchant-platform', 'sj_m_staff_edit', 'sj_m_staff_edit');
COMMIT;

-- ----------------------------
-- Table structure for resource_menu
-- ----------------------------
DROP TABLE IF EXISTS `resource_menu`;
CREATE TABLE `resource_menu` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `PARENT_ID` bigint(20) DEFAULT NULL COMMENT '父id',
  `TITLE` varchar(50) NOT NULL COMMENT '菜单标题',
  `URL` varchar(200) DEFAULT NULL COMMENT '链接url',
  `ICON` varchar(50) DEFAULT NULL COMMENT '图标',
  `SORT` int(11) NOT NULL COMMENT '排序',
  `COMMENT` varchar(200) DEFAULT NULL COMMENT '说明',
  `STATUS` int(11) NOT NULL COMMENT '状态',
  `APPLICATION_CODE` varchar(50) DEFAULT NULL COMMENT '所属应用编码',
  `PRIVILEGE_CODE` varchar(50) DEFAULT NULL COMMENT '绑定权限',
  `PCODE` varchar(200) DEFAULT NULL COMMENT '用于鉴权',
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=347 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='菜单';

-- ----------------------------
-- Records of resource_menu
-- ----------------------------
BEGIN;
INSERT INTO `resource_menu` VALUES (1, 0, '商户后台', NULL, NULL, 1, NULL, 1, '', NULL, NULL);
INSERT INTO `resource_menu` VALUES (2, 0, '运营商后台', NULL, NULL, 1, NULL, 1, '', NULL, NULL);
INSERT INTO `resource_menu` VALUES (3, 0, '门户', NULL, NULL, 1, NULL, 1, '', NULL, NULL);
INSERT INTO `resource_menu` VALUES (4, 1, '工作台', NULL, NULL, 1, NULL, 1, 'merchant-platform', 'sj_m_console', 'sj_m_console');
INSERT INTO `resource_menu` VALUES (5, 1, '应用管理', NULL, NULL, 2, NULL, 1, 'merchant-platform', 'sj_m_app', 'sj_m_app');
INSERT INTO `resource_menu` VALUES (6, 1, '账户管理', NULL, NULL, 3, NULL, 1, 'merchant-platform', 'sj_m_account', 'sj_m_account');
INSERT INTO `resource_menu` VALUES (7, 1, '组织管理', NULL, NULL, 4, NULL, 1, 'merchant-platform', 'sj_m_organization', 'sj_m_organization');
INSERT INTO `resource_menu` VALUES (8, 2, '企业管理', NULL, NULL, 1, NULL, 1, 'operation-platform', 'sj_o_entreprise', 'sj_o_entreprise');
INSERT INTO `resource_menu` VALUES (9, 2, '服务类型管理', NULL, NULL, 2, NULL, 1, 'operation-platform', 'sj_o_service', 'sj_o_service');
INSERT INTO `resource_menu` VALUES (10, 2, '账单管理', NULL, NULL, 3, NULL, 1, 'operation-platform', 'sj_o_account', 'sj_o_account');
INSERT INTO `resource_menu` VALUES (11, 2, '系统管理', NULL, NULL, 4, NULL, 1, 'operation-platform', 'sj_o_sys', 'sj_o_sys');
INSERT INTO `resource_menu` VALUES (12, 5, '应用管理列表', NULL, NULL, 1, NULL, 1, 'merchant-platform', 'sj_m_app_list', 'sj_m_app_list');
INSERT INTO `resource_menu` VALUES (13, 5, '应用交易总览', NULL, NULL, 2, NULL, 1, 'merchant-platform', 'sj_m_transaction_list', 'sj_m_transaction_list');
INSERT INTO `resource_menu` VALUES (14, 5, '应用财务对账', NULL, NULL, 3, NULL, 1, 'merchant-platform', 'sj_m_account_check', 'sj_m_account_check');
INSERT INTO `resource_menu` VALUES (15, 5, '开始支付', NULL, NULL, 4, NULL, 1, 'merchant-platform', 'sj_m_payment', 'sj_m_payment');
INSERT INTO `resource_menu` VALUES (16, 6, '账户中心', NULL, NULL, 1, NULL, 1, 'merchant-platform', 'sj_m_account_list', 'sj_m_account_list');
INSERT INTO `resource_menu` VALUES (17, 6, '企业认证申请', NULL, NULL, 2, NULL, 1, 'merchant-platform', 'sj_m_enterprise_auth', 'sj_m_enterprise_auth');
INSERT INTO `resource_menu` VALUES (18, 7, '门店管理', NULL, NULL, 1, NULL, 1, 'merchant-platform', 'sj_m_store_list', 'sj_m_store_list');
INSERT INTO `resource_menu` VALUES (19, 7, '成员管理', NULL, NULL, 2, NULL, 1, 'merchant-platform', 'sj_m_staff_list', 'sj_m_staff_list');
INSERT INTO `resource_menu` VALUES (20, 8, '会员管理', NULL, NULL, 1, NULL, 1, 'operation-platform', 'sj_o_member_list', 'sj_o_member_list');
INSERT INTO `resource_menu` VALUES (21, 8, '企业管理', NULL, NULL, 2, NULL, 1, 'operation-platform', 'sj_o_entreprise_list', 'sj_o_entreprise_list');
INSERT INTO `resource_menu` VALUES (22, 8, '审核管理', NULL, NULL, 3, NULL, 1, 'operation-platform', 'sj_o_audit', 'sj_o_audit');
INSERT INTO `resource_menu` VALUES (23, 9, '服务类型管理', NULL, NULL, 1, NULL, 1, 'operation-platform', 'sj_o_service_type', 'sj_o_service_type');
INSERT INTO `resource_menu` VALUES (24, 10, '往来对账', NULL, NULL, 1, NULL, 1, 'operation-platform', 'sj_o_account_check', 'sj_o_account_check');
INSERT INTO `resource_menu` VALUES (25, 11, '管理员管理', NULL, NULL, 1, NULL, 1, 'operation-platform', 'sj_o_admin_list', 'sj_o_admin_list');
INSERT INTO `resource_menu` VALUES (26, 11, '角色管理', NULL, NULL, 2, NULL, 1, 'operation-platform', 'sj_o_role_list', 'sj_o_role_list');
COMMIT;

-- ----------------------------
-- Table structure for tenant
-- ----------------------------
DROP TABLE IF EXISTS `tenant`;
CREATE TABLE `tenant` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `NAME` varchar(512) DEFAULT NULL COMMENT '租户名称',
  `TENANT_TYPE_CODE` varchar(50) DEFAULT NULL COMMENT '租户类型编码',
  `BUNDLE_CODE` varchar(50) DEFAULT NULL COMMENT '购买套餐编码',
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1206857411133181959 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of tenant
-- ----------------------------
BEGIN;
INSERT INTO `tenant` VALUES (1111111672565051234, 'operationAdmin', 'shanju-operation', 'shanju-operation');
COMMIT;

-- ----------------------------
-- Table structure for tenant_account
-- ----------------------------
DROP TABLE IF EXISTS `tenant_account`;
CREATE TABLE `tenant_account` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `TENANT_ID` bigint(20) DEFAULT NULL COMMENT '租户id',
  `ACCOUNT_ID` bigint(20) DEFAULT NULL COMMENT '账号d',
  `IS_ADMIN` tinyint(1) DEFAULT NULL COMMENT '是否是租户管理员',
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1199893220994179121 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of tenant_account
-- ----------------------------
BEGIN;
INSERT INTO `tenant_account` VALUES (1199893220994179080, 1111111672565051234, 123456, 1);
COMMIT;

-- ----------------------------
-- Table structure for tenant_type
-- ----------------------------
DROP TABLE IF EXISTS `tenant_type`;
CREATE TABLE `tenant_type` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `NAME` varchar(50) DEFAULT NULL COMMENT '租户类型名称',
  `CODE` varchar(50) DEFAULT NULL COMMENT '租户类型编码',
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of tenant_type
-- ----------------------------
BEGIN;
INSERT INTO `tenant_type` VALUES (1, '系统管理组织', 'admin');
INSERT INTO `tenant_type` VALUES (2, '闪聚支付-运营商', 'shanju-operation');
INSERT INTO `tenant_type` VALUES (3, '闪聚支付-签约商户', 'shanju-merchant');
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
