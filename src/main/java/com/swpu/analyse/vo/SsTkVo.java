package com.swpu.analyse.vo;

import lombok.Data;

/**
 * @author cyg
 * @date 2019/5/26 下午11:20
 **/
@Data
public class SsTkVo {

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
