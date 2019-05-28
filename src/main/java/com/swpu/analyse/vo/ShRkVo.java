package com.swpu.analyse.vo;

import lombok.Data;

/**
 * @author cyg
 * @date 2019/5/20 下午12:18
 **/
@Data
public class ShRkVo {

    /**
     * 学院代码
     **/
    private String yxsdm;
    /**
     * 学院代码
     **/
    private String yxsmc;
    /**
     * 专业代码
     **/
    private String zzdm;
    /**
     * 专业名称
     **/
    private String zzmc;
    /**
     * 报考学习方式 (1全日制，2非全日制）
     **/
    private String bkxxfs;
    /**
     * 一志愿A
     **/
    private Integer yzyA;
    /**
     * 一志愿B
     **/
    private Integer yzyB;
    /**
     * 一志愿C
     **/
    private Integer yzyC;
    /**
     * 一志愿D
     **/
    private Integer yzyD;
    /**
     * 一志愿E
     **/
    private Integer yzyE;
    /**
     * 调剂A
     **/
    private Integer tjA;
    /**
     * 调剂B
     **/
    private Integer tjB;
    /**
     * 调剂C
     **/
    private Integer tjC;
    /**
     * 调剂D
     **/
    private Integer tjD;
    /**
     * 调剂E
     **/
    private Integer tjE;
    /**
     * 总人数
     **/
    private Integer total;
}
