package com.swpu.analyse.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * 全国招生规模
 * 我校招生规模和录取情况
 *
 * @author cyg
 * @date 2019/5/16 下午3:22
 **/
@Entity
@Data
public class Scale {

    @Id
    private String id;
    /**
     * 年份
     **/
    private String time;
    /**
     * 博士小计
     **/
    private Integer totalBs;
    /**
     * 硕士小计
     **/
    private Integer totalSs;
    /**
     * 全日制学术学位博士
     **/
    private Integer xsxwQrzBs;
    /**
     * 全日制专业学位博士
     **/
    private Integer zyxwQrzBs;
    /**
     * 非全日制学术学位博士
     **/
    private Integer xsxwFqrzBs;
    /**
     * 非全日制专业学位博士
     **/
    private Integer zyxwFqrzBs;

    /**
     * 全日制学术学位硕士
     **/
    private Integer xsxwQrzSs;
    /**
     * 全日制专业学位硕士
     **/
    private Integer zyxwQrzSs;
    /**
     * 非全日制学术学位硕士
     **/
    private Integer xsxwFqrzSs;
    /**
     * 非全日制专业学位硕士
     **/
    private Integer zyxwFqrzSs;

    /**
     * 数据类型
     * 1-(全国招生规模)
     * 2-(我校招生规模)
     * 3-(我校招生人数)
     **/
    private Integer type;
}
