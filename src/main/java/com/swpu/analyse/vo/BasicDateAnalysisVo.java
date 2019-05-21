package com.swpu.analyse.vo;

import lombok.Data;

/**
 * 基本数据分析
 *
 * @author cyg
 * @date 2019/5/17 下午11:01
 **/
@Data
public class BasicDateAnalysisVo {

    /**
     * 女生报考最多专业
     **/
    private String maxGirlBkMajor = "无";
    /**
     * 录取最大年龄
     **/
    private Integer maxAge;
    /**
     * 录取最小年龄
     **/
    private Integer minAge;
    /**
     * 党员人数
     **/
    private Integer partyMember;
    /**
     * 未婚
     **/
    private Integer wh = 0;
    /**
     * 已婚
     **/
    private Integer yh = 0;
    /**
     * 丧偶
     **/
    private Integer so = 0;
    /**
     * 离婚
     **/
    private Integer lh = 0;
    /**
     * 其他
     **/
    private Integer other = 0;
    /**
     * 一志愿分数最高
     **/
    private Double maxYzy = 0d;
    /**
     * 一志愿分数最低
     **/
    private Double minYzy = 0d;
    /**
     * 调剂分数最高
     **/
    private Double maxTj = 0d;
    /**
     * 调剂分数最低
     **/
    private Double minTj = 0d;
    /**
     * 女生录取最多专业
     **/
    private String maxGirlMajor = "无";
    /**
     * 女生录取最少专业
     **/
    private String minGirlMajor = "无";
    /**
     * 一志愿报录比最大专业
     **/
    private String MaxBlYzyMajor = "无";
    /**
     * 一志愿报录比最大值
     **/
    private Double MaxBlYzyValue = 0d;
    /**
     * 一志愿报录比最小专业
     **/
    private String minBlYzyMajor = "无";
    /**
     * 一志愿报录比最小值
     **/
    private Double MinBlYzyValue = 0d;
    /**
     * 毕业学校之最
     **/
    private String maxByUniversity = "无";
    /**
     * 男生数量
     **/
    private Integer boy = 0;
    /**
     * 女生数量
     **/
    private Integer girl = 0;
    /**
     * 结婚人数最多专业
     **/
    private String maxHyMajor = "无";
    /**
     * 结婚人数最少专业
     **/
    private String minHyMajor = "无";
    /**
     * 汉族人数
     **/
    private Integer han = 0;
    /**
     * 非汉族人数
     **/
    private Integer hanNot = 0;
}
