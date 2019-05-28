package com.swpu.analyse.dto;

import io.swagger.annotations.ApiParam;
import lombok.Data;

/**
 * 修改招生规模/招生情况
 *
 * @author cyg
 * @date 2019/5/16 下午3:22
 **/
@Data
public class ScaleDtoUpadte {

    @ApiParam("id")
    private String id;
    @ApiParam("年份")
    private String time;
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
}
