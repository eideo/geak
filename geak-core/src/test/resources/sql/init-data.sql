-- ----------------------------
--  Init Records of geak_company
-- ----------------------------
BEGIN;
INSERT INTO geak_company VALUES 
(1, '大南门店', '大南门', '大南门'), 
(2, '体育路店', '体育路', '体育路'), 
(3, '食品街店', '食品街', '食品街'), 
(4, '柳巷店', '柳巷', '柳巷'), 
(5, '长风店', '长风', '长风街'), 
(6, '千峰店', '千峰', '千峰路'),
(7, '世贸店', '世贸', '世贸'),
(8, '极客工厂', '工厂', '极客工厂')
;
COMMIT;

-- ----------------------------
--  Init Records of geak_product
-- ----------------------------
BEGIN;
INSERT INTO geak_product VALUES 
('1', '邪恶力量-猎人', '猎人', '密室门票', 'NORMAL', 48, 100, '1'), 
('2', '怪客V的邀请函', '邀请函', '密室门票', 'NORMAL', 48, 100, '1'), 
('3', '谍影神偷', '谍影', '密室门票', 'NORMAL', 48, 100, '1'), 
('4', '时空追捕者', '时空', '密室门票', 'NORMAL', 68, 100, '2'), 
('5', '又见平遥', '平遥', '密室门票', 'NORMAL', 68, 100, '2'), 
('6', '空中监狱', '监狱', '密室门票', 'NORMAL', 68, 100, '2'), 
('7', '生化危机', '生化', '密室门票', 'NORMAL', 68, 100, '2'), 
('8', '失落的文明', '失落', '密室门票', 'NORMAL', 68, 100, '2'), 
('9', '怪客V的使命', '使命', '密室门票', 'NORMAL', 68, 100, '2'), 
('10', '非正常人类研究中心', '非正常', '密室门票', 'NORMAL', 68, 100, '3'), 
('11', '一九二六', '一九二六', '密室门票', 'NORMAL', 68, 100, '3'), 
('12', '侠路相逢', '侠路', '密室门票', 'NORMAL', 68, 100, '3'), 
('13', '藏地密码', '藏地', '密室门票', 'NORMAL', 68, 100, '3'), 
('14', '蛊墓悬棺', '蛊墓', '密室门票', 'NORMAL', 68, 100, '3'), 
('15', '怪客V的越狱', '越狱', '密室门票', 'NORMAL', 68, 100, '3'), 
('16', '审判者', '审判者', '密室门票', 'NORMAL', 68, 100, '4'), 
('17', '星际杯驻地任务', '足球', '密室门票', 'NORMAL', 68, 100, '4'), 
('18', '怪客V的时空罪', '时空罪', '密室门票', 'NORMAL', 68, 100, '4'), 
('19', '夺命酒店', '酒店', '密室门票', 'NORMAL', 68, 100, '4'), 
('20', '愤怒的小鸟', '小鸟', '密室门票', 'NORMAL', 68, 100, '4'), 
('21', '赛琳娜公主', '公主', '密室门票', 'NORMAL', 68, 100, '4'), 
('22', '开封奇案', '开封', '密室门票', 'NORMAL', 48, 100, '5'), 
('23', '法老的诅咒', '法老', '密室门票', 'NORMAL', 48, 100, '5'), 
('24', '秘密潜入', '潜入', '密室门票', 'NORMAL', 48, 100, '5'), 
('25', '暗夜复仇', '暗夜', '密室门票', 'NORMAL', 68, 100, '6'), 
('26', '汉墓疑云', '汉墓', '密室门票', 'NORMAL', 68, 100, '6'), 
('27', '加勒比海盗之惊世珍宝', '海盗', '密室门票', 'NORMAL', 68, 100, '6'), 
('28', '迷失·荒岛余生', '迷失', '密室门票', 'NORMAL', 68, 100, '6'), 
('29', '魔戒·霍比特人', '魔戒', '密室门票', 'NORMAL', 68, 100, '6'), 
('30', '怪客V的邀请函II黑暗之塔', '黑暗之塔', '密室门票', 'NORMAL', 88, 100, '7'), 
('31', '失落的文明II', '失落文明', '密室门票', 'NORMAL', 68, 100, '7'), 
('32', '剑冢', '剑冢', '密室门票', 'NORMAL', 88, 100, '7'), 
('33', '阿育吠陀', '阿育吠陀', '密室门票', 'NORMAL', 88, 100, '7'), 
('34', '深渊迷航', '深渊迷航', '密室门票', 'NORMAL', 68, 100, '7'), 
('35', '米拉库鲁', '米拉库鲁', '密室门票', 'NORMAL', 68, 100, '7'),
('36', '工厂门票', '工厂门票', '工厂门票', 'NORMAL', 38, 38, '8'),
('37', '爆米花(小)', '爆米花(小)', '工厂水吧', 'NORMAL', 10, 10, '8'),
('38', '爆米花(大)', '爆米花(大)', '工厂水吧', 'NORMAL', 20, 20, '8'),
('39', '汽水', '汽水', '工厂水吧', 'NORMAL', 8, 8, '8'),
('40', '奶茶', '奶茶', '工厂水吧', 'NORMAL', 8, 8, '8'),
('41', '咖啡', '咖啡', '工厂水吧', 'NORMAL', 8, 8, '8'),
('42', '茶水(茉莉、普洱、龙井、铁观音)', '茶水', '工厂水吧', 'NORMAL', 38,38, '8'),
('43', '菊花茶', '菊花茶', '工厂水吧', 'NORMAL', 18,18, '8'),
('44', '冰糖', '冰糖', '工厂水吧', 'NORMAL', 2, 2, '8'),
('45', '现磨咖啡', '现磨咖啡', '工厂水吧', 'NORMAL', 19, 19, '8')
;
COMMIT;


-- ----------------------------
--  Init Records of geak_user
-- ----------------------------
BEGIN;
INSERT INTO geak_user VALUES ('2016012301', '2016012301', '麻文强', '15001276389', '', 'NORMAL', 8);
INSERT INTO geak_user VALUES ('2016043002', '2016043002', '杨牧之', '', '', 'NORMAL', 8);
INSERT INTO geak_user VALUES ('2013110101', '2013110101', '韩钰', '', '', 'NORMAL', 8);
INSERT INTO geak_user VALUES ('2014070101', '2014070101', '张小喵', '', '', 'NORMAL', 8);

INSERT INTO `geak_user_company` VALUES ('2016012301', 1);  -- 麻文强
INSERT INTO `geak_user_company` VALUES ('2016012301', 2);  -- 麻文强
INSERT INTO `geak_user_company` VALUES ('2016012301', 3);  -- 麻文强
INSERT INTO `geak_user_company` VALUES ('2016012301', 4);  -- 麻文强
INSERT INTO `geak_user_company` VALUES ('2016012301', 5);  -- 麻文强
INSERT INTO `geak_user_company` VALUES ('2016012301', 6);  -- 麻文强
INSERT INTO `geak_user_company` VALUES ('2016012301', 7);  -- 麻文强
INSERT INTO `geak_user_company` VALUES ('2016012301', 8);  -- 麻文强

INSERT INTO `geak_user_company` VALUES ('2013110101', 1);  -- 韩钰
INSERT INTO `geak_user_company` VALUES ('2013110101', 2);  -- 韩钰
INSERT INTO `geak_user_company` VALUES ('2013110101', 3);  -- 韩钰
INSERT INTO `geak_user_company` VALUES ('2013110101', 4);  -- 韩钰
INSERT INTO `geak_user_company` VALUES ('2013110101', 5);  -- 韩钰
INSERT INTO `geak_user_company` VALUES ('2013110101', 6);  -- 韩钰
INSERT INTO `geak_user_company` VALUES ('2013110101', 7);  -- 韩钰
INSERT INTO `geak_user_company` VALUES ('2013110101', 8);  -- 韩钰

INSERT INTO `geak_user_company` VALUES ('2014070101', 1);  -- 张小喵
INSERT INTO `geak_user_company` VALUES ('2014070101', 2);  -- 张小喵
INSERT INTO `geak_user_company` VALUES ('2014070101', 3);  -- 张小喵
INSERT INTO `geak_user_company` VALUES ('2014070101', 4);  -- 张小喵
INSERT INTO `geak_user_company` VALUES ('2014070101', 5);  -- 张小喵
INSERT INTO `geak_user_company` VALUES ('2014070101', 6);  -- 张小喵
INSERT INTO `geak_user_company` VALUES ('2014070101', 7);  -- 张小喵
INSERT INTO `geak_user_company` VALUES ('2014070101', 8);  -- 张小喵

INSERT INTO `geak_user_company` VALUES ('2016043002', 1);  -- 杨牧之
INSERT INTO `geak_user_company` VALUES ('2016043002', 2);  -- 杨牧之
INSERT INTO `geak_user_company` VALUES ('2016043002', 3);  -- 杨牧之
INSERT INTO `geak_user_company` VALUES ('2016043002', 4);  -- 杨牧之
INSERT INTO `geak_user_company` VALUES ('2016043002', 5);  -- 杨牧之
INSERT INTO `geak_user_company` VALUES ('2016043002', 6);  -- 杨牧之
INSERT INTO `geak_user_company` VALUES ('2016043002', 7);  -- 杨牧之
INSERT INTO `geak_user_company` VALUES ('2016043002', 8);  -- 杨牧之
COMMIT;

