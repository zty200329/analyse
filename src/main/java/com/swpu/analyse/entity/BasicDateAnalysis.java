package com.swpu.analyse.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * 基本数据分析结果
 *
 * @author cyg
 * @date 2019/5/17 下午11:01
 **/
@Data
@Entity
public class BasicDateAnalysis {

    @Id
    private String time;
    /**
     * 女生报考最多专业
     **/
    private String maxGirlBkMajor;
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
    private Integer wh;
    /**
     * 已婚
     **/
    private Integer yh;
    /**
     * 丧偶
     **/
    private Integer so;
    /**
     * 离婚
     **/
    private Integer lh;
    /**
     * 其他
     **/
    private Integer other;
    /**
     * 一志愿分数最高
     **/
    private Double maxYzy;
    /**
     * 一志愿分数最低
     **/
    private Double minYzy;
    /**
     * 调剂分数最高
     **/
    private Double maxTj;
    /**
     * 调剂分数最低
     **/
    private Double minTj;
    /**
     * 女生录取最多专业
     **/
    private String maxGirlMajor;
    /**
     * 女生录取最少专业
     **/
    private String minGirlMajor;
    /**
     * 一志愿报录比最大专业
     **/
    private String MaxBlYzyMajor;
    /**
     * 一志愿报录比最大值
     **/
    private Double MaxBlYzyValue;
    /**
     * 一志愿报录比最小专业
     **/
    private String minBlYzyMajor;
    /**
     * 一志愿报录比最小值
     **/
    private Double MinBlYzyValue;
    /**
     * 毕业学校之最
     **/
    private String maxByUniversity;
    /**
     * 男生数量
     **/
    private Integer boy;
    /**
     * 女生数量
     **/
    private Integer girl;
    /**
     * 结婚人数最多专业
     **/
    private String maxHyMajor;
    /**
     * 结婚人数最少专业
     **/
    private String minHyMajor;
    /**
     * 汉族人数
     **/
    private Integer han;
    /**
     * 非汉族人数
     **/
    private Integer hanNot;
}
