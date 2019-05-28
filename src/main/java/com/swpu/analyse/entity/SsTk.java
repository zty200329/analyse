package com.swpu.analyse.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * @author cyg
 * @date 2019/5/26 下午11:20
 **/
@Entity
@Data
public class SsTk {

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
     * 一志愿平均分
     **/
    private Double yzyPj;
    /**
     * 一志愿方差
     **/
    private Double yzyFc;
    /**
     * 调剂平均分
     **/
    private Double tjPj;
    /**
     * 调剂方差
     **/
    private Double tjFc;
}
