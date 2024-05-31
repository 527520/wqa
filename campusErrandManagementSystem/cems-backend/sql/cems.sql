/*
Navicat MySQL Data Transfer

Source Server         : localhost_3306
Source Server Version : 80023
Source Host           : localhost:3306
Source Database       : cems

Target Server Type    : MYSQL
Target Server Version : 80023
File Encoding         : 65001

Date: 2024-05-25 15:38:16
*/

SET FOREIGN_KEY_CHECKS=0;


CREATE DATABASE cems;

-- ----------------------------
-- Table structure for address
-- ----------------------------
DROP TABLE IF EXISTS `address`;
CREATE TABLE `address` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `user_id` bigint NOT NULL COMMENT '用户id',
  `address` varchar(255) NOT NULL DEFAULT '' COMMENT '地址',
  `consignee` varchar(255) NOT NULL DEFAULT '' COMMENT '收货人',
  `phone_number` varchar(255) NOT NULL DEFAULT '' COMMENT '收货人手机号',
  `is_default` tinyint NOT NULL DEFAULT '0' COMMENT '是否为默认地址',
  PRIMARY KEY (`id`),
  KEY `address_user_1` (`user_id`),
  CONSTRAINT `address_user_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB AUTO_INCREMENT=1771051856626208771 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Table structure for announcement
-- ----------------------------
DROP TABLE IF EXISTS `announcement`;
CREATE TABLE `announcement` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `title` varchar(255) NOT NULL DEFAULT '' COMMENT '公告标题',
  `content` text NOT NULL COMMENT '公告内容',
  `publish_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '发布时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_active` tinyint NOT NULL DEFAULT '1' COMMENT '是否有效',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1770472689903906818 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Table structure for become_deliveryman
-- ----------------------------
DROP TABLE IF EXISTS `become_deliveryman`;
CREATE TABLE `become_deliveryman` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `user_id` bigint NOT NULL COMMENT '用户id',
  `id_number` varchar(255) NOT NULL DEFAULT '' COMMENT '身份证号',
  `phone_number` varchar(255) NOT NULL DEFAULT '' COMMENT '手机号码',
  `name` varchar(255) NOT NULL DEFAULT '' COMMENT '姓名',
  `sex` varchar(255) NOT NULL DEFAULT '' COMMENT '性别',
  `birthday` date DEFAULT NULL COMMENT '出生日期',
  `id_card_front_image` varchar(255) NOT NULL DEFAULT '' COMMENT '身份证正面照',
  `id_card_back_image` varchar(255) NOT NULL DEFAULT '' COMMENT '身份证反面照',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `status` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '已提交' COMMENT '状态',
  `message` varchar(255) NOT NULL DEFAULT '' COMMENT '如果未通过展示给用户的消息',
  `isDelete` tinyint NOT NULL DEFAULT '0' COMMENT '是否删除',
  PRIMARY KEY (`id`),
  KEY `a2` (`user_id`),
  CONSTRAINT `a2` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB AUTO_INCREMENT=1770729571134582787 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Table structure for deliveryman
-- ----------------------------
DROP TABLE IF EXISTS `deliveryman`;
CREATE TABLE `deliveryman` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '配送员id',
  `user_id` bigint NOT NULL COMMENT '用户id',
  `id_number` varchar(255) NOT NULL DEFAULT '' COMMENT '身份证号',
  `phone_number` varchar(255) DEFAULT '' COMMENT '手机号码',
  `name` varchar(255) NOT NULL DEFAULT '' COMMENT '姓名',
  `sex` varchar(255) NOT NULL DEFAULT '' COMMENT '性别',
  `birthday` date DEFAULT NULL COMMENT '出生日期',
  `id_card_front_image` varchar(255) NOT NULL DEFAULT '' COMMENT '身份证正面照',
  `id_card_back_image` varchar(255) NOT NULL DEFAULT '' COMMENT '身份证反面照',
  `is_online` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '否' COMMENT '是否在线',
  `accepting_orders` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '离线' COMMENT '可接单状态(离线，未接单，已接单)',
  `status` varchar(255) NOT NULL DEFAULT '正常' COMMENT '账号状态(正常，暂停接单，离职)',
  `completed_orders` int NOT NULL DEFAULT '0' COMMENT '完成订单数',
  `average_rating` decimal(4,2) NOT NULL DEFAULT '10.00' COMMENT '平均评分',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `isDelete` tinyint NOT NULL DEFAULT '0' COMMENT '是否删除',
  PRIMARY KEY (`id`),
  KEY `a1` (`user_id`),
  CONSTRAINT `a1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB AUTO_INCREMENT=1770729571134582787 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Table structure for order
-- ----------------------------
DROP TABLE IF EXISTS `order`;
CREATE TABLE `order` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `user_id` bigint NOT NULL COMMENT '下单用户id',
  `deliveryman_id` bigint NOT NULL COMMENT '配送员id',
  `task_id` bigint NOT NULL COMMENT '任务id',
  `status` varchar(255) NOT NULL DEFAULT '未支付' COMMENT '订单状态(未支付，已支付，已完成，已取消)',
  `order_time` datetime NOT NULL COMMENT '下单时间',
  `fetch_address` varchar(255) NOT NULL COMMENT '取件地址',
  `shipping_address` varchar(255) NOT NULL COMMENT '收货地址',
  `receiver_name` varchar(255) NOT NULL COMMENT '收货人姓名',
  `receiver_phone_number` varchar(255) NOT NULL COMMENT '收货人电话',
  `order_amount` decimal(10,2) NOT NULL COMMENT '订单金额',
  `order_note` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '' COMMENT '订单备注',
  `estimated_completion_time` datetime NOT NULL COMMENT '预计送达时间',
  `completion_time` datetime DEFAULT NULL COMMENT '实际完成时间',
  `is_delete` tinyint NOT NULL DEFAULT '0' COMMENT '是否删除',
  PRIMARY KEY (`id`),
  KEY `order_user_1` (`user_id`),
  KEY `order_deliveryman_1` (`deliveryman_id`),
  KEY `order_task_1` (`task_id`),
  CONSTRAINT `order_deliveryman_1` FOREIGN KEY (`deliveryman_id`) REFERENCES `deliveryman` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `order_task_1` FOREIGN KEY (`task_id`) REFERENCES `task` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `order_user_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB AUTO_INCREMENT=1771051407546273795 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Table structure for recruit
-- ----------------------------
DROP TABLE IF EXISTS `recruit`;
CREATE TABLE `recruit` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `title` varchar(255) NOT NULL DEFAULT '' COMMENT '标题',
  `content` text NOT NULL COMMENT '招聘内容',
  `publish_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '发布时间',
  `status` tinyint NOT NULL DEFAULT '0' COMMENT '招聘状态(0：招聘中，1：招聘结束)',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1776205907399647234 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Table structure for review
-- ----------------------------
DROP TABLE IF EXISTS `review`;
CREATE TABLE `review` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `order_id` bigint NOT NULL COMMENT '订单id',
  `reviewer_id` bigint NOT NULL COMMENT '评价者id',
  `reviewed_id` bigint NOT NULL COMMENT '被评价者id',
  `rating` decimal(4,2) NOT NULL DEFAULT '10.00' COMMENT '评分',
  `comment` varchar(1024) NOT NULL DEFAULT '' COMMENT '评价内容',
  `image` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '图片',
  `review_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '评价时间',
  `is_delete` tinyint NOT NULL DEFAULT '0' COMMENT '是否删除',
  PRIMARY KEY (`id`),
  KEY `review_order_1` (`order_id`),
  KEY `review_user_1` (`reviewer_id`),
  KEY `review_deliveryman_1` (`reviewed_id`),
  CONSTRAINT `review_deliveryman_1` FOREIGN KEY (`reviewed_id`) REFERENCES `deliveryman` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `review_order_1` FOREIGN KEY (`order_id`) REFERENCES `order` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `review_user_1` FOREIGN KEY (`reviewer_id`) REFERENCES `user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB AUTO_INCREMENT=1793940146282868739 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Table structure for task
-- ----------------------------
DROP TABLE IF EXISTS `task`;
CREATE TABLE `task` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `user_id` bigint NOT NULL COMMENT '发布的用户id',
  `deliveryman_id` bigint DEFAULT NULL COMMENT '接单配送员id',
  `service_type` varchar(255) NOT NULL DEFAULT '' COMMENT '服务类型',
  `item_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '物品名称',
  `fetch_address` varchar(255) NOT NULL DEFAULT '' COMMENT '取件地址',
  `shipping_address` varchar(255) NOT NULL DEFAULT '' COMMENT '收货地址',
  `item_weight` varchar(255) NOT NULL DEFAULT '小于5公斤' COMMENT '物品重量',
  `description` varchar(255) NOT NULL COMMENT '描述信息',
  `status` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '待接单' COMMENT '任务状态(待接单，已接单，配送中，已送达，已完成，已评论，已取消)',
  `publish_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '发布时间',
  `estimated_completion_time` datetime DEFAULT NULL COMMENT '用户期望完成时间',
  `completion_time` datetime DEFAULT NULL COMMENT '实际完成时间',
  `price` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '任务价格',
  `is_deleted` tinyint NOT NULL DEFAULT '0' COMMENT '是否删除',
  PRIMARY KEY (`id`),
  KEY `task_user_1` (`user_id`),
  KEY `task_deliveryman_1` (`deliveryman_id`),
  CONSTRAINT `task_deliveryman_1` FOREIGN KEY (`deliveryman_id`) REFERENCES `deliveryman` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `task_user_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB AUTO_INCREMENT=1771052155944325122 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '用户id',
  `user_account` varchar(255) NOT NULL DEFAULT '' COMMENT '用户名',
  `password` varchar(255) NOT NULL DEFAULT '' COMMENT '密码',
  `username` varchar(255) NOT NULL DEFAULT '' COMMENT '用户姓名',
  `phone_number` varchar(255) NOT NULL DEFAULT '' COMMENT '手机号',
  `user_avatar` varchar(255) NOT NULL DEFAULT '' COMMENT '头像',
  `role` varchar(255) NOT NULL DEFAULT 'user' COMMENT '角色',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `isDelete` tinyint NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1768297744050425859 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Event structure for cancel_unaccepted_tasks
-- ----------------------------
DROP EVENT IF EXISTS `cancel_unaccepted_tasks`;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` EVENT `cancel_unaccepted_tasks` ON SCHEDULE EVERY 15 MINUTE STARTS '2024-03-10 19:35:03' ON COMPLETION NOT PRESERVE ENABLE DO BEGIN
    UPDATE task SET status = '已取消' WHERE estimated_completion_time <= NOW() AND status = '待接单';
END
;;
DELIMITER ;
