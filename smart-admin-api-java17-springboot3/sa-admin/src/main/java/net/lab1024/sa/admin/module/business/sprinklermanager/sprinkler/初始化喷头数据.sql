drop table if exists `t_sprinkler`;
CREATE TABLE `t_sprinkler` (
                               `sprinkler_id` bigint NOT NULL AUTO_INCREMENT COMMENT '喷头ID',
                               `purchase_date_contract_number` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '购入日期（合同编号）',
                               `sprinkler_model` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '喷头型号',
                               `sprinkler_serial` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL UNIQUE COMMENT '喷头序列号',
                               `shipping_date` date  COMMENT '发货日期',
                               `warehouse_date` date  COMMENT '入仓日期',
                               `allocate_date` date  COMMENT '领用日期',
                               `allocate_user` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '领用人',
                               `allocate_purpose` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '领用用途',
                               `allocate_position` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '位置',
                               `voltage` float COMMENT '电压',
                               `jetsout` tinyint COMMENT 'jetsout',
                               `history` mediumtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '历史',
                               `status` tinyint NOT NULL DEFAULT '0' COMMENT '0=库存 1=使用中 2=维修中 3=破损 4=RMA',
                               `is_new` tinyint(1) NOT NULL DEFAULT '0' COMMENT '新旧喷头',
                               `sprinkler_detail` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '喷头详情',
                               `disabled_flag` tinyint(1) NOT NULL DEFAULT '0' COMMENT '禁用状态',
                               `deleted_flag` tinyint(1) NOT NULL DEFAULT '0' COMMENT '删除状态',
                               `create_user_id` bigint NOT NULL COMMENT '创建人ID',
                               `create_user_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '创建人',
                               `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                               `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                               PRIMARY KEY (`sprinkler_id`) USING BTREE,
                               KEY `idx_sprinkler_serial` (`sprinkler_serial`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='全部喷头模块\r\n';


drop table if exists `t_usable_sprinkler`;
CREATE TABLE `t_usable_sprinkler` (
                                      `sprinkler_id` bigint NOT NULL AUTO_INCREMENT COMMENT '喷头ID',
                                      `sprinkler_serial` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL UNIQUE COMMENT '喷头序列号',
                                      `ret_warehouse_date` date  COMMENT '返仓日期',
                                      `allocate_limitation` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '领用是否有限制',
                                      `allocate_note1` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '领用时备注1',
                                      `disabled_flag` tinyint(1) NOT NULL DEFAULT '0' COMMENT '禁用状态',
                                      `deleted_flag` tinyint(1) NOT NULL DEFAULT '0' COMMENT '删除状态',
                                      `create_user_id` bigint NOT NULL COMMENT '创建人ID',
                                      `create_user_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '创建人',
                                      `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                      `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                      PRIMARY KEY (`sprinkler_id`) USING BTREE,
                                      KEY `idx_sprinkler_serial` (`sprinkler_serial`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='可用仓喷头模块\r\n';

drop table if exists `t_machine_sprinkler`;
CREATE TABLE `t_machine_sprinkler` (
                                      `sprinkler_id` bigint NOT NULL AUTO_INCREMENT COMMENT '喷头ID',
                                      `sprinkler_serial` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL UNIQUE COMMENT '喷头序列号',
                                      `disabled_flag` tinyint(1) NOT NULL DEFAULT '0' COMMENT '禁用状态',
                                      `deleted_flag` tinyint(1) NOT NULL DEFAULT '0' COMMENT '删除状态',
                                      `create_user_id` bigint NOT NULL COMMENT '创建人ID',
                                      `create_user_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '创建人',
                                      `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                      `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                      PRIMARY KEY (`sprinkler_id`) USING BTREE,
                                      KEY `idx_sprinkler_serial` (`sprinkler_serial`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='机台喷头模块\r\n';

drop table if exists `t_maintaining_sprinkler`;
CREATE TABLE `t_maintaining_sprinkler` (
                                       `sprinkler_id` bigint NOT NULL AUTO_INCREMENT COMMENT '喷头ID',
                                       `sprinkler_serial` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL UNIQUE COMMENT '喷头序列号',
                                       `ret_maintainence_date` date  COMMENT '返修日期',
                                       `ret_maintainence_reason` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '返修原因',
                                       `real_reason` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '具体原因',
                                       `customer` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '返修客户',
                                       `disabled_flag` tinyint(1) NOT NULL DEFAULT '0' COMMENT '禁用状态',
                                       `deleted_flag` tinyint(1) NOT NULL DEFAULT '0' COMMENT '删除状态',
                                       `create_user_id` bigint NOT NULL COMMENT '创建人ID',
                                       `create_user_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '创建人',
                                       `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                       `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                       PRIMARY KEY (`sprinkler_id`) USING BTREE,
                                       KEY `idx_sprinkler_serial` (`sprinkler_serial`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='维修仓喷头模块\r\n';

drop table if exists `t_damaged_sprinkler`;
CREATE TABLE `t_damaged_sprinkler` (
                                           `sprinkler_id` bigint NOT NULL AUTO_INCREMENT COMMENT '喷头ID',
                                           `sprinkler_serial` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL UNIQUE COMMENT '喷头序列号',
                                           `ret_warehouse_date` date COMMENT '返仓日期',
                                           `note1` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '备注1',
                                           `damaged_reason_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '破损原因分类',
                                           `real_damaged_reason` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '具体破损原因',
                                           `disabled_flag` tinyint(1) NOT NULL DEFAULT '0' COMMENT '禁用状态',
                                           `deleted_flag` tinyint(1) NOT NULL DEFAULT '0' COMMENT '删除状态',
                                           `create_user_id` bigint NOT NULL COMMENT '创建人ID',
                                           `create_user_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '创建人',
                                           `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                           `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                           PRIMARY KEY (`sprinkler_id`) USING BTREE,
                                           KEY `idx_sprinkler_serial` (`sprinkler_serial`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='破损仓喷头模块\r\n';


drop table if exists `t_rma_sprinkler`;
CREATE TABLE `t_rma_sprinkler` (
                                       `sprinkler_id` bigint NOT NULL AUTO_INCREMENT COMMENT '喷头ID',
                                       `sprinkler_serial` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL UNIQUE COMMENT '喷头序列号',
                                       `ret_maintainence_date` date COMMENT '返修日期',
                                       `ret_maintainence_reason` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '返修原因',
                                       `ret_warehouse_date` date  COMMENT '返仓日期',
                                       `customer` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '返修客户',
                                       `rma_position` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT 'RMA地点',
                                       `warehouse_check` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '系统核对',
                                       `disabled_flag` tinyint(1) NOT NULL DEFAULT '0' COMMENT '禁用状态',
                                       `deleted_flag` tinyint(1) NOT NULL DEFAULT '0' COMMENT '删除状态',
                                       `create_user_id` bigint NOT NULL COMMENT '创建人ID',
                                       `create_user_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '创建人',
                                       `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                       `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                       PRIMARY KEY (`sprinkler_id`) USING BTREE,
                                       KEY `idx_sprinkler_serial` (`sprinkler_serial`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='rma喷头模块\r\n';
