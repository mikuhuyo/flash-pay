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
