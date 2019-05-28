package com.swpu.analyse.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * 记录学生的省份位置信息
 *
 * @author cyg
 * @date 2019/5/28 下午9:24
 **/
@Entity
@Data
public class Position {

    @Id
    private String id;
    /**
     * 年份
     **/
    private String time;
    /**
     * 省份名称
     **/
    private String name;
    /**
     * 毕业于该省份的人数
     **/
    private Integer num;
}
