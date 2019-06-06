package com.swpu.analyse.entity;

import lombok.Data;
import lombok.Getter;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * @author cyg
 * @date 19-6-6 上午11:27
 **/
@Entity
@Data
public class User {

    @Id
    private String username;

    private String password;
}
