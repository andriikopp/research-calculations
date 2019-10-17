-- --------------------------------------------------------
-- Host:                         127.0.0.1
-- Server version:               10.1.34-MariaDB - mariadb.org binary distribution
-- Server OS:                    Win32
-- HeidiSQL Version:             9.4.0.5125
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;


-- Dumping database structure for bpmai
DROP DATABASE IF EXISTS `bpmai`;
CREATE DATABASE IF NOT EXISTS `bpmai` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci */;
USE `bpmai`;

-- Dumping structure for table bpmai.d_features
DROP TABLE IF EXISTS `d_features`;
CREATE TABLE IF NOT EXISTS `d_features` (
  `features_id` varchar(5) COLLATE utf8mb4_unicode_ci NOT NULL,
  `description` text COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`features_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Dumping data for table bpmai.d_features: ~0 rows (approximately)
DELETE FROM `d_features`;
/*!40000 ALTER TABLE `d_features` DISABLE KEYS */;
/*!40000 ALTER TABLE `d_features` ENABLE KEYS */;

-- Dumping structure for table bpmai.d_model
DROP TABLE IF EXISTS `d_model`;
CREATE TABLE IF NOT EXISTS `d_model` (
  `model_id` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL,
  `name` text COLLATE utf8mb4_unicode_ci NOT NULL,
  `description` text COLLATE utf8mb4_unicode_ci NOT NULL,
  `fileName` text COLLATE utf8mb4_unicode_ci NOT NULL,
  `model` text COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`model_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Dumping data for table bpmai.d_model: ~0 rows (approximately)
DELETE FROM `d_model`;
/*!40000 ALTER TABLE `d_model` DISABLE KEYS */;
/*!40000 ALTER TABLE `d_model` ENABLE KEYS */;

-- Dumping structure for table bpmai.d_notation
DROP TABLE IF EXISTS `d_notation`;
CREATE TABLE IF NOT EXISTS `d_notation` (
  `notation_id` int(11) NOT NULL,
  `name` text COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`notation_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Dumping data for table bpmai.d_notation: ~4 rows (approximately)
DELETE FROM `d_notation`;
/*!40000 ALTER TABLE `d_notation` DISABLE KEYS */;
INSERT INTO `d_notation` (`notation_id`, `name`) VALUES
	(1, 'BPMN'),
	(2, 'EPC'),
	(3, 'IDEF0'),
	(4, 'DFD');
/*!40000 ALTER TABLE `d_notation` ENABLE KEYS */;

-- Dumping structure for table bpmai.d_time
DROP TABLE IF EXISTS `d_time`;
CREATE TABLE IF NOT EXISTS `d_time` (
  `time_id` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL,
  `time_stamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `t_day` int(11) NOT NULL,
  `t_month` int(11) NOT NULL,
  `t_year` int(11) NOT NULL,
  PRIMARY KEY (`time_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Dumping data for table bpmai.d_time: ~0 rows (approximately)
DELETE FROM `d_time`;
/*!40000 ALTER TABLE `d_time` DISABLE KEYS */;
/*!40000 ALTER TABLE `d_time` ENABLE KEYS */;

-- Dumping structure for table bpmai.f_analysis
DROP TABLE IF EXISTS `f_analysis`;
CREATE TABLE IF NOT EXISTS `f_analysis` (
  `model_id` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL,
  `notation_id` int(11) NOT NULL,
  `time_id` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL,
  `tstamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `size` int(11) NOT NULL,
  `functions` int(11) NOT NULL,
  `connectorsBalance` decimal(5,2) NOT NULL,
  `functionsBalance` decimal(5,2) NOT NULL,
  `startEvents` int(11) NOT NULL,
  `endEvents` int(11) NOT NULL,
  `mismatch` decimal(5,2) NOT NULL,
  `orConnectors` int(11) NOT NULL,
  `results` text COLLATE utf8mb4_unicode_ci NOT NULL,
  `quality` decimal(5,2) NOT NULL,
  PRIMARY KEY (`model_id`,`notation_id`,`time_id`),
  KEY `FK_f_analysis_d_notation` (`notation_id`),
  KEY `FK_f_analysis_d_time` (`time_id`),
  CONSTRAINT `FK_f_analysis_d_model` FOREIGN KEY (`model_id`) REFERENCES `d_model` (`model_id`),
  CONSTRAINT `FK_f_analysis_d_notation` FOREIGN KEY (`notation_id`) REFERENCES `d_notation` (`notation_id`),
  CONSTRAINT `FK_f_analysis_d_time` FOREIGN KEY (`time_id`) REFERENCES `d_time` (`time_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Dumping data for table bpmai.f_analysis: ~0 rows (approximately)
DELETE FROM `f_analysis`;
/*!40000 ALTER TABLE `f_analysis` DISABLE KEYS */;
/*!40000 ALTER TABLE `f_analysis` ENABLE KEYS */;

-- Dumping structure for table bpmai.f_partition
DROP TABLE IF EXISTS `f_partition`;
CREATE TABLE IF NOT EXISTS `f_partition` (
  `model_id` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL,
  `features_id` varchar(5) COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`model_id`,`features_id`),
  KEY `FK_f_partition_d_features` (`features_id`),
  CONSTRAINT `FK_f_partition_d_features` FOREIGN KEY (`features_id`) REFERENCES `d_features` (`features_id`),
  CONSTRAINT `FK_f_partition_d_model` FOREIGN KEY (`model_id`) REFERENCES `d_model` (`model_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Dumping data for table bpmai.f_partition: ~0 rows (approximately)
DELETE FROM `f_partition`;
/*!40000 ALTER TABLE `f_partition` DISABLE KEYS */;
/*!40000 ALTER TABLE `f_partition` ENABLE KEYS */;

-- Dumping structure for table bpmai.f_recommendations
DROP TABLE IF EXISTS `f_recommendations`;
CREATE TABLE IF NOT EXISTS `f_recommendations` (
  `model_id` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL,
  `recommendation_id` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL,
  `recommendation` text COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`model_id`,`recommendation_id`),
  CONSTRAINT `FK_f_recommendations_d_model` FOREIGN KEY (`model_id`) REFERENCES `d_model` (`model_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Dumping data for table bpmai.f_recommendations: ~0 rows (approximately)
DELETE FROM `f_recommendations`;
/*!40000 ALTER TABLE `f_recommendations` DISABLE KEYS */;
/*!40000 ALTER TABLE `f_recommendations` ENABLE KEYS */;

/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
