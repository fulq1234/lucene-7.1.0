
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
INSERT INTO tbl_goods VALUES ('1', 'ѧ���', 'ѧ����ʲô�', '����');
INSERT INTO tbl_goods VALUES ('2', 'ɾ��', 'ɾ����Ƭ', '����');
INSERT INTO tbl_goods VALUES ('3', '���ŵ�', '��ע�㣬�²���', '�ǰ�');
INSERT INTO tbl_goods VALUES ('4', '���˰�', '����', 'վվ');
INSERT INTO tbl_goods VALUES ('5', 'android', 'ԭ����� ', 'android');

update tbl_goods set content ='��ѧһ��ȫ�ļ�������ԭ��������С�ף����������ԣ��о�ÿ�ż�������������о������ǲ���һ�ն����С����ͼ򵥽���һ����һ�ܵ�ѧϰ���̣�������λ����Գ�ǲο��������в��漰�κ�˽�ܻ��⣬���Ҳ���ô��������ˣ������������Ŀ�Դ���ϣ���ȻҲ���漰�κ������ϵ��';
