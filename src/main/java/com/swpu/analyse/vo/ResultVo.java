package com.swpu.analyse.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author cyg
 * @date 18-6-2 下午2:55
 **/
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ResultVo<T> implements Serializable {

    /**
     * 错误码.
     */
    private Integer code;

    /**
     * 提示信息.
     */
    private String msg;

    /**
     * 具体内容.
     */
    private T data;
}