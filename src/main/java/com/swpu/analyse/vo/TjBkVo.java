package com.swpu.analyse.vo;

import lombok.Data;

/**
 * @author cyg
 * @date 2019/5/28 上午11:21
 **/
@Data
public class TjBkVo {

    /**
     * 考生编号
     **/
    private String ksbh;
    /**
     * 姓名
     **/
    private String xm;
    /**
     * 报考单位
     **/
    private String bkdwmc;
    /**
     * 录取学院代码
     **/
    private String yxsdm;
    /**
     * 录取学院名称
     **/
    private String yxsmc;
    /**
     * 录取专业代码
     **/
    private String zydm;
    /**
     * 录取专业名称
     **/
    private String zymc;
    /**
     * 录取学习方式 (全日制，非全日制）
     **/
    private String xxfsdm;
    /**
     * 985211
     **/
    private String bkdw985211;
    /**
     * syl
     **/
    private String bkdwsyl;
    /**
     * 本校
     **/
    private String bkdwbx;
    /**
     * 其他
     **/
    private String bkdwother;
    /**
     * 软科排名
     **/
    private Integer rkpm;
}
