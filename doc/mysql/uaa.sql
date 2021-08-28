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
