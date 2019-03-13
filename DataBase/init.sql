
create database `dsf_center_master` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;

CREATE TABLE `dsf_site` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `site_code` varchar(12) NOT NULL COMMENT '站点名code',
  `site_name` varchar(40) DEFAULT NULL COMMENT '站点中文名',
  `schema_name` varchar(25) DEFAULT NULL COMMENT '对应数据库名（不参与业务）',
  `is_api` tinyint(4) NOT NULL DEFAULT '1',
  `currency` varchar(4) NOT NULL DEFAULT 'RMB',
  `company_id` int(11) DEFAULT NULL COMMENT '业主id',
  `start_date` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '建立时间',
  `end_date` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '过期时间',
  `available` tinyint(4) NOT NULL DEFAULT '1',
  `use_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '启用时间',
  `memo` varchar(200) DEFAULT NULL COMMENT '备注',
  `create_user` varchar(16) DEFAULT NULL COMMENT '创建者',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `modify_user` varchar(16) DEFAULT NULL COMMENT '',
  `modify_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '',
  `company_user` varchar(32) DEFAULT NULL COMMENT '业主名',
  PRIMARY KEY (`id`,`site_code`) USING BTREE,
  UNIQUE KEY `t_site_schema_uindex` (`schema_name`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT '第三方站点表';


CREATE TABLE `dsf_gm_api` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `platform_code` varchar(12) DEFAULT NULL COMMENT '三方平台code',
  `api_name` varchar(40) NOT NULL COMMENT '三方游戏平台线路名',
  `pc_url` varchar(100) NOT NULL COMMENT 'pc路径',
  `pc_url2` varchar(100) DEFAULT NULL COMMENT 'pc第二路径',
  `mb_url` varchar(100) NOT NULL COMMENT '手机路径',
  `mb_url2` varchar(100) DEFAULT NULL COMMENT '手机第二路径',
  `agy_acc` varchar(20) NOT NULL COMMENT '代理号',
  `md5_key` varchar(32) NOT NULL COMMENT '加密码',
  `secure_code` text COMMENT '完整第三方给你线路json',
  `web_name` varchar(60) DEFAULT NULL COMMENT '',
  `proxy_fore` varchar(20) NOT NULL COMMENT '是否有代理',
  `sort_id` int(11) DEFAULT NULL COMMENT '排序号',
  `memo` varchar(200) DEFAULT NULL COMMENT '备注',
  `available` tinyint(4) NOT NULL COMMENT '是否可用',
  `create_user` varchar(16) NOT NULL COMMENT '',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '',
  `modify_user` varchar(16) DEFAULT NULL COMMENT '',
  `modify_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '',
  `def_api` tinyint(4) DEFAULT '1' COMMENT '是否是api或包网 0位api 1为包网',
  PRIMARY KEY (`id`),
  UNIQUE KEY `t_gm_api_name` (`api_name`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT '';


CREATE TABLE `dsf_gm_api_prefix` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `api_id` int(12) NOT NULL COMMENT 'dsf_gm_api线路id',
  `prefix` varchar(40) NOT NULL COMMENT '定义的线路前缀',
  `site_id` int(11) DEFAULT NULL COMMENT '定义的站点ID',
  `available` tinyint(4) NOT NULL DEFAULT '1' COMMENT '是否可用',
  `create_user` varchar(255) DEFAULT NULL ,
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `modify_user` varchar(255) DEFAULT NULL,
  `modify_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT 'api线路和对应的前缀';


CREATE TABLE `dsf_gm_category` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `category_name` varchar(40) NOT NULL COMMENT '游戏类别',
  `sort_id` int(11) NOT NULL COMMENT '排序号',
  `available` tinyint(4) NOT NULL COMMENT '是否可用',
  `memo` varchar(200) DEFAULT NULL COMMENT '备注',
  `create_user` varchar(16) NOT NULL COMMENT '',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modify_user` varchar(16) DEFAULT NULL,
  `modify_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `parent_id` int(11) NOT NULL COMMENT '父节点',
  `tree_id` varchar(40) DEFAULT NULL COMMENT '游戏树id，方便前段计算',
  PRIMARY KEY (`id`),
  UNIQUE KEY `index_t_gm_depotcat_name` (`category_name`) USING BTREE,
  UNIQUE KEY `index_t_gm_depot_tree_id` (`tree_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT '游戏分类';


CREATE TABLE `dsf_gm_platform` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `platform_name` varchar(45) NOT NULL COMMENT '第三方游戏平台名',
  `platform_code` varchar(12) DEFAULT NULL COMMENT '第三方游戏平台Code',
  `available` tinyint(4) NOT NULL COMMENT '是否可用',
  `memo` varchar(200) DEFAULT NULL COMMENT '备注',
  `create_user` varchar(16) DEFAULT NULL,
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `modify_user` varchar(16) DEFAULT NULL,
  `modify_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `start_date` datetime DEFAULT NULL COMMENT '开始时间',
  `end_date` datetime DEFAULT NULL COMMENT '结束时间',
  `sort_id` int(11) DEFAULT NULL COMMENT '排序号',
  PRIMARY KEY (`id`),
  UNIQUE KEY `index_t_gm_depot_platform_name` (`platform_name`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT '第三方游戏';


CREATE TABLE `dsf_gm_platform_category` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `platform_code` varchar(12) DEFAULT NULL COMMENT '游戏平台Code',
  `category_id` int(11) DEFAULT NULL COMMENT '游戏分类id',
  `sort` int(11) DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT '游戏平台和包含的游戏类型';


CREATE TABLE `dsf_gm_game` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `category_id` int(11) NOT NULL COMMENT '对应游戏分类ID',
  `platform_code` varchar(12) NOT NULL COMMENT '游戏平台Code',
  `platform_name` varchar(45) DEFAULT NULL COMMENT '游戏平台名',
  `game_code` varchar(100) DEFAULT NULL COMMENT '游戏code',
  `h5_game_code` varchar(100) DEFAULT NULL COMMENT '游戏code_h5',
  `adriod_game_code` varchar(100) DEFAULT NULL COMMENT '游戏code_andriod',
  `pc_game_code` varchar(100) DEFAULT NULL COMMENT '游戏code_pc',
  `game_name` varchar(40) NOT NULL COMMENT '游戏中文名',
  `game_tag` varchar(100) DEFAULT NULL COMMENT '游戏介绍',
  `game_param` varchar(200) DEFAULT NULL COMMENT '游戏附加码',
  `available` tinyint(4) DEFAULT '0',
  `pc_logo` varchar(100) DEFAULT NULL COMMENT 'pc版的logo',
  `phone_logo` varchar(100) DEFAULT NULL COMMENT '手机版logo',
  `enable_pc` tinyint(4) DEFAULT '1',
  `enable_Mobile` tinyint(4) DEFAULT '1',
  `enable_test` tinyint(4) DEFAULT '0',
  `enable_hot` tinyint(4) DEFAULT NULL COMMENT '是否热门',
  `ebable_new` tinyint(4) DEFAULT NULL COMMENT '是否新游戏',
  `memo` varchar(200) DEFAULT NULL COMMENT '备注',
  `create_user` varchar(16) DEFAULT NULL,
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `modify_user` varchar(16) DEFAULT NULL ,
  `modify_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `game_name_en` varchar(40) DEFAULT NULL COMMENT '游戏英文名',
  `game_id` varchar(20) DEFAULT NULL COMMENT 'game_id可不填',
  `lastday_per` int(11) DEFAULT '0' COMMENT '昨天该游戏的玩家比例',
  `sort_id` int(11) NOT NULL DEFAULT '0' COMMENT '排序号',
  `click_num` int(11) NOT NULL DEFAULT '0' COMMENT '点击数',
  `good_num` int(11) NOT NULL DEFAULT '0' COMMENT '好评数',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT '游戏列表';


CREATE TABLE `dsf_schema` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `schema_name` varchar(25) NOT NULL COMMENT '站点名',
  `is_used` tinyint(4) NOT NULL DEFAULT '0',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modify_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `site_code` varchar(12) DEFAULT NULL COMMENT '对应的code 不参与业务',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT '站点表';


CREATE TABLE `dsf_transfer_log` (
  `id` bigint(60) NOT NULL AUTO_INCREMENT,
  `transaction_id` varchar(64) NOT NULL COMMENT '转账号（唯一）',
  `member_user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `site_code` varchar(12) NOT NULL COMMENT '站点名',
  `dsf_player_id` varchar(64) NOT NULL COMMENT '第三方游戏平台id',
  `platform_code` varchar(12) NOT NULL COMMENT '游戏平台',
  `state` varchar(100) NOT NULL COMMENT '转账状态',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '转账开始时间',
  `complete_time` datetime DEFAULT NULL COMMENT '转账结束时间',
  `version` int(20) NOT NULL DEFAULT '1' COMMENT '版本号',
  `type` varchar(20) NOT NULL COMMENT '转账类型 转入或 转出',
  `amount` double(10,2) NOT NULL COMMENT '转账金额',
  `balance_changed_id` bigint(20) DEFAULT NULL COMMENT '主账号账变id',
  `operator` varchar(40) DEFAULT NULL COMMENT '操作者member',
  `fail_reason` varchar(64) DEFAULT NULL COMMENT '转账失败原因',
  `dsf_transaction_id` varchar(128) NOT NULL COMMENT '第三方转账id',
  `api_name` varchar(40) NOT NULL COMMENT '转账提交的线路名',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `combo1` (`create_time`,`type`,`dsf_player_id`) USING BTREE,
  KEY `combo2` (`create_time`,`platform_code`,`dsf_player_id`,`type`) USING BTREE,
  KEY `combo5` (`create_time`,`site_code`),
  KEY `combo4` (`create_time`),
  KEY `combo3` (`api_name`),
  KEY `combo6` (`platform_code`,`dsf_player_id`,`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='第三方转账表';;



CREATE TABLE `dsf_statements_player_day_2019` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `account_id` bigint(20) NOT NULL,
  `site_code` varchar(20) NOT NULL COMMENT '站点',
  `platform_code` varchar(12) NOT NULL COMMENT '游戏平台',
  `day` datetime NOT NULL COMMENT '日期',
  `total_bet` decimal(10,2) NOT NULL COMMENT '总有效投注',
  `bet_count` bigint(11) NOT NULL COMMENT '注单量',
  `total_win` decimal(10,2) NOT NULL COMMENT '总赢得',
  `total_notsettled` decimal(10,2) unsigned zerofill DEFAULT '00000000.00' COMMENT '未结算(只有日报表有数据)',
  `dsf_player_id` varchar(20) NOT NULL COMMENT '第三方玩家Id',
  `nickname` varchar(64) NOT NULL COMMENT '玩家名称',
  `category_name` varchar(40) NOT NULL COMMENT '游戏类型',
  `result` decimal(10,2) NOT NULL COMMENT '总输赢',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `rebate_amount` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '返水',
  `total_member_exposure` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '玩家实际投注',
  PRIMARY KEY (`id`),
  UNIQUE KEY `player_day_uion` (`platform_code`,`day`,`dsf_player_id`,`category_name`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT '第三方玩家每日汇总表';


CREATE TABLE `dsf_statements_month` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `site_code` varchar(20) NOT NULL COMMENT '站点',
  `platform_code` varchar(12) NOT NULL COMMENT '游戏平台',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '开始时间',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '结束时间',
  `total_bet` decimal(10,2) NOT NULL COMMENT '总有效投注',
  `bet_count` bigint(11) NOT NULL COMMENT '注单量',
  `total_win` decimal(10,2) NOT NULL COMMENT '总赢得',
  `player_result` decimal(10,2) NOT NULL COMMENT '输赢后结果',
  `mon` varchar(10) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `platform_frand` (`site_code`,`platform_code`,`mon`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT '第三方按月汇总';


CREATE TABLE `dsf_member` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `site_code` varchar(20) NOT NULL COMMENT '站点',
  `platform_code` varchar(12) NOT NULL COMMENT '游戏平台',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '开始时间',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '结束时间',
  `dsf_player_id` varchar(20) NOT NULL COMMENT '第三方玩家Id',
  `member_user` varchar(40) NOT NULL COMMENT '平台玩家',
  `available` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否可用',
  `api_name` varchar(40) NOT NULL COMMENT '转账提交的线路名',
  `password` varchar(40) DEFAULT NULL COMMENT '用户第三方密码',
  PRIMARY KEY (`id`),
  UNIQUE KEY `platform_frand` (`site_code`,`platform_code`,`member_user`,`dsf_player_id`),
  KEY `unique_platform_code_dsf_player_id` (`platform_code`,`dsf_player_id`)
) ENGINE=InnoDB AUTO_INCREMENT=29 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='第三方玩家表';


CREATE TABLE `dsf_site_schema` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `site_id` int(11) NOT NULL,
  `schema_id` int(11) NOT NULL,
  `create_user` varchar(16) DEFAULT NULL,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modify_user` varchar(16) DEFAULT NULL,
  `modify_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `is_enabled` tinyint(4) NOT NULL DEFAULT '1',
 PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

CREATE TABLE `dsf_betlog_game_video_201903` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `bet_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '注单id,自己通过程序生成',
  `dsf_platform_bet_id` varchar(40) NOT NULL COMMENT '第三方游戏平台注单ID,使用replace into,和game_platform做唯一键.',
  `category_name` varchar(10) NOT NULL COMMENT '注单类型',
  `game_platform` varchar(5) NOT NULL COMMENT '游戏平台',
  `member_user` varchar(40) NOT NULL COMMENT '平台账号',
  `dsf_player_id` varchar(40) NOT NULL COMMENT '第三方账号Id',
  `game_name_cn` varchar(40) NOT NULL COMMENT '游戏名称',
  `table_name` varchar(40) NOT NULL COMMENT '桌号',
  `issue_no` varchar(40) DEFAULT NULL COMMENT '期号',
  `stake_amount` decimal(11,2) NOT NULL COMMENT '投注金额',
  `valid_stake` decimal(11,2) NOT NULL COMMENT '有效投注',
  `win_loss` decimal(11,2) NOT NULL COMMENT '输赢',
  `jackpot` decimal(11,2) NOT NULL COMMENT '奖池金额',
  `create_time` datetime(4) NOT NULL ON UPDATE CURRENT_TIMESTAMP(4) COMMENT '数据下载时间,精确到毫秒后一位',
  `game_time` datetime(3) NOT NULL COMMENT '游戏时间',
  `game_result` varchar(255) DEFAULT NULL COMMENT '游戏结果',
  `remark` varchar(255) DEFAULT NULL COMMENT '其他内容,保存json字符串',
  `result_picture_url` varchar(255) DEFAULT NULL COMMENT '结果图片',
  `site_code` varchar(12) DEFAULT NULL COMMENT '站点前缀',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `undex_bet_id_game_platform` (`dsf_platform_bet_id`,`game_platform`) USING BTREE,
  KEY `index_category_name` (`category_name`) USING BTREE,
  KEY `index_game_platform` (`game_platform`) USING BTREE,
  KEY `index_member_user` (`member_user`) USING BTREE,
  KEY `index_dsf_player_id` (`dsf_player_id`) USING BTREE,
  KEY `index_create_time` (`create_time`) USING BTREE,
  KEY `index_game_time` (`game_time`) USING BTREE,
  KEY `index_game_name` (`game_name_cn`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=40 DEFAULT CHARSET=utf8 COMMENT='第三方真人注单表';

CREATE TABLE  `dsf_betlog_game_video_item_201903` IF NOT EXISTS (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键自增id',
  `dsf_platform_bet_id` varchar(40) NOT NULL COMMENT '第三方游戏平台父注单id',
  `bet_type` varchar(20) NOT NULL COMMENT '玩家投注类型',
  `bet_amount` decimal(13,2) NOT NULL COMMENT '玩家投注额度',
  `bet_win_loss` decimal(13,2) NOT NULL COMMENT '玩家投注输赢',
  `bet_time` datetime NOT NULL COMMENT '玩家下注时间',
  `create_time` datetime NOT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `game_platform` varchar(5) NOT NULL COMMENT '平台类型',
  PRIMARY KEY (`id`),
  UNIQUE KEY `unix_game_platform_dsf_platform_bet_id` (`dsf_platform_bet_id`,`game_platform`) USING BTREE,
  KEY `index_dsf_platform_bet_id` (`dsf_platform_bet_id`) USING BTREE,
  KEY `index_bet_time` (`bet_time`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8 COMMENT='第三方真人注单子订单';



INSERT INTO `dsf_center_master`.`dsf_schema`(`id`, `schema_name`, `is_used`, `create_time`, `modify_time`, `site_code`) VALUES (1, 'melody_db', 1, '2019-02-15 17:24:35', '2019-02-15 17:24:35', 'test');

INSERT INTO `dsf_center_master`.`dsf_site`(`id`, `site_code`, `site_name`, `schema_name`, `is_api`, `currency`, `company_id`, `start_date`, `end_date`, `available`, `use_time`, `memo`, `create_user`, `create_time`, `modify_user`, `modify_time`, `company_user`) VALUES (1, 'test', 'test', 'melody_db', 1, 'RMB', NULL, '2019-02-15 17:23:36', '2019-02-15 17:23:36', 1, '2019-02-15 17:23:36', NULL, 'rmi', '2019-02-15 17:23:36', 'rmi', '2019-02-23 20:54:01', NULL);







