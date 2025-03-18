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

 Date: 09/03/2025 23:34:08
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for book_total
-- ----------------------------
DROP TABLE IF EXISTS `book_total`;
CREATE TABLE `book_total`  (
  `bid` bigint(11) NOT NULL AUTO_INCREMENT,
  `book_master` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `participants1` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `create_time` datetime NOT NULL,
  `bname` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `b_status` int(11) NULL DEFAULT NULL,
  `participants2` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `participants3` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `participants4` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`bid`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 10 CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of book_total
-- ----------------------------
INSERT INTO `book_total` VALUES (1, 'zhangsan', 'dachui', '2025-02-23 16:43:44', 'Updated Family Book', 1, NULL, NULL, NULL);
INSERT INTO `book_total` VALUES (2, 'dachui', NULL, '2025-02-23 16:47:15', '家庭账本', 1, NULL, NULL, NULL);
INSERT INTO `book_total` VALUES (3, 'zhangsan', NULL, '2025-02-23 19:20:59', '学习账本', 1, NULL, NULL, NULL);
INSERT INTO `book_total` VALUES (6, 'dachui', 'zhangsan', '2025-02-23 19:50:51', '家庭账本本', 1, NULL, NULL, NULL);
INSERT INTO `book_total` VALUES (9, 'zhangsan', NULL, '2025-02-23 22:28:31', '测试3', 1, NULL, NULL, NULL);

SET FOREIGN_KEY_CHECKS = 1;
