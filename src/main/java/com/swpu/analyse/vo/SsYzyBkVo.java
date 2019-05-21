package com.swpu.analyse.vo;

import lombok.Data;

/**
 * @author cyg
 * @date 2019/5/20 下午12:18
 **/
@Data
public class SsYzyBkVo {

    /**
     * 学院代码
     **/
    private String yxsdm;
    /**
     * 学院名称
     **/
    private String yxs;
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
     * 毕业高校985211
     **/
    private Integer by985211;
    /**
     * 毕业高校双一流
     **/
    private Integer bySyl;
    /**
     * 毕业本校应届
     **/
    private Integer bxYj;
    /**
     * 毕业本校往届
     **/
    private Integer bxWj;
    /**
     * 其他学校
     **/
    private Integer other;
    /**
     * 接受推免生
     **/
    private Integer tms;
    /**
     * 男生
     **/
    private Integer boy;
    /**
     * 总人数
     **/
    private Integer total;
}
