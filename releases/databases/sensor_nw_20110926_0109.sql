-- MySQL Administrator dump 1.4
--
-- ------------------------------------------------------
-- Server version	5.1.37-1ubuntu5.5


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;


--
-- Create schema sensor_nw
--

CREATE DATABASE IF NOT EXISTS sensor_nw;
USE sensor_nw;
CREATE TABLE  `sensor_nw`.`data` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `imei` text NOT NULL,
  `datetime` datetime NOT NULL,
  `latitude` double NOT NULL,
  `reading` double NOT NULL,
  `job_id` bigint(20) NOT NULL,
  `longitude` double NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=116 DEFAULT CHARSET=latin1;
INSERT INTO `sensor_nw`.`data` VALUES  (89,'000000000000000','2011-09-17 23:35:10',0,0,2,0),
 (90,'000000000000000','2011-09-17 23:36:10',0,0,2,0),
 (91,'000000000000000','2011-09-17 23:37:10',0,0,2,0),
 (92,'000000000000000','2011-09-17 23:38:10',0,0,2,0),
 (93,'000000000000000','2011-09-17 23:39:10',0,0,2,0),
 (94,'000000000000000','2011-09-17 23:40:10',0,0,2,0),
 (95,'000000000000000','2011-09-17 23:41:10',0,0,2,0),
 (96,'000000000000000','2011-09-17 23:42:11',0,0,2,0),
 (97,'000000000000000','2011-09-17 23:43:11',0,0,2,0),
 (98,'000000000000000','2011-09-17 23:44:11',0,0,2,0),
 (99,'000000000000000','2011-09-17 23:45:11',0,0,2,0),
 (100,'000000000000000','2011-09-17 23:46:11',0,0,2,0),
 (101,'000000000000000','2011-09-17 23:47:11',0,0,2,0),
 (102,'000000000000000','2011-09-17 23:48:11',0,0,2,0),
 (103,'000000000000000','2011-09-17 23:49:11',0,0,2,0),
 (104,'000000000000000','2011-09-17 23:50:11',0,0,2,0),
 (105,'000000000000000','2011-09-17 23:51:11',0,0,2,0),
 (106,'000000000000000','2011-09-17 23:52:11',0,0,2,0),
 (107,'000000000000000','2011-09-17 23:53:11',0,0,2,0);
INSERT INTO `sensor_nw`.`data` VALUES  (108,'000000000000000','2011-09-17 23:54:11',0,0,2,0),
 (109,'000000000000000','2011-09-17 23:55:12',0,0,2,0),
 (110,'000000000000000','2011-09-17 23:56:12',0,0,2,0),
 (111,'000000000000000','2011-09-17 23:57:12',0,0,2,0),
 (112,'000000000000000','2011-09-17 23:58:12',0,0,2,0),
 (113,'000000000000000','2011-09-18 00:49:06',0,0,2,0),
 (114,'000000000000000','2011-09-18 01:01:33',0,0,2,0),
 (115,'000000000000000','2011-09-18 12:09:32',0,0,2,0);
CREATE TABLE  `sensor_nw`.`job` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `datetime` datetime NOT NULL,
  `sensor_id` bigint(20) NOT NULL,
  `frequency` int(11) NOT NULL COMMENT 'frequency in minutes',
  `nodes` int(11) NOT NULL COMMENT 'number of nodes need to collect data. This value is reducing while assigning work to nodes.',
  `user_id` bigint(20) NOT NULL,
  `description` text,
  `longitude` double NOT NULL COMMENT 'GPS location',
  `loc_range` double NOT NULL COMMENT 'valid range from the specified GPS location',
  `node_count` int(11) NOT NULL DEFAULT '0' COMMENT 'Status\nServer ::  running(1), completed(2), expired(3)\nMobile :: new(1), running(2), completed(3), hold(4)',
  `latitude` double NOT NULL COMMENT 'GPS location',
  `expire_time` datetime DEFAULT NULL COMMENT 'Only for Server. Till this date. If this is null it will not expire till all nodes executes.',
  `start_time` datetime DEFAULT NULL COMMENT 'If there is start time this job need to be started at that time and if not start immideatly.',
  `time_period` int(11) NOT NULL COMMENT 'hh:mm:ss  for how long hours ',
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=10 DEFAULT CHARSET=latin1;
INSERT INTO `sensor_nw`.`job` VALUES  (1,'2011-09-10 18:36:19',1,5,5,1,NULL,-74.0444,5838,1,40.6892,'0000-00-00 00:00:00','2011-09-10 18:36:19',24),
 (2,'2011-09-10 18:36:19',1,5,5,1,'Test record for the emulator',0,10,5,0,'0000-00-00 00:00:00','2011-09-10 18:36:19',24),
 (9,'2011-09-25 13:28:34',1,20,4,1,'This added by my phone. Integration test.',222.2,3.3,0,1111.1,'2011-10-25 13:27:33','2011-09-25 13:27:33',10);
CREATE TABLE  `sensor_nw`.`sensor` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` text NOT NULL,
  `description` text,
  `datetime` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;
INSERT INTO `sensor_nw`.`sensor` VALUES  (1,'Magnetic Filed','Magnetic field sensor to measure field of different places.','2011-09-24 17:37:56');
CREATE TABLE  `sensor_nw`.`user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `username` text NOT NULL,
  `password` text NOT NULL,
  `fullname` text NOT NULL,
  `imei` text NOT NULL,
  `rank` int(11) NOT NULL DEFAULT '0' COMMENT 'this will increment every time user uploads data',
  `datetime` datetime NOT NULL,
  `email` text NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uniq_username` (`username`(20))
) ENGINE=MyISAM AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;
INSERT INTO `sensor_nw`.`user` VALUES  (1,'thisara','thilanka','Thisara Rupasinghe','0000-0000-0000',1,'0000-00-00 00:00:00',''),
 (2,'achala','thilanka','Achala Rupasinghe','0000-0000-0000',0,'2011-09-24 13:27:41','thisarattr@yahoo.com');



/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
