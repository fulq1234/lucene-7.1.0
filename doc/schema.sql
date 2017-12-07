
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
INSERT INTO tbl_goods VALUES ('1', '学生活动', '学生有什么活动', '宿舍', 'http://www.baidu.com');
INSERT INTO tbl_goods VALUES ('2', '删除', '删除照片', '几寸', 'http://laodongguanx.com');
INSERT INTO tbl_goods VALUES ('3', '备着点', '备注点，怕不够', '是吧', 'http://www.kusile.com');
INSERT INTO tbl_goods VALUES ('4', '算了吧', '胶带', '站站', 'htttp://www.bkk.com');
INSERT INTO tbl_goods VALUES ('5', 'android', '原地溜达 ', 'android', 'http://www.dkkdk.com');
