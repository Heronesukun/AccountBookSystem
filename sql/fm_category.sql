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

 Date: 05/05/2025 16:25:28
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for fm_category
-- ----------------------------
DROP TABLE IF EXISTS `fm_category`;
CREATE TABLE `fm_category`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '分类名称',
  `type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '分类类型：INCOME(收入)、EXPENSE(支出)',
  `parent_id` bigint(20) NULL DEFAULT NULL COMMENT '父分类ID，NULL表示一级分类',
  `level` int(11) NOT NULL DEFAULT 1 COMMENT '分类级别：1(一级分类)、2(二级分类)',
  `icon` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '分类图标',
  `sort_order` int(11) NULL DEFAULT 0 COMMENT '排序顺序',
  `book_id` bigint(20) NOT NULL COMMENT '所属账本ID',
  `user_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户ID',
  `is_default` tinyint(1) NULL DEFAULT 0 COMMENT '是否默认分类',
  `is_system` tinyint(1) NULL DEFAULT 0 COMMENT '是否系统预设分类',
  `is_deleted` tinyint(1) NULL DEFAULT 0 COMMENT '是否删除',
  `created_at` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_parent_id`(`parent_id`) USING BTREE,
  INDEX `idx_book_id`(`book_id`) USING BTREE,
  INDEX `idx_user_id`(`user_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 19 CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of fm_category
-- ----------------------------
INSERT INTO `fm_category` VALUES (1, '购物消费', 'EXPENSE', NULL, 1, 'shopping_icon', 1, 1, '1', 1, 1, 0, '2025-03-16 14:40:32', '2025-03-16 15:53:38');
INSERT INTO `fm_category` VALUES (2, '餐饮美食', 'EXPENSE', NULL, 1, 'food_icon', 2, 1, '1', 1, 1, 0, '2025-03-16 14:40:32', '2025-03-16 15:53:38');
INSERT INTO `fm_category` VALUES (3, '交通出行', 'EXPENSE', NULL, 1, 'transport_icon', 3, 1, '1', 1, 1, 0, '2025-03-16 14:40:32', '2025-03-16 15:53:38');
INSERT INTO `fm_category` VALUES (4, '居家生活', 'EXPENSE', NULL, 1, 'home_icon', 4, 1, '1', 1, 1, 0, '2025-03-16 14:40:32', '2025-03-16 15:53:38');
INSERT INTO `fm_category` VALUES (5, '日常用品', 'EXPENSE', 1, 2, 'daily_icon', 1, 1, '1', 1, 1, 0, '2025-03-16 14:40:32', '2025-03-16 15:53:38');
INSERT INTO `fm_category` VALUES (6, '超市购物', 'EXPENSE', 1, 2, 'supermarket_icon', 2, 1, '1', 1, 1, 0, '2025-03-16 14:40:32', '2025-03-16 15:53:39');
INSERT INTO `fm_category` VALUES (7, '洗护用品', 'EXPENSE', 1, 2, 'toiletry_icon', 3, 1, '1', 1, 1, 0, '2025-03-16 14:40:32', '2025-03-16 15:53:39');
INSERT INTO `fm_category` VALUES (8, '数码电器', 'EXPENSE', 1, 2, 'digital_icon', 4, 1, '1', 1, 1, 0, '2025-03-16 14:40:32', '2025-03-16 15:53:39');
INSERT INTO `fm_category` VALUES (9, '职业收入', 'INCOME', NULL, 1, 'salary_icon', 1, 1, '1', 1, 1, 0, '2025-03-16 14:40:32', '2025-03-16 15:53:39');
INSERT INTO `fm_category` VALUES (10, '投资理财', 'INCOME', NULL, 1, 'investment_icon', 2, 1, '1', 1, 1, 0, '2025-03-16 14:40:32', '2025-03-16 15:53:39');
INSERT INTO `fm_category` VALUES (11, '其他收入', 'INCOME', NULL, 1, 'other_income_icon', 3, 1, '1', 1, 1, 0, '2025-03-16 14:40:32', '2025-03-16 15:53:39');
INSERT INTO `fm_category` VALUES (12, '工资收入', 'INCOME', 5, 2, 'salary_icon', 1, 1, '1', 1, 1, 0, '2025-03-16 14:40:32', '2025-03-16 15:53:39');
INSERT INTO `fm_category` VALUES (13, '奖金收入', 'INCOME', 9, 2, 'bonus_icon', 2, 1, '1', 1, 1, 0, '2025-03-16 14:40:32', '2025-03-17 19:33:24');
INSERT INTO `fm_category` VALUES (14, '绩效收入', 'INCOME', 5, 2, 'performance_icon', 3, 1, '1', 1, 1, 0, '2025-03-16 14:40:32', '2025-03-16 15:53:39');
INSERT INTO `fm_category` VALUES (15, '吃谷', 'EXPENSE', 1, 2, '', 0, 1, '1', 0, 0, 0, '2025-03-16 16:23:22', '2025-03-16 16:23:22');
INSERT INTO `fm_category` VALUES (16, '工资', 'INCOME', 9, 2, '', 0, 1, '1', 0, 0, 0, '2025-03-16 16:34:12', '2025-03-16 16:34:12');
INSERT INTO `fm_category` VALUES (17, '基金', 'INCOME', 10, 2, '', 0, 1, '1', 0, 0, 0, '2025-03-16 16:34:20', '2025-03-16 16:34:20');
INSERT INTO `fm_category` VALUES (18, '地上捡的', 'INCOME', 11, 2, '', 0, 1, '1', 0, 0, 0, '2025-03-16 16:34:30', '2025-03-16 16:34:34');

SET FOREIGN_KEY_CHECKS = 1;
