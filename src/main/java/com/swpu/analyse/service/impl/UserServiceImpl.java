package com.swpu.analyse.service.impl;

import com.swpu.analyse.config.JwtProperties;
import com.swpu.analyse.entity.User;
import com.swpu.analyse.enums.ResultEnum;
import com.swpu.analyse.exception.AnalyseException;
import com.swpu.analyse.mapper.UserMapper;
import com.swpu.analyse.service.UserService;
import com.swpu.analyse.util.JwtTokenUtil;
import com.swpu.analyse.util.ResultVoUtil;
import com.swpu.analyse.vo.ResultVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletResponse;

/**
 * @author cyg
 * @date 18-11-18 上午11:46
 **/
@Slf4j
@Service
@Transactional(rollbackFor = AnalyseException.class)
public class UserServiceImpl implements UserService {

    private final AuthenticationManager authenticationManager;
    private final MyUserDetailsService userDetailsService;
    private final JwtProperties jwtProperties;
    private final JwtTokenUtil jwtTokenUtil;
    @Autowired
    private UserMapper userMapper;

    public UserServiceImpl(AuthenticationManager authenticationManager, MyUserDetailsService userDetailsService, JwtProperties jwtProperties, JwtTokenUtil jwtTokenUtil) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtProperties = jwtProperties;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @Override
    public ResultVo login(String username, String password, HttpServletResponse response) {
        try {
            User user = userMapper.getOne(username);
            if (!username.equals(user.getUsername()) || !password.equals(user.getPassword())) {
                throw new AnalyseException(ResultEnum.LOGIN_ERROR);
            }
        } catch (EntityNotFoundException e) {
            throw new AnalyseException(ResultEnum.WITHOUT_THIS_USER);
        }
        Authentication authenticationToken = new UsernamePasswordAuthenticationToken(
                username, password);
        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        String token = jwtTokenUtil.generateToken(userDetails);
        response.addHeader(jwtProperties.getTokenName(), token);
        return ResultVoUtil.success("登录成功");
    }

    @Override
    public ResultVo update(String username, String password, String newPassword) {
        try {
            User user = userMapper.getOne(username);
            if (!username.equals(user.getUsername()) || !password.equals(user.getPassword())) {
                throw new AnalyseException(ResultEnum.LOGIN_ERROR);
            }
            user.setPassword(newPassword);
            userMapper.save(user);
        } catch (EntityNotFoundException e) {
            throw new AnalyseException(ResultEnum.WITHOUT_THIS_USER);
        }
        return ResultVoUtil.success("密码修改成功");
    }
}
