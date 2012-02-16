delimiter $$

CREATE DATABASE `test` /*!40100 DEFAULT CHARACTER SET utf8 */$$

delimiter $$

CREATE TABLE `address` (
  `address_id` int(11) NOT NULL,
  `address_longitude` double DEFAULT NULL,
  `address_latitude` double DEFAULT NULL,
  `address_city` varchar(45) DEFAULT NULL,
  `address_street` varchar(45) DEFAULT NULL,
  `address_building` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`address_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8$$

delimiter $$

CREATE TABLE `query` (
  `query_id` int(11) NOT NULL,
  `query_str` varchar(45) DEFAULT NULL,
  `query_count` int(11) DEFAULT NULL,
  `query_last_updated` datetime DEFAULT NULL,
  PRIMARY KEY (`query_id`),
  UNIQUE KEY `query_str_UNIQUE` (`query_str`),
  UNIQUE KEY `query_last_updated_UNIQUE` (`query_last_updated`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='	'$$

delimiter $$

CREATE TABLE `query_result` (
  `idquery_result` int(11) NOT NULL,
  `query_id` int(11) DEFAULT NULL,
  `vacancy_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`idquery_result`),
  UNIQUE KEY `query_id_UNIQUE` (`query_id`),
  UNIQUE KEY `vacancy_id_UNIQUE` (`vacancy_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8$$


delimiter $$

CREATE TABLE `salary` (
  `salary_id` int(11) NOT NULL,
  `salary_from` int(11) DEFAULT NULL,
  `salary_to` int(11) DEFAULT NULL,
  `salary_currency` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`salary_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8$$

delimiter $$

CREATE TABLE `vacancy` (
  `vacancy_id` int(11) NOT NULL,
  `vacancy_name` varchar(45) DEFAULT NULL,
  `vacancy_address_id` int(11) DEFAULT NULL,
  `vacancy_salary_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`vacancy_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8$$

