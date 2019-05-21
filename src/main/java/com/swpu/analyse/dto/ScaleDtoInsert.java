package com.swpu.analyse.entity;

import io.swagger.annotations.ApiParam;
import lombok.Data;

import javax.persistence.Entity;

/**
 * 新增招生规模/招生情况
 *
 * @author cyg
 * @date 2019/5/16 下午3:22
 **/
@Data
public class ScaleDtoInsert {

    @ApiParam("博士小计")
    private Integer totalBs;
    @ApiParam("硕士小计")
    private Integer totalSs;
    @ApiParam("全日制学术学位博士")
    private Integer xsxwQrzBs;
    @ApiParam("全日制专业学位博士")
    private Integer zyxwQrzBs;
    @ApiParam("非全日制学术学位博士")
    private Integer xsxwFqrzBs;
    @ApiParam("非全日制专业学位博士")
    private Integer zyxwFqrzBs;
    @ApiParam("全日制学术学位硕士")
    private Integer xsxwQrzSs;
    @ApiParam("全日制专业学位硕士")
    private Integer zyxwQrzSs;
    @ApiParam("非全日制学术学位硕士")
    private Integer xsxwFqrzSs;
    @ApiParam("非全日制专业学位硕士")
    private Integer zyxwFqrzSs;
    @ApiParam("数据类型 1-(全国招生规模),2-(我校招生规模),3-(我校招生人数)")
    private String type;
}
