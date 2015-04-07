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

DROP TABLE IF EXISTS `Session`;
CREATE TABLE `Session` (
  `token` varchar(50) NOT NULL,
  `tokenKey` varchar(50) DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  `roles` varchar(50) DEFAULT NULL,
  `userId` int(10) unsigned DEFAULT NULL,
  `userName` varchar(50) DEFAULT NULL,
  `lastAccessTime` datetime DEFAULT NULL,
  `createTime` datetime DEFAULT NULL,
  PRIMARY KEY (`token`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
#测试资源
insert into `user` (`id`, `userName`, `password`, `createTime`, `roles`, `section`) values('1','test001','vLFfghR5tNV3K9DKhmwArV+SbjWAcgZZzIDTnJ0JgCo=','2015-04-14 21:06:59','user','c001-s001');
