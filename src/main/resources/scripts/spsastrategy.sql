

DROP SCHEMA IF EXISTS spsastrategy;
CREATE SCHEMA spsastrategy;

USE spsastrategy;

DROP TABLE IF EXISTS authoritygoals;
CREATE TABLE `authoritygoals` (
  `id` VARCHAR(500) NOT NULL,
  `goal` TEXT NOT NULL,
  `yearlyweight` INT,
  `yearlyexpectedweight` INT,
  `username` VARCHAR(250) NOT NULL,
  `deadline` timestamp NULL,
  `status` VARCHAR(200) NOT NULL,
  `date_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

DROP TABLE IF EXISTS departmentgoals;
CREATE TABLE `departmentgoals` (
  `id` VARCHAR(500) NOT NULL,
  `authgoalid` VARCHAR(500) NOT NULL,
  `goal` TEXT NOT NULL,
  `yearlyweight` INT,
  `yearlyexpectedweight` INT,
  `username` VARCHAR(250) NOT NULL,
  `deadline` timestamp NULL,
  `status` VARCHAR(200) NOT NULL,
  `reason` LONGTEXT NOT NULL,
  `solution` LONGTEXT NOT NULL,
  `date_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

DROP TABLE IF EXISTS sectiongoals;
CREATE TABLE `sectiongoals` (
  `id` VARCHAR(500) NOT NULL,
  `depgoalid` VARCHAR(500) NOT NULL,
  `goal` TEXT NOT NULL,
  `yearlyweight` INT,
  `yearlyexpectedweight` INT,
  `username` VARCHAR(250) NOT NULL,
  `deadline` timestamp NULL,
  `status` VARCHAR(200) NOT NULL,
  `reason` LONGTEXT NOT NULL,
  `solution` LONGTEXT NOT NULL,
  `date_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


DROP TABLE IF EXISTS goalsuserlevel;
CREATE TABLE `goalsuserlevel` (
  `goalid` VARCHAR(500),
  `userlevelid` VARCHAR(500) NOT NULL,
  PRIMARY KEY (`goalid`, `userlevelid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


DROP TABLE IF EXISTS userlevel;
CREATE TABLE `userlevel` (
  `id` VARCHAR(500) NOT NULL,
  `name` VARCHAR(250) NOT NULL COMMENT '(IT - Security - ...)',
  `level` VARCHAR(500) NOT NULL COMMENT '(AUTHORITY - DEPARTMENT - SECTION)',
  `role` VARCHAR(500) NOT NULL COMMENT '(MANAGER, EMPLOYEE)',
  `date_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


DROP TABLE IF EXISTS quarters;
CREATE TABLE `quarters` (
  `id` VARCHAR(500) NOT NULL,
  `name` VARCHAR(500),
  `year` VARCHAR(500),
  `quarternumber` INT,
  `goalid` VARCHAR(500) NOT NULL,
  `expectedweight` INT,
  `weight` INT,
  `status` VARCHAR(200) NOT NULL,
  `deadline` timestamp NULL,
  `date_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

DROP TABLE IF EXISTS evidence;
CREATE TABLE `evidence` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `username` VARCHAR(250) NOT NULL,
  `comment` longtext,
  `quarterid` VARCHAR(500) NOT NULL,
  `date_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

DROP TABLE IF EXISTS evidence_reply;
CREATE TABLE `evidence_reply` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `evidenceid` bigint NOT NULL,
  `username` VARCHAR(250) NOT NULL,
  `comment` longtext,
  `date_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

DROP TABLE IF EXISTS evidence_files;
CREATE TABLE `evidence_files` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `evidenceid` bigint DEFAULT NULL,
  `filepath` longtext COMMENT 'Employees upload files\n',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


CREATE TABLE `settings` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `adminkey` varchar(255) DEFAULT NULL,
  `isdefault` bit(1) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
INSERT INTO `settings` (`id`, `adminkey`, `isdefault`) VALUES ('1', 'wsTk5JDXd74XLGUrJ1', b'1');


ALTER TABLE `evidence` 
ADD COLUMN `goalid` BIGINT NULL AFTER `date_time`;
ALTER TABLE `evidence` 
ADD COLUMN `depusername` BIGINT NULL AFTER `goalid`;



-- AUTH 


INSERT INTO `authorization` (`user_role`, `api`) VALUES ('Admin', '/strategy/api/admin/authority/goal/list');
INSERT INTO `authorization` (`user_role`, `api`) VALUES ('GRCAdmin', '/strategy/api/admin/authority/goal/list');

INSERT INTO `authorization` (`user_role`, `api`) VALUES ('Admin', '/strategy/api/admin/authority/goal/save');
INSERT INTO `authorization` (`user_role`, `api`) VALUES ('GRCAdmin', '/strategy/api/admin/authority/goal/save');

INSERT INTO `menu_authorization` (`menu_auth_id`, `api`, `isget`) VALUES ('manageauthoritygoals', '/strategy/api/admin/authority/goal/list', b'1');
INSERT INTO `menu_authorization` (`menu_auth_id`, `api`, `ispost`, `isupdate`) VALUES ('manageauthoritygoals', '/strategy/api/admin/authority/goal/save', b'1', b'1');

INSERT INTO `menu` (`id`, `lang`, `name`, `href`, `icon`, `order`, `auth_id`, `parent_id`) VALUES ('manageauthoritygoalsen', 'en', 'Authority Goals', 'manageauthoritygoals', 'fa fa-folder-o', 400, 'manageauthoritygoals', 'manageen');
INSERT INTO `menu` (`id`, `lang`, `name`, `href`, `icon`, `order`, `auth_id`, `parent_id`) VALUES ('manageauthoritygoalsar', 'ar', 'الاهداف الاستراتيجية', 'manageauthoritygoals', 'fa fa-folder-o', 400, 'manageauthoritygoals', 'managear');


INSERT INTO `menu_role` (`menu_id`, `user_role`) VALUES ('manageauthoritygoalsen', 'Admin');
INSERT INTO `menu_role` (`menu_id`, `user_role`) VALUES ('manageauthoritygoalsen', 'GRCAdmin');
INSERT INTO `menu_role` (`menu_id`, `user_role`) VALUES ('manageauthoritygoalsar', 'Admin');
INSERT INTO `menu_role` (`menu_id`, `user_role`) VALUES ('manageauthoritygoalsar', 'GRCAdmin');


