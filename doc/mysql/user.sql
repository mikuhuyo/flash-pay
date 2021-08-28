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
