
DROP TABLE IF EXISTS tbl_goods;
CREATE TABLE tbl_goods (
  `id` int(8) NOT NULL AUTO_INCREMENT,
  `name` varchar(32) DEFAULT NULL,
  `remark` varchar(32) DEFAULT NULL,
  `content` varchar(1000) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records 
-- ----------------------------
INSERT INTO tbl_goods VALUES ('1', '学生活动', '学生有什么活动', '宿舍');
INSERT INTO tbl_goods VALUES ('2', '删除', '删除照片', '几寸');
INSERT INTO tbl_goods VALUES ('3', '备着点', '备注点，怕不够', '是吧');
INSERT INTO tbl_goods VALUES ('4', '算了吧', '胶带', '站站');
INSERT INTO tbl_goods VALUES ('5', 'android', '原地溜达 ', 'android');

update tbl_goods set content ='苦学一周全文检索，由原来的搜索小白，到初次涉猎，感觉每门技术都博大精深，其中精髓亦是不可一日而语。那小博猪就简单介绍一下这一周的学习历程，仅供各位程序猿们参考，这其中不涉及任何私密话题，因此也不用打马赛克了，都是网络分享的开源资料，当然也不涉及任何利益关系。';
