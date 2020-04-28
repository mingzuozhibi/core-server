CREATE DATABASE core_server DEFAULT CHARSET utf8;

USE core_server;

CREATE TABLE `message` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `tag` varchar(20) NOT NULL,
  `level` varchar(20) NOT NULL,
  `accept_on` datetime(6) NOT NULL,
  `create_on` datetime(6) NOT NULL,
  `version` bigint(20) DEFAULT NULL,
  `content` varchar(1000) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `username` varchar(32) NOT NULL,
  `password` varchar(32) NOT NULL,
  `enabled` bit(1) NOT NULL,
  `create_on` datetime(6) NOT NULL,
  `logged_on` datetime(6) DEFAULT NULL,
  `version` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_sb8bbouer5wak8vyiiy4pf2bx` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `user_roles` (
  `user_id` bigint(20) NOT NULL,
  `roles` varchar(255) DEFAULT NULL,
  KEY `FK55itppkw3i07do3h7qoclqd4k` (`user_id`),
  CONSTRAINT `FK55itppkw3i07do3h7qoclqd4k` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `token` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `uuid` varchar(36) NOT NULL,
  `access_on` datetime(6) NOT NULL,
  `expire_on` datetime(6) NOT NULL,
  `version` bigint(20) DEFAULT NULL,
  `user_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_k74apnrvhamiburvb8hjwuh7h` (`uuid`),
  KEY `FKe32ek7ixanakfqsdaokm4q9y2` (`user_id`),
  CONSTRAINT `FKe32ek7ixanakfqsdaokm4q9y2` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `disc` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `asin` varchar(20) NOT NULL,
  `this_rank` int(11) DEFAULT NULL,
  `prev_rank` int(11) DEFAULT NULL,
  `add_point` int(11) DEFAULT NULL,
  `sum_point` int(11) DEFAULT NULL,
  `pow_point` int(11) DEFAULT NULL,
  `disc_type` varchar(20) DEFAULT NULL,
  `release_date` date DEFAULT NULL,
  `create_on` datetime(6) DEFAULT NULL,
  `update_on` datetime(6) DEFAULT NULL,
  `modify_on` datetime(6) DEFAULT NULL,
  `version` bigint(20) DEFAULT NULL,
  `title` varchar(500) NOT NULL,
  `titlecn` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_t9eafej76medxt6ohdfwpb0fp` (`asin`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
