package com.swpu.analyse.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

/**
 * 报名信息
 *
 * @author cyg
 * @date 2019/5/15 下午2:47
 **/
@Data
@Entity
public class Bm {

    @Id
    private String bmId;
    /**
     * 报名号
     **/
    private String bmh;
    /**
     * 时间
     **/
    private String time;
    /**
     * 姓名
     **/
    private String xm;
    /**
     * 考试方式码 21-全国统考 22-推荐免试 23-单独考试
     * 25-MBA联考 26-法律硕士联考 27-强军计划 28-援藏计划
     * 29-农村师资计划
     **/
    private String ksfsm;
    /**
     * 出生日期
     **/
    private String csrq;
    /**
     * 民族代码
     **/
    private String mzm;
    /**
     * 性别  1-男  2-女
     **/
    private Integer xbm;
    /**
     * 婚否 1-未婚, 2-已婚, 3-丧偶, 4-离婚, 5-其他
     **/
    private Integer hfm;
    /**
     * 政治面貌码
     **/
    private String zzmmm;
    /**
     * 毕业单位码
     **/
    private String bydwm;
    /**
     * 毕业单位名称
     **/
    private String bydw;
    /**
     * 毕业专业名称
     **/
    private String byzymc;
    /**
     * 毕业年月
     **/
    private String byny;
    /**
     * 报考单位代码
     **/
    private String bkdwdm;
    /**
     * 报考单位名称
     **/
    private String bkdwmc;
    /**
     * 报考学院代码
     **/
    private String bkyxsm;
    /**
     * 报考学院名称
     **/
    private String bkyxsmc;
    /**
     * 报考专业代码
     **/
    private String bkzydm;
    /**
     * 报考专业名称
     **/
    private String bkzymc;
    /**
     * 报考学习方式 (1全日制，2非全日制）
     **/
    private Integer bkxxfs;
    /**
     * 政治
     **/
    private String zzllmc;
    /**
     * 英语
     **/
    private String wgymc;
    /**
     * 业务课1
     **/
    private String ywk1mc;

    /**
     * 业务课2
     **/
    private String ywk2mc;

}
