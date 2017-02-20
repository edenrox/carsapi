CREATE DATABASE `cars` /*!40100 DEFAULT CHARACTER SET utf8 */;

CREATE TABLE `country` (
  `code` char(2) NOT NULL,
  `name` varchar(80) NOT NULL,
  PRIMARY KEY (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `make` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(80) NOT NULL,
  `countryCode` char(2) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `countryCode_idx` (`countryCode`),
  CONSTRAINT `countryCode` FOREIGN KEY (`countryCode`) REFERENCES `country` (`code`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `model` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `makeId` int(11) NOT NULL,
  `name` varchar(80) NOT NULL,
  `type` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `makeId` (`makeId`),
  KEY `type` (`type`),
  CONSTRAINT `makeId` FOREIGN KEY (`makeId`) REFERENCES `make` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;