package com.swpu.analyse.exception;

import com.swpu.analyse.enums.ResultEnum;
import lombok.Data;

/**
 * @author cyg
 * @date 18-9-10 上午11:20
 **/
@Data
public class AnalyseException extends RuntimeException {

    private Integer code;

    public AnalyseException(ResultEnum resultEnum) {
        super(resultEnum.getMessage());
        this.code = resultEnum.getCode();
    }

    public AnalyseException(Integer code, String message) {
        super(message);
        this.code = code;
    }
}
