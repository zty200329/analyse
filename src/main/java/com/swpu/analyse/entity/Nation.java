package com.swpu.analyse.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * 民族信息
 *
 * @author cyg
 * @date 2019/5/14 上午11:23
 **/
@Data
@Entity
public class Nation {

    /**
     * 民族代码
     **/
    @Id
    private String mzdm;

    /**
     * 名字名称
     **/
    private String mcmc;

}
