package com.swpu.analyse.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * 专业
 *
 * @author cyg
 * @date 19-5-17 下午8:49
 **/
@Data
@Entity
public class Major {

    /**
     * 专业id
     **/
    @Id
    private String id;

    /**
     * 专业名称
     **/
    private String name;
    /**
     * 接受推免生数量
     **/
    private Integer tms;
    /**
     * 学院id
     **/
    private String departmentId;
}
