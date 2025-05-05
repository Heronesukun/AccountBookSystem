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

 Date: 05/05/2025 16:25:41
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
) ENGINE = InnoDB AUTO_INCREMENT = 30 CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of fm_general_table
-- ----------------------------
INSERT INTO `fm_general_table` VALUES (24, 1, 1, 13, 1, 1, -104, '2025-04-01 08:00:00', '超市测试1', '支出', NULL, 0, NULL, '用户1', '支付宝', '沃尔玛超市', '购物消费', 'shopping_icon');
INSERT INTO `fm_general_table` VALUES (25, 1, 1, 13, 5, 3, -802, '2025-05-03 08:00:00', '打车出行吗?有点意思', '支出', NULL, 0, NULL, '用户1', '支付宝', '滴滴出行', '交通出行', 'transport_icon');
INSERT INTO `fm_general_table` VALUES (26, 1, 1, 13, NULL, 9, 3200, '2025-04-15 08:00:00', '发工资', '收入', NULL, 0, NULL, '用户1', '支付宝', NULL, '职业收入', 'salary_icon');
INSERT INTO `fm_general_table` VALUES (27, 1, 1, 13, 2, 2, -123, '2025-05-01 08:00:00', '疯狂星期四', '支出', NULL, 0, NULL, '用户1', '支付宝', '肯德基', '餐饮美食', 'food_icon');
INSERT INTO `fm_general_table` VALUES (28, 1, 1, 13, NULL, 10, 605, '2025-05-03 08:00:00', '刮刮乐', '收入', NULL, 0, NULL, '用户1', '支付宝', NULL, '投资理财', 'investment_icon');
INSERT INTO `fm_general_table` VALUES (29, 1, 1, 13, 3, 2, -88, '2025-05-03 08:00:00', '联动套餐', '支出', NULL, 0, NULL, '用户1', '支付宝', '麦当劳', '餐饮美食', 'food_icon');

SET FOREIGN_KEY_CHECKS = 1;
