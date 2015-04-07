DROP TABLE IF EXISTS `wallet`; 
CREATE TABLE `wallet` (
  `userId` bigint(20) NOT NULL,
  `balance` decimal(19,2) DEFAULT NULL,
  PRIMARY KEY (`userId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

#测试资源
INSERT INTO `Wallet` (`userId`,`balance`) VALUES (100001,'0.00');
