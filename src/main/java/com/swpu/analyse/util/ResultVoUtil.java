package com.swpu.analyse.util;

import com.swpu.analyse.enums.ResultEnum;
import com.swpu.analyse.vo.ResultVo;

import java.io.Serializable;

/**
 * 封装返回内容
 *
 * @author cyg
 * @date 18-3-2 下午2:26
 **/

public class ResultVoUtil implements Serializable {

    /**
     * 功能描述: <br>
     * 〈成功返回  携带内容〉
     *
     * @param
     * @return
     * @author cyg
     * @date 18-9-18 下午10:21
     */
    public static ResultVo success(Object object) {
        ResultVo resultVo = new ResultVo();
        resultVo.setData(object);
        resultVo.setCode(0);
        resultVo.setMsg("成功");
        return resultVo;
    }

    /**
     * 功能描述: <br>
     * 〈成功返回  不携带内容〉
     *
     * @param
     * @return
     * @author cyg
     * @date 18-9-18 下午10:21
     */
    public static ResultVo success() {
        return success(null);
    }

    /**
     * 功能描述: <br>
     * 〈错误返回 携带code和msg〉
     *
     * @param
     * @return
     * @author cyg
     * @date 18-9-18 下午10:22
     */
    public static ResultVo error(Integer code, String msg) {
        ResultVo resultVo = new ResultVo();
        resultVo.setCode(code);
        resultVo.setMsg(msg);
        return resultVo;
    }

    /**
     * 错误返回 携带code和msg
     **/
    public static ResultVo error(String msg) {
        ResultVo resultVo = new ResultVo();
        resultVo.setCode(-1);
        resultVo.setMsg(msg);
        return resultVo;
    }

    /**
     * 错误返回 携带ResultEnum
     **/
    public static ResultVo error(ResultEnum resultEnum) {
        ResultVo resultVo = new ResultVo();
        resultVo.setCode(resultEnum.getCode());
        resultVo.setMsg(resultEnum.getMessage());
        return resultVo;
    }

    /**
     * 错误返回 携带ResultEnum 和 data
     **/
    public static ResultVo error(ResultEnum resultEnum, Object o) {
        ResultVo resultVo = new ResultVo();
        resultVo.setCode(resultEnum.getCode());
        resultVo.setMsg(resultEnum.getMessage());
        resultVo.setData(o);
        return resultVo;
    }
}