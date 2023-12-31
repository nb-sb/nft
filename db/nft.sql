/*
 Navicat Premium Data Transfer

 Source Server         : root
 Source Server Type    : MySQL
 Source Server Version : 80032
 Source Host           : localhost:3306
 Source Schema         : nft

 Target Server Type    : MySQL
 Target Server Version : 80032
 File Encoding         : 65001

 Date: 09/01/2024 15:11:30
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for nft_detail_info
-- ----------------------------
DROP TABLE IF EXISTS `nft_detail_info`;
CREATE TABLE `nft_detail_info`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `hash` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '藏品hash',
  `transfer_address` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '转移方用户地址',
  `target_address` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '接受方用户地址',
  `type` tinyint NULL DEFAULT NULL COMMENT '0 表示转增，1表示购买 ||用于藏品来源显示',
  `time` datetime NULL DEFAULT NULL COMMENT '时间',
  `digital_collection_id` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '数字藏品编号\r\n例如 1#5000 或 51#5000 等也就是id和总数进行拼接',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 17 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for nft_metas
-- ----------------------------
DROP TABLE IF EXISTS `nft_metas`;
CREATE TABLE `nft_metas`  (
  `mid` int NOT NULL AUTO_INCREMENT COMMENT '藏品分类id',
  `conllection_name` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '藏品分类名称',
  `conllection_slug` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '藏品分类代号',
  `count` int NULL DEFAULT NULL COMMENT '该分类下藏品总数',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`mid`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for nft_order_info
-- ----------------------------
DROP TABLE IF EXISTS `nft_order_info`;
CREATE TABLE `nft_order_info`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `order_no` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '订单id',
  `user_id` int NULL DEFAULT NULL COMMENT '用户id',
  `product_id` int NULL DEFAULT NULL COMMENT '藏品id',
  `product_img` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `product_name` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `product_price` decimal(10, 2) NULL DEFAULT NULL COMMENT '产品价格',
  `seckill_price` decimal(10, 2) NULL DEFAULT NULL COMMENT '秒杀价格',
  `status` tinyint(1) UNSIGNED ZEROFILL NULL DEFAULT NULL COMMENT '0是创建完成订单，1是未支付，2是已支付，3是藏品已经到账，4是取消订单，5是已经退款',
  `init_date` datetime NULL DEFAULT NULL COMMENT '订单创建时间',
  `pay_date` datetime NULL DEFAULT NULL COMMENT '支付时间',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`, `order_no`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for nft_owner_ship
-- ----------------------------
DROP TABLE IF EXISTS `nft_owner_ship`;
CREATE TABLE `nft_owner_ship`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `address` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '所属用户地址',
  `time` datetime NULL DEFAULT NULL COMMENT '获得时间',
  `hash` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '数字藏品hash',
  `type` tinyint(1) NULL DEFAULT NULL COMMENT '获得类型 type ||  0 表示转增，1表示购买',
  `digital_collection_id` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '数字藏品编号',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `hash`(`hash` ASC) USING BTREE,
  CONSTRAINT `hash` FOREIGN KEY (`hash`) REFERENCES `nft_sell_info` (`ipfs_hash`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 14 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for nft_relationships
-- ----------------------------
DROP TABLE IF EXISTS `nft_relationships`;
CREATE TABLE `nft_relationships`  (
  `cid` int NOT NULL COMMENT '藏品id',
  `mid` smallint NOT NULL COMMENT '分类id',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`cid`) USING BTREE,
  INDEX `mid`(`mid` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for nft_sell_info
-- ----------------------------
DROP TABLE IF EXISTS `nft_sell_info`;
CREATE TABLE `nft_sell_info`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `unique_id` int NULL DEFAULT NULL COMMENT '提交的缓存表的对应的id',
  `hash` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '藏品hash',
  `amount` int NULL DEFAULT NULL COMMENT '发行量',
  `remain` int NULL DEFAULT NULL COMMENT '剩余数量',
  `auther` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '数字藏品作者地址',
  `status` tinyint(1) NULL DEFAULT NULL COMMENT '# 1 为正常 ，  0 为闭售',
  `ipfs_hash` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT 'ipfs中的hash',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `hash`(`hash` ASC) USING BTREE,
  INDEX `ipfs_hash`(`ipfs_hash` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 17 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for nft_submit_cache
-- ----------------------------
DROP TABLE IF EXISTS `nft_submit_cache`;
CREATE TABLE `nft_submit_cache`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `path` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '文件路径',
  `total` int NULL DEFAULT NULL COMMENT '出售数量',
  `present` text CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL COMMENT '介绍',
  `name` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '藏品姓名',
  `hash` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '藏品hash , 审核阶段并无ifpfs hash',
  `author_id` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '作者 所属id',
  `author_address` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '作者 区块链账户地址',
  `price` decimal(10, 2) NULL DEFAULT NULL COMMENT '售价',
  `status` tinyint(1) NULL DEFAULT NULL COMMENT '审核状态 | 0为未审核 ，1是通过，2是拒绝',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `author_address`(`author_address` ASC) USING BTREE,
  CONSTRAINT `author_address` FOREIGN KEY (`author_address`) REFERENCES `nft_user_info` (`address`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 15 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for nft_user_detal
-- ----------------------------
DROP TABLE IF EXISTS `nft_user_detal`;
CREATE TABLE `nft_user_detal`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `for_id` int NOT NULL COMMENT '来自用户表中的id',
  `name` varchar(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '姓名 ',
  `address` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '用户区块链地址',
  `cardid` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '身份证',
  `phone_number` varchar(30) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '手机号',
  `status` tinyint(1) UNSIGNED ZEROFILL NOT NULL COMMENT '用户信息审核状态：0为待审核，1 为审核成功，2为审核退回需要重新提交',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `for_id`(`for_id` ASC) USING BTREE,
  INDEX `address`(`address` ASC) USING BTREE,
  CONSTRAINT `address` FOREIGN KEY (`address`) REFERENCES `nft_user_info` (`address`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `for_id` FOREIGN KEY (`for_id`) REFERENCES `nft_user_info` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for nft_user_info
-- ----------------------------
DROP TABLE IF EXISTS `nft_user_info`;
CREATE TABLE `nft_user_info`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `address` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `privatekey` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '私钥',
  `username` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `password` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `role` tinyint(1) UNSIGNED ZEROFILL NULL DEFAULT NULL COMMENT '用户权限，普通用户为0，管理员为1',
  `balance` decimal(10, 2) UNSIGNED ZEROFILL NOT NULL COMMENT '用户余额',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `address`(`address` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 10 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
