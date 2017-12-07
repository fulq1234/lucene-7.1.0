
DROP TABLE IF EXISTS tbl_goods;
CREATE TABLE tbl_goods (
  `id` int(8) NOT NULL AUTO_INCREMENT,
  `title` varchar(32) DEFAULT NULL,
  `content` varchar(32) DEFAULT NULL,
  `tag` varchar(32) DEFAULT NULL,
  `url` varchar(32) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records 
-- ----------------------------
INSERT INTO tbl_goods VALUES ('1', 'ѧ���', 'ѧ����ʲô�', '����', 'http://www.baidu.com');
INSERT INTO tbl_goods VALUES ('2', 'ɾ��', 'ɾ����Ƭ', '����', 'http://laodongguanx.com');
INSERT INTO tbl_goods VALUES ('3', '���ŵ�', '��ע�㣬�²���', '�ǰ�', 'http://www.kusile.com');
INSERT INTO tbl_goods VALUES ('4', '���˰�', '����', 'վվ', 'htttp://www.bkk.com');
INSERT INTO tbl_goods VALUES ('5', 'android', 'ԭ����� ', 'android', 'http://www.dkkdk.com');
