package com.swpu.analyse.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * 省份信息
 *
 * @author cyg
 * @date 18-11-9 上午10:57
 **/
@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Province {

    /**
     * 省份id
     **/
    @Id
    private Integer id;
    /**
     * 省份名称
     **/
    private String name;

}
