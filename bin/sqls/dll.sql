CREATE DATABASE core_server DEFAULT CHARSET utf8mb4;

USE core_server;

CREATE TABLE `message` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `version` bigint(20) DEFAULT NULL,
  `index_` varchar(20) NOT NULL,
  `level_` varchar(20) NOT NULL,
  `content` varchar(1000) NOT NULL,
  `create_on` datetime(6) NOT NULL,
  `accept_on` datetime(6) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `version` bigint(20) DEFAULT NULL,
  `enabled` bit(1) NOT NULL,
  `username` varchar(32) NOT NULL,
  `password` varchar(32) NOT NULL,
  `create_on` datetime(6) NOT NULL,
  `logged_on` datetime(6) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_sb8bbouer5wak8vyiiy4pf2bx` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `user_roles` (
  `id` bigint(20) NOT NULL,
  `roles` varchar(20) NOT NULL,
  PRIMARY KEY (`id`,`roles`),
  CONSTRAINT `FKsrdrenljg5rfi2ceuxfsx23oh` FOREIGN KEY (`id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `token` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `version` bigint(20) DEFAULT NULL,
  `uuid` varchar(36) NOT NULL,
  `access_on` datetime(6) NOT NULL,
  `expire_on` datetime(6) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_k74apnrvhamiburvb8hjwuh7h` (`uuid`),
  KEY `FKe32ek7ixanakfqsdaokm4q9y2` (`user_id`),
  CONSTRAINT `FKe32ek7ixanakfqsdaokm4q9y2` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `disc` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `version` bigint(20) DEFAULT NULL,
  `asin` varchar(20) NOT NULL,
  `title` varchar(500) NOT NULL,
  `titlecn` varchar(255) DEFAULT NULL,
  `disc_type` varchar(20) NOT NULL,
  `release_date` date DEFAULT NULL,
  `this_rank` int(11) DEFAULT NULL,
  `prev_rank` int(11) DEFAULT NULL,
  `add_point` int(11) DEFAULT NULL,
  `sum_point` int(11) DEFAULT NULL,
  `pow_point` int(11) DEFAULT NULL,
  `create_on` datetime(6) DEFAULT NULL,
  `update_on` datetime(6) DEFAULT NULL,
  `modify_on` datetime(6) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_t9eafej76medxt6ohdfwpb0fp` (`asin`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `group_` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `version` bigint(20) DEFAULT NULL,
  `index_` varchar(255) NOT NULL,
  `title_` varchar(255) NOT NULL,
  `status_type` varchar(20) NOT NULL,
  `update_type` varchar(20) NOT NULL,
  `update_date` date DEFAULT NULL,
  `last_update` datetime(6) DEFAULT NULL,
  `disc_count` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_knlkpp0qcu6udtjj3n9m1uu27` (`index_`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `group_discs` (
  `id` bigint(20) NOT NULL,
  `disc_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`,`disc_id`),
  KEY `FKbjop9pwmyhvs40kwsqqdv82h9` (`disc_id`),
  CONSTRAINT `FKa502a95r2r34xe6ilto1eirqr` FOREIGN KEY (`id`) REFERENCES `group_` (`id`),
  CONSTRAINT `FKbjop9pwmyhvs40kwsqqdv82h9` FOREIGN KEY (`disc_id`) REFERENCES `disc` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `record_date` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `version` bigint(20) DEFAULT NULL,
  `date` date NOT NULL,
  `add_point` double DEFAULT NULL,
  `sum_point` double DEFAULT NULL,
  `pow_point` double DEFAULT NULL,
  `aver_rank` double DEFAULT NULL,
  `disc_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK67ohgfmbfj54anymgbn7ie4t5` (`disc_id`),
  CONSTRAINT `FK67ohgfmbfj54anymgbn7ie4t5` FOREIGN KEY (`disc_id`) REFERENCES `disc` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `record_hour` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `version` bigint(20) DEFAULT NULL,
  `date` date NOT NULL,
  `add_point` double DEFAULT NULL,
  `sum_point` double DEFAULT NULL,
  `pow_point` double DEFAULT NULL,
  `rank00` int(11) DEFAULT NULL,
  `rank01` int(11) DEFAULT NULL,
  `rank02` int(11) DEFAULT NULL,
  `rank03` int(11) DEFAULT NULL,
  `rank04` int(11) DEFAULT NULL,
  `rank05` int(11) DEFAULT NULL,
  `rank06` int(11) DEFAULT NULL,
  `rank07` int(11) DEFAULT NULL,
  `rank08` int(11) DEFAULT NULL,
  `rank09` int(11) DEFAULT NULL,
  `rank10` int(11) DEFAULT NULL,
  `rank11` int(11) DEFAULT NULL,
  `rank12` int(11) DEFAULT NULL,
  `rank13` int(11) DEFAULT NULL,
  `rank14` int(11) DEFAULT NULL,
  `rank15` int(11) DEFAULT NULL,
  `rank16` int(11) DEFAULT NULL,
  `rank17` int(11) DEFAULT NULL,
  `rank18` int(11) DEFAULT NULL,
  `rank19` int(11) DEFAULT NULL,
  `rank20` int(11) DEFAULT NULL,
  `rank21` int(11) DEFAULT NULL,
  `rank22` int(11) DEFAULT NULL,
  `rank23` int(11) DEFAULT NULL,
  `disc_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKdwsifsykvmjtu7x1lfsis0trl` (`disc_id`),
  CONSTRAINT `FKdwsifsykvmjtu7x1lfsis0trl` FOREIGN KEY (`disc_id`) REFERENCES `disc` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `SPRING_SESSION` (
  `PRIMARY_ID` char(36) NOT NULL,
  `SESSION_ID` char(36) NOT NULL,
  `CREATION_TIME` bigint(20) NOT NULL,
  `LAST_ACCESS_TIME` bigint(20) NOT NULL,
  `MAX_INACTIVE_INTERVAL` int(11) NOT NULL,
  `EXPIRY_TIME` bigint(20) NOT NULL,
  `PRINCIPAL_NAME` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`PRIMARY_ID`),
  UNIQUE KEY `SPRING_SESSION_IX1` (`SESSION_ID`),
  KEY `SPRING_SESSION_IX2` (`EXPIRY_TIME`),
  KEY `SPRING_SESSION_IX3` (`PRINCIPAL_NAME`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;

CREATE TABLE `SPRING_SESSION_ATTRIBUTES` (
  `SESSION_PRIMARY_ID` char(36) NOT NULL,
  `ATTRIBUTE_NAME` varchar(200) NOT NULL,
  `ATTRIBUTE_BYTES` blob NOT NULL,
  PRIMARY KEY (`SESSION_PRIMARY_ID`,`ATTRIBUTE_NAME`),
  CONSTRAINT `SPRING_SESSION_ATTRIBUTES_FK` FOREIGN KEY (`SESSION_PRIMARY_ID`) REFERENCES `SPRING_SESSION` (`PRIMARY_ID`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;
