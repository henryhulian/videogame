DROP TABLE IF EXISTS `User`; 
CREATE TABLE `User` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `userName` varchar(50) NOT NULL,
  `password` varchar(50) DEFAULT NULL,
  `createTime` datetime DEFAULT NULL,
  `roles` varchar(50) DEFAULT NULL,
  `section` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `userName` (`userName`),
  KEY `createTime` (`createTime`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `wallet`; 
CREATE TABLE `wallet` (
  `userId` bigint(20) NOT NULL,
  `balance` decimal(19,2) DEFAULT NULL,
  PRIMARY KEY (`userId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

#测试资源
INSERT INTO `Wallet` (`userId`,`balance`) VALUES (100001,'0.00');
