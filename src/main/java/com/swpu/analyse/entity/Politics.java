package com.swpu.analyse.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * 政治面貌信息
 *
 * @author cyg
 * @date 2019/5/14 上午11:45
 **/
@Data
@Entity
public class Politics {

    /**
     * 政治面貌代码
     **/
    @Id
    private String zzmmdm;

    /**
     * 政治面貌名称
     **/
    private String zzmmmc;

    /**
     * 政治面貌简称
     **/
    private String zzmmjc;
}
