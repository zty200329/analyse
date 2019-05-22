package com.swpu.analyse.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * 录取信息
 *
 * @author cyg
 * @date 2019/5/15 下午7:13
 **/
@Data
@Entity
public class Lq {

    @Id
    private String lqId;
    /**
     * 报名号
     **/
    private String bmh;
    /**
     * 时间
     **/
    private String time;
    private String xm;
    /**
     * 录取学院
     **/
    private String yxsdm;
    /**
     * 录取专业代码
     **/
    private String zydm;
    /**
     * 录取专业名称
     **/
    private String zymc;
    /**
     * 录取学习方式 (1全日制，2非全日制）
     **/
    private Integer xxfsdm;
    /**
     * 报考单位
     **/
    private String bkdwmc;
    /**
     * 报考学院代码
     **/
    private String bkyxsdm;
    /**
     * 报考学院名称
     **/
    private String bkyxsmc;
    /**
     * 报考专业代码
     **/
    private String bkzydm;
    /**
     * 出生日期
     **/
    private String csrq;
    /**
     * 民族代码
     **/
    private String mzdm;
    /**
     * 性别  1-男  2-女
     **/
    private Integer xbdm;
    /**
     * 婚否 1-未婚, 2-已婚, 3-丧偶, 4-离婚, 5-其他
     **/
    private Integer hfdm;
    /**
     * 政治面貌码
     **/
    private String zzmmdm;
    /**
     * 考生来源
     **/
    private String kslymc;
    /**
     * 毕业单位
     **/
    private String bydwmc;
    /**
     * 毕业年月
     **/
    private String byny;
    /**
     * 毕业专业代码
     **/
    private String byzydm;
    /**
     * 毕业专业名称
     **/
    private String byzymc;
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
    /**
     * 政治理论成绩
     **/
    private Double zzll;
    /**
     * 外语成绩成绩
     **/
    private Double wgy;
    /**
     * 业务课1成绩
     **/
    private Double ywk1;
    /**
     * 业务课2成绩
     **/
    private Double ywk2;
    /**
     * 总分
     **/
    private Double zf;
}
