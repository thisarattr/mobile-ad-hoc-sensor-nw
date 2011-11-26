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
  `user_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_data_userid` (`user_id`),
  KEY `fk_data_jobid` (`job_id`),
  CONSTRAINT `fk_data_jobid` FOREIGN KEY (`job_id`) REFERENCES `job` (`id`) ON UPDATE CASCADE,
  CONSTRAINT `fk_data_userid` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=538 DEFAULT CHARSET=latin1;
INSERT INTO `sensor_nw`.`data` VALUES  (524,'000000000000000','2011-11-02 22:54:35',0,0,1,0,1),
 (525,'000000000000000','2011-11-02 22:55:35',0,0,1,0,1),
 (526,'000000000000000','2011-11-02 22:56:35',0,0,1,0,1),
 (527,'000000000000000','2011-11-02 22:57:35',0,0,1,0,1),
 (528,'000000000000000','2011-11-02 22:58:35',0,0,1,0,1),
 (529,'000000000000000','2011-11-02 22:59:36',0,0,1,0,1),
 (530,'000000000000000','2011-11-02 23:00:36',0,0,1,0,1),
 (531,'000000000000000','2011-11-02 23:01:36',0,0,1,0,1),
 (532,'000000000000000','2011-11-02 23:02:36',0,0,1,0,1),
 (533,'000000000000000','2011-11-02 23:03:36',0,0,1,0,1),
 (534,'000000000000000','2011-11-02 23:04:36',0,0,1,0,1),
 (535,'000000000000000','2011-11-02 23:05:36',0,0,1,0,1),
 (536,'000000000000000','2011-11-02 23:06:36',0,0,1,0,1),
 (537,'000000000000000','2011-11-02 23:07:36',0,566,1,0,1);
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
  `status` int(11) NOT NULL DEFAULT '1' COMMENT '1. runnung\n2. completed\n3. unsuccessful',
  PRIMARY KEY (`id`),
  KEY `fk_job_sensorid` (`sensor_id`),
  KEY `fk_job_userid` (`user_id`),
  CONSTRAINT `fk_job_sensorid` FOREIGN KEY (`sensor_id`) REFERENCES `sensor` (`id`) ON UPDATE CASCADE,
  CONSTRAINT `fk_job_userid` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=latin1;
INSERT INTO `sensor_nw`.`job` VALUES  (1,'2011-09-10 18:36:19',1,1,5,1,'',-74.0444,10000,1,40.6892,'1970-01-01 05:30:00','2011-09-10 18:36:04',24,1),
 (2,'2011-09-10 18:36:19',1,1,5,1,'Test record for the emulator',0,10,0,0,'0000-00-00 00:00:00','2011-09-10 18:36:19',24,1),
 (13,'2011-11-02 19:50:04',1,1,6,1,'Added with phone',80.1898,100,0,6.933079,'1970-01-01 05:30:00','2011-11-02 19:49:02',100,1);
CREATE TABLE  `sensor_nw`.`sensor` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` text NOT NULL,
  `description` text,
  `datetime` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;
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
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;
INSERT INTO `sensor_nw`.`user` VALUES  (1,'thisara','thilanka','Thisara Rupasinghe','0000-0000-0000',1,'0000-00-00 00:00:00','thisarattr@yahoo.com'),
 (2,'achala','thilanka','Achala Rupasinghe','0000-0000-0000',0,'2011-09-24 13:27:41','thisarattr@yahoo.com');



/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
