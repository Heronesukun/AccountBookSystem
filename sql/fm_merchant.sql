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

 Date: 05/05/2025 16:25:54
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for fm_merchant
-- ----------------------------
DROP TABLE IF EXISTS `fm_merchant`;
CREATE TABLE `fm_merchant`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '商家名称',
  `category_id` bigint(20) NULL DEFAULT NULL COMMENT '关联的分类ID',
  `logo` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '商家logo',
  `address` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '商家地址',
  `contact` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '联系方式',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '商家描述',
  `sort_order` int(11) NULL DEFAULT 0 COMMENT '排序顺序',
  `book_id` bigint(20) NOT NULL COMMENT '所属账本ID',
  `user_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户ID',
  `is_default` tinyint(1) NULL DEFAULT 0 COMMENT '是否默认商家',
  `is_system` tinyint(1) NULL DEFAULT 0 COMMENT '是否系统预设商家',
  `is_deleted` tinyint(1) NULL DEFAULT 0 COMMENT '是否删除',
  `created_at` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_category_id`(`category_id`) USING BTREE,
  INDEX `idx_book_id`(`book_id`) USING BTREE,
  INDEX `idx_user_id`(`user_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of fm_merchant
-- ----------------------------
INSERT INTO `fm_merchant` VALUES (1, '沃尔玛超市', 2, 'walmart_logo', NULL, NULL, NULL, 1, 1, '1', 1, 1, 0, '2025-03-18 11:04:11', '2025-03-20 20:02:17');
INSERT INTO `fm_merchant` VALUES (2, '肯德基', 2, 'kfc_logo', NULL, NULL, NULL, 2, 1, '1', 1, 1, 0, '2025-03-18 11:04:11', '2025-03-18 11:04:22');
INSERT INTO `fm_merchant` VALUES (3, '麦当劳', 2, 'mcdonalds_logo', NULL, NULL, NULL, 3, 1, '1', 1, 1, 0, '2025-03-18 11:04:11', '2025-03-18 11:04:22');
INSERT INTO `fm_merchant` VALUES (4, '星巴克', 2, 'starbucks_logo', NULL, NULL, NULL, 4, 1, '1', 1, 1, 0, '2025-03-18 11:04:11', '2025-03-18 11:04:22');
INSERT INTO `fm_merchant` VALUES (5, '滴滴出行', 3, 'didi_logo', NULL, NULL, NULL, 5, 1, '1', 1, 1, 0, '2025-03-18 11:04:11', '2025-03-18 11:04:22');

SET FOREIGN_KEY_CHECKS = 1;
