

DROP SCHEMA IF EXISTS spsastrategy;
CREATE SCHEMA spsastrategy;

USE spsastrategy;

DROP TABLE IF EXISTS authoritygoals;
CREATE TABLE `authoritygoals` (
  `id` VARCHAR(500) NOT NULL,
  `goal` TEXT NOT NULL,
  `goalar` TEXT NOT NULL,
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
  `goalar` TEXT NOT NULL,
  `yearlyweight` INT,
  `yearlyexpectedweight` INT,
  `username` VARCHAR(250) NOT NULL,
  `deadline` timestamp NULL,
  `status` VARCHAR(200) NULL,
  `reason` LONGTEXT NULL,
  `solution` LONGTEXT NULL,
  `date_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

DROP TABLE IF EXISTS sectiongoals;
CREATE TABLE `sectiongoals` (
  `id` VARCHAR(500) NOT NULL,
  `depgoalid` VARCHAR(500) NOT NULL,
  `goal` TEXT NOT NULL,
  `goalar` TEXT NOT NULL,
  `yearlyweight` INT,
  `yearlyexpectedweight` INT,
  `username` VARCHAR(250) NOT NULL,
  `deadline` timestamp NULL,
  `status` VARCHAR(200) NULL,
  `reason` LONGTEXT NULL,
  `solution` LONGTEXT NULL,
  `date_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


DROP TABLE IF EXISTS restrictedgoalsuserlevel;
CREATE TABLE `restrictedgoalsuserlevel` (
  `goalid` VARCHAR(500),
  `userrole` VARCHAR(50) NOT NULL,
  PRIMARY KEY (`goalid`)
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


DROP TABLE IF EXISTS evidence;
CREATE TABLE `evidence` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `username` VARCHAR(250) NOT NULL,
  `comment` longtext,
  `goalid` VARCHAR(500) NOT NULL,
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
ADD COLUMN `depusername` BIGINT NULL AFTER `goalid`;

INSERT INTO `userlevel` (`id`, `name`, `level`, `role`) VALUES ('sectionmanager', 'sectionmanager', 'SECTION', 'MANAGER');
INSERT INTO `userlevel` (`id`, `name`, `level`, `role`) VALUES ('sectionemployee', 'sectionemployee', 'SECTION', 'EMPLOYEE');
INSERT INTO `userlevel` (`id`, `name`, `level`, `role`) VALUES ('departmentmanager', 'departmentmanager', 'DEPARTMENT', 'MANAGER');
INSERT INTO `userlevel` (`id`, `name`, `level`, `role`) VALUES ('departmentemployee', 'departmentemployee', 'DEPARTMENT', 'EMPLOYEE');
INSERT INTO `userlevel` (`id`, `name`, `level`, `role`) VALUES ('authoritymanager', 'authoritymanager', 'AUTHORITY', 'MANAGER');

ALTER TABLE `authoritygoals` 
ADD COLUMN `quarter` VARCHAR(250) NULL AFTER `goalar`,
ADD COLUMN `fromdate` TIMESTAMP NULL AFTER `quarter`,
ADD COLUMN `todate` TIMESTAMP NULL AFTER `fromdate`,
ADD COLUMN `year` VARCHAR(20) NULL AFTER `todate`;
ALTER TABLE `evidence_files` 
ADD COLUMN `filetype` VARCHAR(200) NULL;
ALTER TABLE `evidence_files` 
ADD COLUMN `fileimage` TEXT NULL AFTER `filetype`;

ALTER TABLE `evidence_files` 
ADD COLUMN `width` VARCHAR(200) NULL DEFAULT '120px';

ALTER TABLE `evidence_files` 
ADD COLUMN `date_time` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP;

ALTER TABLE `evidence` 
ADD COLUMN `status` VARCHAR(200) NULL AFTER `goalid`;

ALTER TABLE `evidence_files` 
ADD COLUMN `filename` VARCHAR(400) NULL AFTER `date_time`,
ADD COLUMN `username` VARCHAR(250) NULL AFTER `filename`,
ADD COLUMN `filesize` BIGINT NULL AFTER `username`;

CREATE TABLE `goalstatus` (
  `nameen` NVARCHAR(200) NOT NULL,
  `namear` NVARCHAR(200) NULL,
  `description` LONGTEXT NULL,
  PRIMARY KEY (`nameen`));

INSERT INTO `goalstatus` (`nameen`, `namear`) VALUES ('New', 'New');
INSERT INTO `goalstatus` (`nameen`, `namear`) VALUES ('In Progress', 'In Progress');
INSERT INTO `goalstatus` (`nameen`, `namear`) VALUES ('Partially Done', 'Partially Done');
INSERT INTO `goalstatus` (`nameen`, `namear`) VALUES ('Not Done', 'Not Done');
INSERT INTO `goalstatus` (`nameen`, `namear`) VALUES ('Done', 'Done');

ALTER TABLE `authoritygoals` 
ADD COLUMN `userrole` VARCHAR(50) NOT NULL ;

ALTER TABLE `departmentgoals` 
ADD COLUMN `userrole` VARCHAR(50) NOT NULL ;

ALTER TABLE `sectiongoals` 
ADD COLUMN `userrole` VARCHAR(50) NOT NULL ;


-- AUTH 


INSERT INTO `authorization` (`user_role`, `api`) VALUES ('Admin', '/strategy/api/admin/authority/goal/list');
INSERT INTO `authorization` (`user_role`, `api`) VALUES ('GRCAdmin', '/strategy/api/admin/authority/goal/list');

INSERT INTO `authorization` (`user_role`, `api`) VALUES ('Admin', '/strategy/api/admin/authority/goal/save');
INSERT INTO `authorization` (`user_role`, `api`) VALUES ('GRCAdmin', '/strategy/api/admin/authority/goal/save');

INSERT INTO `authorization` (`user_role`, `api`) VALUES ('Admin', '/strategy/api/admin/authority/goal/remove');
INSERT INTO `authorization` (`user_role`, `api`) VALUES ('GRCAdmin', '/strategy/api/admin/authority/goal/remove');

INSERT INTO `authorization` (`user_role`, `api`) VALUES ('Admin', '/strategy/api/admin/authority/goal/details');
INSERT INTO `authorization` (`user_role`, `api`) VALUES ('GRCAdmin', '/strategy/api/admin/authority/goal/details');

INSERT INTO `menu_authorization` (`menu_auth_id`, `api`, `isget`) VALUES ('manageauthoritygoals', '/strategy/api/admin/authority/goal/list', b'1');
INSERT INTO `menu_authorization` (`menu_auth_id`, `api`, `ispost`, `isupdate`) VALUES ('manageauthoritygoals', '/strategy/api/admin/authority/goal/save', b'1', b'1');
INSERT INTO `menu_authorization` (`menu_auth_id`, `api`, `isdelete`) VALUES ('manageauthoritygoals', '/strategy/api/admin/authority/goal/remove', b'1');
INSERT INTO `menu_authorization` (`menu_auth_id`, `api`, `isget`) VALUES ('managedepartmentgoals', '/strategy/api/admin/authority/goal/details', b'1');


INSERT INTO `authorization` (`user_role`, `api`) VALUES ('Admin', '/strategy/api/admin/department/goal/list');
INSERT INTO `authorization` (`user_role`, `api`) VALUES ('GRCAdmin', '/strategy/api/admin/department/goal/list');

INSERT INTO `authorization` (`user_role`, `api`) VALUES ('Admin', '/strategy/api/admin/department/goal/save');
INSERT INTO `authorization` (`user_role`, `api`) VALUES ('GRCAdmin', '/strategy/api/admin/department/goal/save');

INSERT INTO `authorization` (`user_role`, `api`) VALUES ('Admin', '/strategy/api/admin/department/goal/remove');
INSERT INTO `authorization` (`user_role`, `api`) VALUES ('GRCAdmin', '/strategy/api/admin/department/goal/remove');

INSERT INTO `authorization` (`user_role`, `api`) VALUES ('Admin', '/strategy/api/admin/department/goal/details');
INSERT INTO `authorization` (`user_role`, `api`) VALUES ('GRCAdmin', '/strategy/api/admin/department/goal/details');

INSERT INTO `menu_authorization` (`menu_auth_id`, `api`, `isget`) VALUES ('managedepartmentgoals', '/strategy/api/admin/department/goal/list', b'1');
INSERT INTO `menu_authorization` (`menu_auth_id`, `api`, `ispost`, `isupdate`) VALUES ('managedepartmentgoals', '/strategy/api/admin/department/goal/save', b'1', b'1');
INSERT INTO `menu_authorization` (`menu_auth_id`, `api`, `isdelete`) VALUES ('managedepartmentgoals', '/strategy/api/admin/department/goal/remove', b'1');
INSERT INTO `menu_authorization` (`menu_auth_id`, `api`, `isget`) VALUES ('managesectiongoals', '/strategy/api/admin/department/goal/details', b'1');


INSERT INTO `authorization` (`user_role`, `api`) VALUES ('Admin', '/strategy/api/admin/section/goal/list');
INSERT INTO `authorization` (`user_role`, `api`) VALUES ('GRCAdmin', '/strategy/api/admin/section/goal/list');

INSERT INTO `authorization` (`user_role`, `api`) VALUES ('Admin', '/strategy/api/admin/section/goal/save');
INSERT INTO `authorization` (`user_role`, `api`) VALUES ('GRCAdmin', '/strategy/api/admin/section/goal/save');

INSERT INTO `authorization` (`user_role`, `api`) VALUES ('Admin', '/strategy/api/admin/section/goal/remove');
INSERT INTO `authorization` (`user_role`, `api`) VALUES ('GRCAdmin', '/strategy/api/admin/section/goal/remove');

INSERT INTO `authorization` (`user_role`, `api`) VALUES ('Admin', '/strategy/api/admin/section/goal/details');
INSERT INTO `authorization` (`user_role`, `api`) VALUES ('GRCAdmin', '/strategy/api/admin/section/goal/details');


INSERT INTO `menu_authorization` (`menu_auth_id`, `api`, `isget`) VALUES ('managesectiongoals', '/strategy/api/goal/evidence/list', b'1');
INSERT INTO `authorization` (`user_role`, `api`) VALUES ('Admin', '/strategy/api/admin/goal/evidence/list');
INSERT INTO `authorization` (`user_role`, `api`) VALUES ('GRCAdmin', '/strategy/api/admin/goal/evidence/list');
INSERT INTO `menu_authorization` (`menu_auth_id`, `api`, `isget`) VALUES ('managedepartmentgoals', '/strategy/api/goal/evidence/list', b'1');


INSERT INTO `menu_authorization` (`menu_auth_id`, `api`, `isget`) VALUES ('managesectiongoals', '/strategy/api/admin/section/goal/list', b'1');
INSERT INTO `menu_authorization` (`menu_auth_id`, `api`, `ispost`, `isupdate`) VALUES ('managesectiongoals', '/strategy/api/admin/section/goal/save', b'1', b'1');
INSERT INTO `menu_authorization` (`menu_auth_id`, `api`, `isdelete`) VALUES ('managesectiongoals', '/strategy/api/admin/section/goal/remove', b'1');
INSERT INTO `menu_authorization` (`menu_auth_id`, `api`, `isget`) VALUES ('managesectiongoals', '/strategy/api/admin/section/goal/details', b'1');


INSERT INTO `menu` (`id`, `lang`, `name`, `href`, `icon`, `order`, `auth_id`, `parent_id`) VALUES ('manageauthoritygoalsen', 'en', 'Authority Goals', 'manageauthoritygoals', 'fa fa-folder-o', 400, 'manageauthoritygoals', 'manageen');
INSERT INTO `menu` (`id`, `lang`, `name`, `href`, `icon`, `order`, `auth_id`, `parent_id`) VALUES ('manageauthoritygoalsar', 'ar', 'الاهداف الاستراتيجية', 'manageauthoritygoals', 'fa fa-folder-o', 400, 'manageauthoritygoals', 'managear');


INSERT INTO `menu_role` (`menu_id`, `user_role`) VALUES ('manageauthoritygoalsen', 'Admin');
INSERT INTO `menu_role` (`menu_id`, `user_role`) VALUES ('manageauthoritygoalsen', 'GRCAdmin');
INSERT INTO `menu_role` (`menu_id`, `user_role`) VALUES ('manageauthoritygoalsar', 'Admin');
INSERT INTO `menu_role` (`menu_id`, `user_role`) VALUES ('manageauthoritygoalsar', 'GRCAdmin');



INSERT INTO `menu` (`id`, `lang`, `name`, `href`, `icon`, `order`, `auth_id`, `parent_id`, `show`) VALUES ('managedepartmentgoalsen', 'en', 'Department Goals', 'managedepartmentgoals', 'fa fa-folder-o', 400, 'managedepartmentgoals', 'manageauthoritygoalsen', b'1');
INSERT INTO `menu` (`id`, `lang`, `name`, `href`, `icon`, `order`, `auth_id`, `parent_id`, `show`) VALUES ('managedepartmentgoalsar', 'ar', 'أهداف القسم', 'managedepartmentgoals', 'fa fa-folder-o', 400, 'managedepartmentgoals', 'manageauthoritygoalsar', b'1');


INSERT INTO `menu_role` (`menu_id`, `user_role`) VALUES ('managedepartmentgoalsen', 'Admin');
INSERT INTO `menu_role` (`menu_id`, `user_role`) VALUES ('managedepartmentgoalsen', 'GRCAdmin');
INSERT INTO `menu_role` (`menu_id`, `user_role`) VALUES ('managedepartmentgoalsar', 'Admin');
INSERT INTO `menu_role` (`menu_id`, `user_role`) VALUES ('managedepartmentgoalsar', 'GRCAdmin');



INSERT INTO `menu` (`id`, `lang`, `name`, `href`, `icon`, `order`, `auth_id`, `parent_id`, `show`) VALUES ('managesectiongoalsen', 'en', 'Section Goals', 'managesectiongoals', 'fa fa-folder-o', 400, 'managesectiongoals', 'managedepartmentgoalsen', b'1');
INSERT INTO `menu` (`id`, `lang`, `name`, `href`, `icon`, `order`, `auth_id`, `parent_id`, `show`) VALUES ('managesectiongoalsar', 'ar', 'أهداف القسم', 'managesectiongoals', 'fa fa-folder-o', 400, 'managesectiongoals', 'managedepartmentgoalsar', b'1');


INSERT INTO `menu_role` (`menu_id`, `user_role`) VALUES ('managesectiongoalsen', 'Admin');
INSERT INTO `menu_role` (`menu_id`, `user_role`) VALUES ('managesectiongoalsen', 'GRCAdmin');
INSERT INTO `menu_role` (`menu_id`, `user_role`) VALUES ('managesectiongoalsar', 'Admin');
INSERT INTO `menu_role` (`menu_id`, `user_role`) VALUES ('managesectiongoalsar', 'GRCAdmin');


UPDATE `menu` SET `isget` = b'1', `ispost` = b'1', `isupdate` = b'1', `isdelete` = b'1', `isconfiguration` = b'1' WHERE (`id` = 'managedepartmentgoalsar');
UPDATE `menu` SET `isget` = b'1', `ispost` = b'1', `isupdate` = b'1', `isdelete` = b'1', `isconfiguration` = b'1' WHERE (`id` = 'managedepartmentgoalsen');
UPDATE `menu` SET `isget` = b'1', `ispost` = b'1', `isupdate` = b'1', `isdelete` = b'1', `isconfiguration` = b'1' WHERE (`id` = 'managesectiongoalsar');
UPDATE `menu` SET `isget` = b'1', `ispost` = b'1', `isupdate` = b'1', `isdelete` = b'1', `isconfiguration` = b'1' WHERE (`id` = 'managesectiongoalsen');
UPDATE `menu` SET `isget` = b'1', `ispost` = b'1', `isupdate` = b'1', `isdelete` = b'1', `isconfiguration` = b'1' WHERE (`id` = 'manageauthoritygoalsar');
UPDATE `menu` SET `isget` = b'1', `ispost` = b'1', `isupdate` = b'1', `isdelete` = b'1', `isconfiguration` = b'1' WHERE (`id` = 'manageauthoritygoalsen');




UPDATE `menu` SET `additionalconfig` = 'Current User Access' WHERE (`id` = 'managedepartmentgoalsar');
UPDATE `menu` SET `additionalconfig` = 'Current User Access' WHERE (`id` = 'manageauthoritygoalsen');
UPDATE `menu` SET `additionalconfig` = 'Current User Access' WHERE (`id` = 'manageauthoritygoalsar');
UPDATE `menu` SET `additionalconfig` = 'Current User Access' WHERE (`id` = 'managedepartmentgoalsen');
UPDATE `menu` SET `additionalconfig` = 'Current User Access' WHERE (`id` = 'managesectiongoalsar');
UPDATE `menu` SET `additionalconfig` = 'Current User Access' WHERE (`id` = 'managesectiongoalsen');


UPDATE `menu` SET `showdropdownlist` = b'0' WHERE (`id` = 'manageauthoritygoalsar');
UPDATE `menu` SET `showdropdownlist` = b'0' WHERE (`id` = 'manageauthoritygoalsen');
UPDATE `menu` SET `showdropdownlist` = b'0' WHERE (`id` = 'managedepartmentgoalsar');
UPDATE `menu` SET `showdropdownlist` = b'0' WHERE (`id` = 'managedepartmentgoalsen');
UPDATE `menu` SET `showdropdownlist` = b'0' WHERE (`id` = 'managesectiongoalsen');
UPDATE `menu` SET `showdropdownlist` = b'0' WHERE (`id` = 'managesectiongoalsar');

UPDATE `menu` SET `parent_id` = 'managear' WHERE (`id` = 'managedepartmentgoalsar');
UPDATE `menu` SET `parent_id` = 'manageen' WHERE (`id` = 'managedepartmentgoalsen');
UPDATE `menu` SET `parent_id` = 'manageen' WHERE (`id` = 'managesectiongoalsen');
UPDATE `menu` SET `parent_id` = 'managear' WHERE (`id` = 'managesectiongoalsar');


INSERT INTO `menu_authorization` (`menu_auth_id`, `api`, `isget`) VALUES ('manageauthoritygoals', '/auth/api/admin/role/goals/access/list', b'1');


INSERT INTO `authorization` (`user_role`, `api`) VALUES ('Admin', '/strategy/api/admin/role/goal/access/list');
INSERT INTO `authorization` (`user_role`, `api`) VALUES ('GRCAdmin', '/strategy/api/admin/role/goal/access/list');

INSERT INTO `menu_authorization` (`menu_auth_id`, `api`, `isget`) VALUES ('manageauthoritygoals', '/strategy/api/admin/role/goal/access/list', b'1');




INSERT INTO `authorization` (`user_role`, `api`) VALUES ('Admin', '/strategy/api/admin/goal/evidence/comment/list');
INSERT INTO `authorization` (`user_role`, `api`) VALUES ('GRCAdmin', '/strategy/api/admin/goal/evidence/comment/list');

INSERT INTO `menu_authorization` (`menu_auth_id`, `api`, `isget`) VALUES ('managedepartmentgoals', '/strategy/api/admin/goal/evidence/comment/list', b'1');
INSERT INTO `menu_authorization` (`menu_auth_id`, `api`, `isget`) VALUES ('managesectiongoals', '/strategy/api/admin/goal/evidence/comment/list', b'1');


INSERT INTO `authorization` (`user_role`, `api`) VALUES ('Admin', '/strategy/api/admin/goal/evidence/comment/save');
INSERT INTO `authorization` (`user_role`, `api`) VALUES ('GRCAdmin', '/strategy/api/admin/goal/evidence/comment/save');

INSERT INTO `menu_authorization` (`menu_auth_id`, `api`, `ispost`, `isupdate`) VALUES ('managedepartmentgoals', '/strategy/api/admin/goal/evidence/comment/save', b'1', b'1');
INSERT INTO `menu_authorization` (`menu_auth_id`, `api`, `ispost`, `isupdate`) VALUES ('managesectiongoals', '/strategy/api/admin/goal/evidence/comment/save', b'1', b'1');


INSERT INTO `authorization` (`user_role`, `api`) VALUES ('Admin', '/strategy/api/admin/goal/evidence/comment/remove');
INSERT INTO `authorization` (`user_role`, `api`) VALUES ('GRCAdmin', '/strategy/api/admin/goal/evidence/comment/remove');

INSERT INTO `menu_authorization` (`menu_auth_id`, `api`, `isdelete`) VALUES ('managedepartmentgoals', '/strategy/api/admin/goal/evidence/comment/remove', b'1');
INSERT INTO `menu_authorization` (`menu_auth_id`, `api`, `isdelete`) VALUES ('managesectiongoals', '/strategy/api/admin/goal/evidence/comment/remove', b'1');


INSERT INTO `authorization` (`user_role`, `api`) VALUES ('Admin', '/strategy/api/admin/goal/evidence/files/list');
INSERT INTO `authorization` (`user_role`, `api`) VALUES ('GRCAdmin', '/strategy/api/admin/goal/evidence/files/list');

INSERT INTO `menu_authorization` (`menu_auth_id`, `api`, `isget`) VALUES ('managedepartmentgoals', '/strategy/api/admin/goal/evidence/files/list', b'1');
INSERT INTO `menu_authorization` (`menu_auth_id`, `api`, `isget`) VALUES ('managesectiongoals', '/strategy/api/admin/goal/evidence/files/list', b'1');


INSERT INTO `authorization` (`user_role`, `api`) VALUES ('Admin', '/strategy/api/admin/goal/evidence/file/upload');
INSERT INTO `authorization` (`user_role`, `api`) VALUES ('GRCAdmin', '/strategy/api/admin/goal/evidence/file/upload');

INSERT INTO `menu_authorization` (`menu_auth_id`, `api`, `ispost`) VALUES ('managedepartmentgoals', '/strategy/api/admin/goal/evidence/file/upload', b'1');
INSERT INTO `menu_authorization` (`menu_auth_id`, `api`, `ispost`) VALUES ('managesectiongoals', '/strategy/api/admin/goal/evidence/file/upload', b'1');


INSERT INTO `authorization` (`user_role`, `api`) VALUES ('Admin', '/strategy/api/admin/goal/evidence/files/upload');
INSERT INTO `authorization` (`user_role`, `api`) VALUES ('GRCAdmin', '/strategy/api/admin/goal/evidence/files/upload');

INSERT INTO `menu_authorization` (`menu_auth_id`, `api`, `ispost`) VALUES ('managedepartmentgoals', '/strategy/api/admin/goal/evidence/files/upload', b'1');
INSERT INTO `menu_authorization` (`menu_auth_id`, `api`, `ispost`) VALUES ('managesectiongoals', '/strategy/api/admin/goal/evidence/files/upload', b'1');


INSERT INTO `authorization` (`user_role`, `api`) VALUES ('Admin', '/strategy/api/admin/goal/evidence/remove');
INSERT INTO `authorization` (`user_role`, `api`) VALUES ('GRCAdmin', '/strategy/api/admin/goal/evidence/remove');

INSERT INTO `menu_authorization` (`menu_auth_id`, `api`, `isdelete`) VALUES ('managedepartmentgoals', '/strategy/api/admin/goal/evidence/remove', b'1');
INSERT INTO `menu_authorization` (`menu_auth_id`, `api`, `isdelete`) VALUES ('managesectiongoals', '/strategy/api/admin/goal/evidence/remove', b'1');


INSERT INTO `authorization` (`user_role`, `api`) VALUES ('Admin', '/strategy/api/admin/goal/evidence/file/remove');
INSERT INTO `authorization` (`user_role`, `api`) VALUES ('GRCAdmin', '/strategy/api/admin/goal/evidence/file/remove');

INSERT INTO `menu_authorization` (`menu_auth_id`, `api`, `isdelete`) VALUES ('managedepartmentgoals', '/strategy/api/admin/goal/evidence/file/remove', b'1');
INSERT INTO `menu_authorization` (`menu_auth_id`, `api`, `isdelete`) VALUES ('managesectiongoals', '/strategy/api/admin/goal/evidence/file/remove', b'1');


INSERT INTO `authorization` (`user_role`, `api`) VALUES ('Admin', '/strategy/api/admin/goal/evidence/save');
INSERT INTO `authorization` (`user_role`, `api`) VALUES ('GRCAdmin', '/strategy/api/admin/goal/evidence/save');

INSERT INTO `menu_authorization` (`menu_auth_id`, `api`, `ispost`) VALUES ('managedepartmentgoals', '/strategy/api/admin/goal/evidence/save', b'1');
INSERT INTO `menu_authorization` (`menu_auth_id`, `api`, `ispost`) VALUES ('managesectiongoals', '/strategy/api/admin/goal/evidence/save', b'1');


INSERT INTO `authorization` (`user_role`, `api`) VALUES ('Admin', '/strategy/api/admin/authority/goal/weight');
INSERT INTO `authorization` (`user_role`, `api`) VALUES ('GRCAdmin', '/strategy/api/admin/authority/goal/weight');

INSERT INTO `menu_authorization` (`menu_auth_id`, `api`, `isget`) VALUES ('manageauthoritygoals', '/strategy/api/admin/authority/goal/weight', b'1');


INSERT INTO `authorization` (`user_role`, `api`) VALUES ('Admin', '/strategy/api/admin/department/goal/weight');
INSERT INTO `authorization` (`user_role`, `api`) VALUES ('GRCAdmin', '/strategy/api/admin/department/goal/weight');

INSERT INTO `menu_authorization` (`menu_auth_id`, `api`, `isget`) VALUES ('managedepartmentgoals', '/strategy/api/admin/department/goal/weight', b'1');


INSERT INTO `authorization` (`user_role`, `api`) VALUES ('Admin', '/strategy/api/admin/section/goal/weight');
INSERT INTO `authorization` (`user_role`, `api`) VALUES ('GRCAdmin', '/strategy/api/admin/section/goal/weight');

INSERT INTO `menu_authorization` (`menu_auth_id`, `api`, `isget`) VALUES ('managesectiongoals', '/strategy/api/admin/section/goal/weight', b'1');


INSERT INTO `authorization` (`user_role`, `api`) VALUES ('Admin', '/strategy/api/admin/year/list');
INSERT INTO `authorization` (`user_role`, `api`) VALUES ('GRCAdmin', '/strategy/api/admin/year/list');

INSERT INTO `menu_authorization` (`menu_auth_id`, `api`, `isget`) VALUES ('manageauthoritygoals', '/strategy/api/admin/year/list', b'1');


INSERT INTO `authorization` (`user_role`, `api`) VALUES ('Admin', '/strategy/api/admin/goal/status/list');
INSERT INTO `authorization` (`user_role`, `api`) VALUES ('GRCAdmin', '/strategy/api/admin/goal/status/list');

INSERT INTO `menu_authorization` (`menu_auth_id`, `api`, `isget`) VALUES ('manageauthoritygoals', '/strategy/api/admin/goal/status/list', b'1');

 


INSERT INTO `menu_authorization` (`menu_auth_id`, `api`, `isget`, `ispost`, `isupdate`, `isdelete`, `isconfiguration`, `accessibleaction`) VALUES ('manageauthoritygoals', 'UpdateExpectedWeight', b'0', b'1', b'1', b'0', b'0', 'Update Expected Weight');


INSERT INTO `menu_authorization` (`menu_auth_id`, `api`, `isget`, `ispost`, `isupdate`, `isdelete`, `isconfiguration`, `accessibleaction`) VALUES ('manageauthoritygoals', 'UpdateWeight', b'0', b'1', b'1', b'0', b'0', 'Update Weight');

INSERT INTO `menu` (`id`, `lang`, `name`, `icon`, `order`, `isget`, `ispost`, `isupdate`, `isdelete`, `isconfiguration`, `date_time`, `auth_id`, `show`, `showdropdownlist`, `opendropdownlist`) VALUES ('managestrategyar', 'ar', 'إدارة الاستراتيجية', 'fa fa-folder-o', '300', b'0', b'0', b'0', b'0', b'0', '2025-01-14 09:18:15', 'managestrategy', b'1', b'1', b'0');
INSERT INTO `menu` (`id`, `lang`, `name`, `icon`, `order`, `isget`, `ispost`, `isupdate`, `isdelete`, `isconfiguration`, `date_time`, `auth_id`, `show`, `showdropdownlist`, `opendropdownlist`) VALUES ('managestrategyen', 'en', 'Manage Strategy', 'fa fa-folder-o', '300', b'0', b'0', b'0', b'0', b'0', '2025-01-14 09:18:15', 'managestrategy', b'1', b'1', b'0');


UPDATE `menu` SET `parent_id` = 'managestrategyar' WHERE (`id` = 'manageauthoritygoalsar');
UPDATE `menu` SET `parent_id` = 'managestrategyen' WHERE (`id` = 'manageauthoritygoalsen');
UPDATE `menu` SET `parent_id` = 'managestrategyen' WHERE (`id` = 'managedepartmentgoalsen');
UPDATE `menu` SET `parent_id` = 'managestrategyar' WHERE (`id` = 'managedepartmentgoalsar');
UPDATE `menu` SET `parent_id` = 'managestrategyen' WHERE (`id` = 'managesectiongoalsen');
UPDATE `menu` SET `parent_id` = 'managestrategyar' WHERE (`id` = 'managesectiongoalsar');
UPDATE `menu` SET `order` = '1000' WHERE (`id` = 'settingsar');
UPDATE `menu` SET `order` = '1000' WHERE (`id` = 'settingsen');


UPDATE `menu` SET `parent_id` = NULL WHERE (`id` = 'logouten');
UPDATE `menu` SET `parent_id` = NULL WHERE (`id` = 'logoutar');



UPDATE `auth`.`menu` SET `icon` = 'fa fa-superpowers', `additionalconfig` = 'Retun All Users', `accessibleactions` = 'Update Expected Weight,Update Weight' WHERE (`id` = 'manageauthoritygoalsar');
UPDATE `auth`.`menu` SET `icon` = 'fa fa-superpowers', `additionalconfig` = 'Retun All Users', `accessibleactions` = 'Update Expected Weight,Update Weight' WHERE (`id` = 'manageauthoritygoalsen');
UPDATE `auth`.`menu` SET `icon` = 'fa fa-briefcase', `additionalconfig` = 'Retun All Users' WHERE (`id` = 'managedepartmentgoalsar');
UPDATE `auth`.`menu` SET `icon` = 'fa fa-briefcase', `additionalconfig` = 'Retun All Users' WHERE (`id` = 'managedepartmentgoalsen');
UPDATE `auth`.`menu` SET `icon` = 'fa fa-tasks', `additionalconfig` = 'Retun All Users', `opendropdownlist` = b'1' WHERE (`id` = 'managesectiongoalsar');
UPDATE `auth`.`menu` SET `icon` = 'fa fa-tasks', `additionalconfig` = 'Retun All Users', `opendropdownlist` = b'1' WHERE (`id` = 'managesectiongoalsen');
UPDATE `auth`.`menu` SET `opendropdownlist` = b'1' WHERE (`id` = 'managestrategyar');
UPDATE `auth`.`menu` SET `opendropdownlist` = b'1' WHERE (`id` = 'managestrategyen');


INSERT INTO `auth`.`authorization` (`user_role`, `api`, `date_time`, `enable`) VALUES ('Admin', '/api/admin/team/list', '2025-02-06 15:29:31', b'1');
INSERT INTO `auth`.`authorization` (`user_role`, `api`, `date_time`, `enable`) VALUES ('Admin', '/api/admin/team/remove', '2025-02-07 10:45:19', b'1');
INSERT INTO `auth`.`authorization` (`user_role`, `api`, `date_time`, `enable`) VALUES ('Admin', '/api/admin/team/save', '2025-02-07 10:44:35', b'1');
INSERT INTO `auth`.`authorization` (`user_role`, `api`, `date_time`, `enable`) VALUES ('GRCAdmin', '/api/admin/team/list', '2025-02-07 09:47:03', b'1');
INSERT INTO `auth`.`authorization` (`user_role`, `api`, `date_time`, `enable`) VALUES ('GRCAdmin', '/api/admin/team/remove', '2025-02-07 10:45:19', b'1');
INSERT INTO `auth`.`authorization` (`user_role`, `api`, `date_time`, `enable`) VALUES ('GRCAdmin', '/api/admin/team/save', '2025-02-07 10:44:35', b'1');

INSERT INTO `auth`.`menu_authorization` (`menu_auth_id`, `api`, `date_time`, `isget`, `ispost`, `isupdate`, `isdelete`, `isconfiguration`) VALUES ('manageteams', '/api/admin/team/list', '2025-02-06 12:18:02', b'1', b'0', b'0', b'0', b'0');
INSERT INTO `auth`.`menu_authorization` (`menu_auth_id`, `api`, `date_time`, `isget`, `ispost`, `isupdate`, `isdelete`, `isconfiguration`) VALUES ('manageroles', '/api/admin/team/list', '2025-02-06 12:21:20', b'1', b'0', b'0', b'0', b'0');
INSERT INTO `auth`.`menu_authorization` (`menu_auth_id`, `api`, `date_time`, `isget`, `ispost`, `isupdate`, `isdelete`, `isconfiguration`) VALUES ('manageteams', '/api/admin/team/save', '2025-02-07 10:44:35', b'0', b'1', b'1', b'0', b'0');
INSERT INTO `auth`.`menu_authorization` (`menu_auth_id`, `api`, `date_time`, `isget`, `ispost`, `isupdate`, `isdelete`, `isconfiguration`) VALUES ('manageteams', '/api/admin/team/remove', '2025-02-07 10:45:19', b'0', b'0', b'0', b'1', b'0');



INSERT INTO `auth`.`menu` (`id`, `lang`, `name`, `href`, `icon`, `order`, `isget`, `ispost`, `isupdate`, `isdelete`, `isconfiguration`, `additionalconfig`, `parent_id`, `auth_id`, `showdropdownlist`, `opendropdownlist`) VALUES ('manageteamsar', 'ar', 'الفرق', 'manageteams', 'fa fa-code-fork', '235', b'0', b'0', b'0', b'0', b'0', 'managear', 'manageteams', b'1', b'1', b'0');
INSERT INTO `auth`.`menu` (`id`, `lang`, `name`, `href`, `icon`, `order`, `isget`, `ispost`, `isupdate`, `isdelete`, `isconfiguration`, `additionalconfig`, `parent_id`, `auth_id`, `showdropdownlist`, `opendropdownlist`) VALUES ('manageteamsen', 'en', 'Teams', 'manageteams', 'fa fa-code-fork', '225', b'0', b'0', b'0', b'0', b'0', 'manageen', 'manageteams', b'1', b'1', b'0');