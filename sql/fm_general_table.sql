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

 Date: 28/04/2025 19:38:06
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
  `desc` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL COMMENT '备注',
  `reser1` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL COMMENT '备用字段1',
  `reser2` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL COMMENT '备用字段2',
  `reser3` bigint(255) NULL DEFAULT NULL COMMENT '备用字段3',
  `reser4` bigint(255) NULL DEFAULT NULL COMMENT '备用字段4',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of fm_general_table
-- ----------------------------

SET FOREIGN_KEY_CHECKS = 1;
