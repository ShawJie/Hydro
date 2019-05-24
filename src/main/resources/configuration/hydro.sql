SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for hy_article
-- ----------------------------
DROP TABLE IF EXISTS `hy_article`;
CREATE TABLE `hy_article`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `uid` int(11) NOT NULL,
  `create_date` datetime(0) NOT NULL,
  `view_count` int(11) DEFAULT NULL,
  `title` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `markdown_path` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `html_path` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `cid` int(11) DEFAULT NULL,
  `is_publicise` tinyint(1) NOT NULL,
  `excerpt` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for hy_article_tag
-- ----------------------------
DROP TABLE IF EXISTS `hy_article_tag`;
CREATE TABLE `hy_article_tag`  (
  `aid` int(11) DEFAULT NULL COMMENT 'forigin key with article id',
  `tid` int(11) DEFAULT NULL COMMENT 'forigin key with article id'
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for hy_category
-- ----------------------------
DROP TABLE IF EXISTS `hy_category`;
CREATE TABLE `hy_category`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `category_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `description` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for hy_media
-- ----------------------------
DROP TABLE IF EXISTS `hy_media`;
CREATE TABLE `hy_media`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `file_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `file_path` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `upload_date` datetime(0) DEFAULT NULL,
  `file_type` int(11) DEFAULT NULL,
  `description` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for hy_page
-- ----------------------------
DROP TABLE IF EXISTS `hy_page`;
CREATE TABLE `hy_page`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `page_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `route_path` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `page_path` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `is_publish` tinyint(1) DEFAULT NULL,
  `release_date` datetime(0) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for hy_setting
-- ----------------------------
DROP TABLE IF EXISTS `hy_setting`;
CREATE TABLE `hy_setting`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `item_name` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `item_value` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `is_system_variable` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `item_name`(`item_name`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for hy_tag
-- ----------------------------
DROP TABLE IF EXISTS `hy_tag`;
CREATE TABLE `hy_tag`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `tag_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `description` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for hy_user
-- ----------------------------
DROP TABLE IF EXISTS `hy_user`;
CREATE TABLE `hy_user`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_name` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `account` varchar(15) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `password` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `avator` varchar(40) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `github` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `email` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `activated` tinyint(1) DEFAULT NULL,
  `group_set` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `last_login_date` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0),
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for hy_visit_info
-- ----------------------------
DROP TABLE IF EXISTS `hy_visit_info`;
CREATE TABLE `hy_visit_info`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `ip` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `request_path` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `datetime` datetime(0) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;

-- ----------------------------
-- Initial database table data
-- ----------------------------

INSERT INTO `hy_setting`(`id`, `item_name`, `item_value`, `is_system_variable`) VALUES (DEFAULT, 'SiteTitle', 'Hydro', 1);
INSERT INTO `hy_setting`(`id`, `item_name`, `item_value`, `is_system_variable`) VALUES (DEFAULT, 'TagLine', 'Here is Hydro', 1);
INSERT INTO `hy_setting`(`id`, `item_name`, `item_value`, `is_system_variable`) VALUES (DEFAULT, 'Language', '', 1);
INSERT INTO `hy_media`(`id`, `file_name`, `file_path`, `upload_date`, `file_type`, `description`) VALUES (DEFAULT, 'avator', 'head.jpg', sysdate(), 2, '');
INSERT INTO `hy_article`(`id`, `uid`, `create_date`, `view_count`, `title`, `markdown_path`, `html_path`, `cid`, `is_publicise`, `excerpt`) VALUES (15, 2, sysdate(), 1, 'Hello World !', 'Hello World !/Hello World !.md', 'Hello World !/Hello World !.html', NULL, 0, '<p>welcome to hydro, here is your first article, edit it, and start now</p>');


