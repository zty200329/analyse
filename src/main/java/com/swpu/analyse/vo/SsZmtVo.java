package com.swpu.analyse.vo;

import lombok.Data;

/**
 * @author cyg
 * @date 2019/5/26 下午11:17
 **/
@Data
public class SsZmtVo {

    /**
     * 科目
     **/
    private String name;
    /**
     * 有效成绩人数
     **/
    private Integer yxcjrs;
    /**
     * 平均分
     **/
    private double averageScore;
    /**
     * 按平均分排名
     **/
    private Integer pm;
    /**
     * 最高分
     **/
    private double maxScore;
    /**
     * 最高分
     **/
    private double minScore;
    /**
     * 优生人数
     **/
    private Integer excellentNum;
    /**
     * 优生率
     **/
    private double excellentRate;
    /**
     * 及格人数
     **/
    private Integer passNum;
    /**
     * 及格率
     **/
    private double passRate;
    /**
     * 高差
     **/
    private double heightDifference;
}
