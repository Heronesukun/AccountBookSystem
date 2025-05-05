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

 Date: 05/05/2025 16:25:22
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for fm_account_category
-- ----------------------------
DROP TABLE IF EXISTS `fm_account_category`;
CREATE TABLE `fm_account_category`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '分类名称',
  `type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '分类类型：ASSET(资产)、LIABILITY(负债)',
  `parent_id` bigint(20) NULL DEFAULT NULL COMMENT '父分类ID，NULL表示一级分类',
  `level` int(11) NOT NULL DEFAULT 1 COMMENT '分类级别：1(一级分类)、2(二级分类)',
  `icon` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '分类图标',
  `sort_order` int(11) NULL DEFAULT 0 COMMENT '排序顺序',
  `book_id` bigint(20) NOT NULL COMMENT '所属账本ID',
  `user_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户ID',
  `asset_amount` decimal(12, 2) NULL DEFAULT 0.00 COMMENT '资产金额',
  `include_in_net_worth` tinyint(1) NULL DEFAULT 1 COMMENT '是否计入净资产',
  `is_default` tinyint(1) NULL DEFAULT 0 COMMENT '是否默认分类',
  `is_system` tinyint(1) NULL DEFAULT 0 COMMENT '是否系统预设分类',
  `is_deleted` tinyint(1) NULL DEFAULT 0 COMMENT '是否删除',
  `created_at` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_parent_id`(`parent_id`) USING BTREE,
  INDEX `idx_book_id`(`book_id`) USING BTREE,
  INDEX `idx_user_id`(`user_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 25 CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of fm_account_category
-- ----------------------------
INSERT INTO `fm_account_category` VALUES (1, '现金账户', 'ASSET', NULL, 1, 'cash_icon', 1, 1, '1', 0.00, 1, 1, 1, 0, '2025-03-17 16:45:12', '2025-03-17 18:13:23');
INSERT INTO `fm_account_category` VALUES (2, '储蓄账户', 'ASSET', NULL, 1, 'savings_icon', 2, 1, '1', 0.00, 1, 1, 1, 0, '2025-03-17 16:45:12', '2025-03-17 18:13:23');
INSERT INTO `fm_account_category` VALUES (3, '投资账户', 'ASSET', NULL, 1, 'investment_icon', 3, 1, '1', 0.00, 1, 1, 1, 0, '2025-03-17 16:45:12', '2025-03-17 18:13:23');
INSERT INTO `fm_account_category` VALUES (4, '数字账户', 'ASSET', NULL, 1, 'digital_icon', 4, 1, '1', 0.00, 1, 1, 1, 0, '2025-03-17 16:45:12', '2025-03-17 18:13:23');
INSERT INTO `fm_account_category` VALUES (5, '工资卡', 'ASSET', 1, 2, 'salary_card_icon', 1, 1, '1', 5000.00, 1, 1, 1, 0, '2025-03-17 16:45:12', '2025-03-17 18:13:23');
INSERT INTO `fm_account_category` VALUES (6, '现金钱包', 'ASSET', 1, 2, 'wallet_icon', 2, 1, '1', 1000.00, 1, 1, 1, 0, '2025-03-17 16:45:12', '2025-03-17 18:13:23');
INSERT INTO `fm_account_category` VALUES (7, '工行储蓄卡', 'ASSET', 2, 2, 'icbc_icon', 1, 1, '1', 10000.00, 1, 1, 1, 0, '2025-03-17 16:45:12', '2025-03-17 18:13:23');
INSERT INTO `fm_account_category` VALUES (8, '建行储蓄卡', 'ASSET', 2, 2, 'ccb_icon', 2, 1, '1', 8000.00, 1, 1, 1, 0, '2025-03-17 16:45:12', '2025-03-17 18:13:23');
INSERT INTO `fm_account_category` VALUES (9, '农行储蓄卡', 'ASSET', 2, 2, 'abc_icon', 3, 1, '1', 5000.00, 1, 1, 1, 0, '2025-03-17 16:45:12', '2025-03-17 18:13:23');
INSERT INTO `fm_account_category` VALUES (10, '股票账户', 'ASSET', 3, 2, 'stock_icon', 1, 1, '1', 20000.00, 1, 1, 1, 0, '2025-03-17 16:45:12', '2025-03-17 18:13:23');
INSERT INTO `fm_account_category` VALUES (11, '基金账户', 'ASSET', 3, 2, 'fund_icon', 2, 1, '1', 15000.00, 1, 1, 1, 0, '2025-03-17 16:45:12', '2025-03-17 18:13:23');
INSERT INTO `fm_account_category` VALUES (12, '理财产品', 'ASSET', 3, 2, 'finance_icon', 3, 1, '1', 30000.00, 1, 1, 1, 0, '2025-03-17 16:45:12', '2025-03-17 18:13:23');
INSERT INTO `fm_account_category` VALUES (13, '支付宝', 'ASSET', 4, 2, 'alipay_icon', 1, 1, '1', 1726.00, 1, 1, 1, 0, '2025-03-17 16:45:12', '2025-05-03 15:58:49');
INSERT INTO `fm_account_category` VALUES (14, '微信钱包', 'ASSET', 4, 2, 'wechat_icon', 2, 1, '1', 2000.00, 1, 1, 1, 0, '2025-03-17 16:45:12', '2025-03-17 18:13:23');
INSERT INTO `fm_account_category` VALUES (15, '信用账户', 'LIABILITY', NULL, 1, 'credit_icon', 1, 1, '1', 0.00, 1, 1, 1, 0, '2025-03-17 16:45:12', '2025-03-17 18:13:23');
INSERT INTO `fm_account_category` VALUES (16, '贷款账户', 'LIABILITY', NULL, 1, 'loan_icon', 2, 1, '1', 0.00, 1, 1, 1, 0, '2025-03-17 16:45:12', '2025-03-17 18:13:23');
INSERT INTO `fm_account_category` VALUES (17, '其他负债', 'LIABILITY', NULL, 1, 'other_liability_icon', 3, 1, '1', 0.00, 1, 1, 1, 0, '2025-03-17 16:45:12', '2025-03-17 18:13:23');
INSERT INTO `fm_account_category` VALUES (18, '中行信用卡', 'LIABILITY', 15, 2, 'boc_icon', 1, 1, '1', -2000.00, 1, 1, 1, 0, '2025-03-17 16:45:12', '2025-03-17 18:32:44');
INSERT INTO `fm_account_category` VALUES (19, '工行信用卡', 'LIABILITY', 15, 2, 'icbc_credit_icon', 2, 1, '1', -1500.00, 1, 1, 1, 0, '2025-03-17 16:45:12', '2025-03-17 18:32:46');
INSERT INTO `fm_account_category` VALUES (20, '招行信用卡', 'LIABILITY', 15, 2, 'cmb_icon', 3, 1, '1', -3000.00, 1, 1, 1, 0, '2025-03-17 16:45:12', '2025-03-17 18:32:47');
INSERT INTO `fm_account_category` VALUES (21, '房贷', 'LIABILITY', 16, 2, 'house_loan_icon', 1, 1, '1', -500000.00, 1, 1, 1, 0, '2025-03-17 16:45:12', '2025-03-17 18:32:54');
INSERT INTO `fm_account_category` VALUES (22, '车贷', 'LIABILITY', 16, 2, 'car_loan_icon', 2, 1, '1', -80000.00, 1, 1, 1, 0, '2025-03-17 16:45:12', '2025-03-17 18:32:59');
INSERT INTO `fm_account_category` VALUES (23, '消费贷', 'LIABILITY', 16, 2, 'consumer_loan_icon', 3, 1, '1', -20000.00, 1, 1, 1, 0, '2025-03-17 16:45:12', '2025-03-17 18:33:02');
INSERT INTO `fm_account_category` VALUES (24, '花呗', 'LIABILITY', 16, 2, '', 0, 1, '1', 200.00, 1, 0, 0, 0, '2025-03-17 18:31:56', '2025-05-01 17:38:24');

SET FOREIGN_KEY_CHECKS = 1;
