package com.swpu.analyse.advice;

import com.swpu.analyse.enums.ResultEnum;
import com.swpu.analyse.exception.AnalyseException;
import com.swpu.analyse.util.ResultVoUtil;
import com.swpu.analyse.vo.ResultVo;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 全局捕获异常类
 *
 * @author cyg
 * @date 18-9-3 下午5:06
 **/
@ControllerAdvice
public class AdviceAll {

    /**
     * 自定义异常
     **/
    @ExceptionHandler(value = {AnalyseException.class})
    @ResponseBody
    public ResultVo exceptionHandle(AnalyseException ex) {
        ex.printStackTrace();
        return ResultVoUtil.error(ex.getCode(), ex.getMessage());
    }

    /**
     * 参数异常
     **/
    @ExceptionHandler(value = {NullPointerException.class})
    @ResponseBody
    public ResultVo exceptionHandle1(NullPointerException ex) {
        ex.printStackTrace();
        return ResultVoUtil.error(ResultEnum.PARAMETERS_ARE_EMPTY);
    }
}
