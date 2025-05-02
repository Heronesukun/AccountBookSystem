/*
 Navicat Premium Data Transfer

 Source Server         : alibabaNet
 Source Server Type    : MySQL
 Source Server Version : 50744
 Source Host           : 47.98.59.0:3306
 Source Schema         : fm_project

 Target Server Type    : MySQL
 Target Server Version : 50744
 File Encoding         : 65001

 Date: 02/05/2025 15:27:30
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for fm_general_table
-- ----------------------------
DROP TABLE IF EXISTS `fm_general_table`;
CREATE TABLE `fm_general_table`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '总表id',
  `bid` bigint(20) NULL DEFAULT NULL COMMENT '对应账单id',
  `user_id` bigint(20) NULL DEFAULT NULL COMMENT '对应用户id',
  `account_category_id` bigint(20) NULL DEFAULT NULL COMMENT '所属账户id',
  `merchant_id` bigint(20) NULL DEFAULT NULL COMMENT '商家id',
  `category_id` bigint(20) NULL DEFAULT NULL COMMENT '分类id',
  `amount` double NULL DEFAULT NULL COMMENT '金额',
  `date` datetime NULL DEFAULT NULL COMMENT '日期时间',
  `desc` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '分类',
  `reser2` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备用字段2',
  `is_deleted` bigint(255) NULL DEFAULT NULL COMMENT '是否删除',
  `transfer_account_id` bigint(255) NULL DEFAULT NULL COMMENT '接收转账账户id',
  `user_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '记账用户名',
  `account_category_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '记账账户名',
  `merchant_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '商家名',
  `category_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '分类名',
  `general_icon` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '图标',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 22 CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of fm_general_table
-- ----------------------------
INSERT INTO `fm_general_table` VALUES (1, 1, 1, NULL, NULL, 1, 12, '2025-05-01 10:57:11', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `fm_general_table` VALUES (2, 1, 1, NULL, NULL, 1, 12, '2025-05-01 11:00:44', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `fm_general_table` VALUES (3, 1, 1, NULL, NULL, 1, 12, '2025-05-01 11:00:47', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `fm_general_table` VALUES (4, 1, 1, NULL, NULL, 1, 12, '2025-05-01 11:00:54', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `fm_general_table` VALUES (5, 1, 1, NULL, NULL, NULL, NULL, '2025-05-01 11:08:49', NULL, NULL, NULL, 0, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `fm_general_table` VALUES (6, 1, 1, NULL, NULL, 3, 99, '2025-05-01 11:11:46', NULL, NULL, NULL, 0, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `fm_general_table` VALUES (7, 1, 1, NULL, NULL, 3, 14, '2025-05-01 11:12:28', NULL, NULL, NULL, 0, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `fm_general_table` VALUES (8, 1, 1, NULL, NULL, 3, 14, '2025-05-01 11:12:30', NULL, NULL, NULL, 0, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `fm_general_table` VALUES (9, 1, 1, NULL, NULL, NULL, 233, '2025-05-01 11:15:00', NULL, NULL, NULL, 0, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `fm_general_table` VALUES (10, 1, 1, 24, NULL, 2, 14, '2025-05-01 13:28:26', '兰州牛肉面\n', NULL, NULL, 0, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `fm_general_table` VALUES (11, 1, 1, 18, 2, 2, 9.9, '2025-05-01 13:31:00', '原味鸡两块', NULL, NULL, 0, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `fm_general_table` VALUES (12, 1, 1, 13, 2, 2, 88, '2025-05-01 14:40:42', '疯狂星期四\n', NULL, NULL, 0, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `fm_general_table` VALUES (13, 1, 1, 13, NULL, 9, 255, '2025-05-01 14:41:45', '123', NULL, NULL, 0, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `fm_general_table` VALUES (14, 1, 1, 13, NULL, NULL, NULL, '2025-05-01 16:12:17', '', NULL, NULL, 0, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `fm_general_table` VALUES (15, 1, 1, 13, NULL, NULL, NULL, '2025-05-01 16:12:23', '', NULL, NULL, 0, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `fm_general_table` VALUES (16, 1, 1, 13, NULL, NULL, NULL, '2025-05-01 16:12:26', '', NULL, NULL, 0, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `fm_general_table` VALUES (17, 1, 1, 13, NULL, NULL, NULL, '2025-05-01 16:13:41', '', NULL, NULL, 0, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `fm_general_table` VALUES (18, 1, 1, 13, 5, 3, -500, '2025-05-01 08:00:00', '', '支出', NULL, 0, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `fm_general_table` VALUES (19, 1, 1, 13, NULL, NULL, 200, '2025-05-01 08:00:00', '', '转账', NULL, 0, 24, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `fm_general_table` VALUES (20, 1, 1, 13, 1, 4, -278, '2025-06-01 08:00:00', '', '支出', NULL, 0, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `fm_general_table` VALUES (21, 1, 1, 13, NULL, 3, -1000, '2025-04-01 08:00:00', '', '支出', NULL, 0, NULL, NULL, NULL, NULL, NULL, NULL);

SET FOREIGN_KEY_CHECKS = 1;
