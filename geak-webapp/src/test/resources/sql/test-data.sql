
-- ----------------------------
--  Records of `geak_user` 
-- ----------------------------
BEGIN;
-- 总部
INSERT INTO `geak_user` VALUES ('2016012301', 2);
INSERT INTO `aa_user` VALUES ('2016012301', '2016012301', '麻文强', '', 'NORMAL');

INSERT INTO `geak_user` VALUES ('2016043001', 2);
INSERT INTO `aa_user` VALUES ('2016043001', '2016043001', '季何', '', 'NORMAL');

INSERT INTO `geak_user` VALUES ('2016043002', 2);
INSERT INTO `aa_user` VALUES ('2016043002', '2016043002', '杨牧之', '', 'NORMAL');

INSERT INTO `geak_user` VALUES ('2014010101', 2);
INSERT INTO `aa_user` VALUES ('2014010101', '2014010101', '郝挺', '', 'NORMAL');

INSERT INTO `geak_user` VALUES ('2013070101', 2);
INSERT INTO `aa_user` VALUES ('2013070101', '2013070101', '吴琪', '', 'NORMAL');

INSERT INTO `geak_user` VALUES ('2014022201', 2);
INSERT INTO `aa_user` VALUES ('2014022201', '2014022201', '何金', '', 'NORMAL');

INSERT INTO `geak_user` VALUES ('2013110101', 2);
INSERT INTO `aa_user` VALUES ('2013110101', '2013110101', '韩钰', '', 'NORMAL');

INSERT INTO `geak_user` VALUES ('2014070101', 2);
INSERT INTO `aa_user` VALUES ('2014070101', '2014070101', '张小喵', '', 'NORMAL');

INSERT INTO `geak_user` VALUES ('2015020101', 2);
INSERT INTO `aa_user` VALUES ('2015020101', '2015020101', '张雯', '', 'NORMAL');

-- 大南门店

-- 体育路店
INSERT INTO `geak_user` VALUES ('2015050701', 2);
INSERT INTO `aa_user` VALUES ('2015050701', '2015050701', '李佳', '', 'NORMAL');

INSERT INTO `geak_user` VALUES ('2014081001', 2);
INSERT INTO `aa_user` VALUES ('2014081001', '2014081001', '张博', '', 'NORMAL');

INSERT INTO `geak_user` VALUES ('2014110101', 2);
INSERT INTO `aa_user` VALUES ('2014110101', '2014110101', '薄佳婧', '', 'NORMAL');

INSERT INTO `geak_user` VALUES ('2015062503', 2);
INSERT INTO `aa_user` VALUES ('2015062503', '2015062503', '宋成', '', 'NORMAL');

INSERT INTO `geak_user` VALUES ('2015062901', 2);
INSERT INTO `aa_user` VALUES ('2015062901', '2015062901', '梁海洋', '', 'NORMAL');

INSERT INTO `geak_user` VALUES ('2015120801', 2);
INSERT INTO `aa_user` VALUES ('2015120801', '2015120801', '闫浩', '', 'NORMAL');

INSERT INTO `geak_user` VALUES ('2015121701', 2);
INSERT INTO `aa_user` VALUES ('2015121701', '2015121701', '戎鹏', '', 'NORMAL');

-- 食品街店
INSERT INTO `geak_user` VALUES ('2014060102', 3);
INSERT INTO `aa_user` VALUES ('2014060102', '2014060102', '肖冰鑫', '', 'NORMAL');

INSERT INTO `geak_user` VALUES ('2015071801', 3);
INSERT INTO `aa_user` VALUES ('2015071801', '2015071801', '范少伟', '', 'NORMAL');

INSERT INTO `geak_user` VALUES ('2015032401', 3);
INSERT INTO `aa_user` VALUES ('2015032401', '2015032401', '郭麟', '', 'NORMAL');

INSERT INTO `geak_user` VALUES ('2015080101', 3);
INSERT INTO `aa_user` VALUES ('2015080101', '2015080101', '窦振刚', '', 'NORMAL');

INSERT INTO `geak_user` VALUES ('2015102302', 3);
INSERT INTO `aa_user` VALUES ('2015102302', '2015102302', '鹿鹏宇', '', 'NORMAL');

-- 柳巷店
INSERT INTO `geak_user` VALUES ('2014060101', 4);
INSERT INTO `aa_user` VALUES ('2014060101', '2014060101', '桂家强', '', 'NORMAL');

INSERT INTO `geak_user` VALUES ('2015040101', 4);
INSERT INTO `aa_user` VALUES ('2015040101', '2015040101', '刘转', '', 'NORMAL');

INSERT INTO `geak_user` VALUES ('2015102301', 4);
INSERT INTO `aa_user` VALUES ('2015102301', '2015102301', '任宏伟', '', 'NORMAL');

INSERT INTO `geak_user` VALUES ('2015121501', 4);
INSERT INTO `aa_user` VALUES ('2015121501', '2015121501', '王强', '', 'NORMAL');

INSERT INTO `geak_user` VALUES ('2016030902', 4);
INSERT INTO `aa_user` VALUES ('2016030902', '2016030902', '张强', '', 'NORMAL');

-- 长风店
INSERT INTO `geak_user` VALUES ('2015060102', 5);
INSERT INTO `aa_user` VALUES ('2015060102', '2015060102', '卫硕', '', 'NORMAL');

INSERT INTO `geak_user` VALUES ('2015063001', 5);
INSERT INTO `aa_user` VALUES ('2015063001', '2015063001', '段梦炜', '', 'NORMAL');

INSERT INTO `geak_user` VALUES ('2015070101', 5);
INSERT INTO `aa_user` VALUES ('2015070101', '2015070101', '郭昕宇', '', 'NORMAL');

-- 千峰店
INSERT INTO `geak_user` VALUES ('2015062501', 6);
INSERT INTO `aa_user` VALUES ('2015062501', '2015062501', '张超', '', 'NORMAL');

INSERT INTO `geak_user` VALUES ('2015062502', 6);
INSERT INTO `aa_user` VALUES ('2015062502', '2015062502', '李运波', '', 'NORMAL');

INSERT INTO `geak_user` VALUES ('2015121801', 6);
INSERT INTO `aa_user` VALUES ('2015121801', '2015121801', '王金湖', '', 'NORMAL');

INSERT INTO `geak_user` VALUES ('2015122002', 6);
INSERT INTO `aa_user` VALUES ('2015122002', '2015122002', '梁起常', '', 'NORMAL');


-- 用户可访问其他门店的记录
INSERT INTO `geak_user_company` VALUES ('2016012301', 1);  -- 麻文强
INSERT INTO `geak_user_company` VALUES ('2016012301', 2);  -- 麻文强
INSERT INTO `geak_user_company` VALUES ('2016012301', 3);  -- 麻文强
INSERT INTO `geak_user_company` VALUES ('2016012301', 4);  -- 麻文强
INSERT INTO `geak_user_company` VALUES ('2016012301', 5);  -- 麻文强
INSERT INTO `geak_user_company` VALUES ('2016012301', 6);  -- 麻文强

INSERT INTO `geak_user_company` VALUES ('2014010101', 1);  -- 郝挺
INSERT INTO `geak_user_company` VALUES ('2014010101', 2);  -- 郝挺
INSERT INTO `geak_user_company` VALUES ('2014010101', 3);  -- 郝挺
INSERT INTO `geak_user_company` VALUES ('2014010101', 4);  -- 郝挺
INSERT INTO `geak_user_company` VALUES ('2014010101', 5);  -- 郝挺
INSERT INTO `geak_user_company` VALUES ('2014010101', 6);  -- 郝挺

INSERT INTO `geak_user_company` VALUES ('2013070101', 1);  -- 吴琪
INSERT INTO `geak_user_company` VALUES ('2013070101', 2);  -- 吴琪
INSERT INTO `geak_user_company` VALUES ('2013070101', 3);  -- 吴琪
INSERT INTO `geak_user_company` VALUES ('2013070101', 4);  -- 吴琪
INSERT INTO `geak_user_company` VALUES ('2013070101', 5);  -- 吴琪
INSERT INTO `geak_user_company` VALUES ('2013070101', 6);  -- 吴琪

INSERT INTO `geak_user_company` VALUES ('2014022201', 1);  -- 何金
INSERT INTO `geak_user_company` VALUES ('2014022201', 2);  -- 何金
INSERT INTO `geak_user_company` VALUES ('2014022201', 3);  -- 何金
INSERT INTO `geak_user_company` VALUES ('2014022201', 4);  -- 何金
INSERT INTO `geak_user_company` VALUES ('2014022201', 5);  -- 何金
INSERT INTO `geak_user_company` VALUES ('2014022201', 6);  -- 何金


INSERT INTO `geak_user_company` VALUES ('2014070101', 1);  -- 张小喵
INSERT INTO `geak_user_company` VALUES ('2014070101', 2);  -- 张小喵
INSERT INTO `geak_user_company` VALUES ('2014070101', 3);  -- 张小喵
INSERT INTO `geak_user_company` VALUES ('2014070101', 4);  -- 张小喵
INSERT INTO `geak_user_company` VALUES ('2014070101', 5);  -- 张小喵
INSERT INTO `geak_user_company` VALUES ('2014070101', 6);  -- 张小喵

INSERT INTO `geak_user_company` VALUES ('2016043001', 1);  -- 季何
INSERT INTO `geak_user_company` VALUES ('2016043001', 2);  -- 季何
INSERT INTO `geak_user_company` VALUES ('2016043001', 3);  -- 季何
INSERT INTO `geak_user_company` VALUES ('2016043001', 4);  -- 季何
INSERT INTO `geak_user_company` VALUES ('2016043001', 5);  -- 季何
INSERT INTO `geak_user_company` VALUES ('2016043001', 6);  -- 季何

INSERT INTO `geak_user_company` VALUES ('2016043002', 1);  -- 杨牧之
INSERT INTO `geak_user_company` VALUES ('2016043002', 2);  -- 杨牧之
INSERT INTO `geak_user_company` VALUES ('2016043002', 3);  -- 杨牧之
INSERT INTO `geak_user_company` VALUES ('2016043002', 4);  -- 杨牧之
INSERT INTO `geak_user_company` VALUES ('2016043002', 5);  -- 杨牧之
INSERT INTO `geak_user_company` VALUES ('2016043002', 6);  -- 杨牧之

COMMIT;

