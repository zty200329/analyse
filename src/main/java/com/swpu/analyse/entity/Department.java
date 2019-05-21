package com.swpu.analyse.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * 学院信息
 *
 * @author cyg
 * @date 19-5-17 下午8:49
 **/
@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Department {

    /**
     * 学院id
     **/
    @Id
    private String id;

    /**
     * 学院名称
     **/
    private String name;
}
