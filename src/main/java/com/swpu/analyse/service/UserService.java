package com.swpu.analyse.service;

import com.swpu.analyse.vo.ResultVo;

import javax.servlet.http.HttpServletResponse;

/**
 * @author cyg
 * @date 18-11-18 上午11:42
 **/
public interface UserService {

    /**
     * 功能描述: <br>
     * 〈用户登录〉
     *
     * @param username 用户名
     * @param password 密码
     * @param response response
     * @return ResultVo
     * @author cyg
     * @date 18-11-18 下午12:14
     */
    ResultVo login(String username, String password, HttpServletResponse response);


}
