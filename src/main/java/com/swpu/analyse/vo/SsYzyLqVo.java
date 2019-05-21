package com.swpu.analyse.vo;

import lombok.Data;

/**
 * @author cyg
 * @date 2019/5/20 下午12:18
 **/
@Data
public class SsYzyLqVo {

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
     * 满足一志愿,毕业高校985211
     **/
    private Integer yzy985211;
    /**
     * 满足一志愿,毕业高校双一流
     **/
    private Integer yzySyl;
    /**
     * 满足一志愿,毕业本校应届
     **/
    private Integer yzyBxYj;
    /**
     * 满足一志愿,毕业本校往届
     **/
    private Integer yzyBxWj;
    /**
     * 满足一志愿,其他学校
     **/
    private Integer yzyOther;
    /**
     * 满足调剂,毕业高校985211
     **/
    private Integer tj985211;
    /**
     * 满足调剂,毕业高校双一流
     **/
    private Integer tjSyl;
    /**
     * 满足调剂,毕业本校应届
     **/
    private Integer tjBxYj;
    /**
     * 满足调剂,毕业本校往届
     **/
    private Integer tjBxWj;
    /**
     * 满足调剂,其他学校
     **/
    private Integer tjOther;
    /**
     * 男生
     **/
    private Integer boy;
    /**
     * 总人数
     **/
    private Integer total;
}
