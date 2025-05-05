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

 Date: 05/05/2025 16:27:29
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for fm_user
-- ----------------------------
DROP TABLE IF EXISTS `fm_user`;
CREATE TABLE `fm_user`  (
  `user_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '用户编号',
  `username` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户名',
  `real_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '真实姓名',
  `user_pass` char(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户密码',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `modify_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '最后修改时间',
  `user_status` int(11) NULL DEFAULT 1 COMMENT '用户状态',
  `dept_id` bigint(20) NULL DEFAULT NULL COMMENT '部门编号',
  PRIMARY KEY (`user_id`) USING BTREE,
  UNIQUE INDEX `username`(`username`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户列表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of fm_user
-- ----------------------------
INSERT INTO `fm_user` VALUES (1, 'dachui', '大锤', 'e10adc3949ba59abbe56e057f20f883e', '2024-10-14 10:53:42', '2024-10-14 10:53:42', 1, 1);
INSERT INTO `fm_user` VALUES (2, 'xiaowanzi', '小丸子', 'e10adc3949ba59abbe56e057f20f883e', '2024-10-14 10:53:51', '2024-10-14 10:53:51', 1, 2);
INSERT INTO `fm_user` VALUES (3, 'zhangsan', 'zs', 'e10adc3949ba59abbe56e057f20f883e', '2024-10-23 20:02:18', '2024-10-23 20:02:18', 1, 3);

SET FOREIGN_KEY_CHECKS = 1;
