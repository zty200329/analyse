package com.swpu.analyse.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * 高校信息
 *
 * @author cyg
 * @date 2019/5/14 下午12:06
 **/
@Entity
@Data
public class University {

    @Id
    /**
     * 名称
     **/
    private String name;
    /**
     * 省份id
     **/
    private Integer provinceId;
    private String website;
    private String city;
    /**
     * 软科排名
     **/
    private Integer rkpm;

    /**
     * 院校隶属
     **/
    private String subjection;

    private Integer b211;

    private Integer b985;

    private Integer syl;

    private Integer numberOfYlXk;
}
