package com.swpu.analyse.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * @author cyg
 * @date 2019/5/26 下午11:17
 **/
@Entity
@Data
public class SsZmt {
    
    @Id
    private String id;
    /**
     * 时间
     **/
    private String time;
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
    /**
     * 满分
     **/
    private double mf;
}
