package com.swpu.analyse.security;


import com.swpu.analyse.config.JwtProperties;
import com.swpu.analyse.enums.ResultEnum;
import com.swpu.analyse.exception.AnalyseException;
import com.swpu.analyse.util.JwtTokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * JwtFilter
 *
 * @author cyg
 * @date 18-9-2 下午4:52
 **/
@Slf4j
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    @Resource
    private UserDetailsService userDetailsService;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private JwtProperties jwtProperties;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {

        String options = "OPTIONS";
        if (options.equals(request.getMethod())) {
            //放行OPTIONS请求
            chain.doFilter(request, response);
            return;
        }
        String url = request.getRequestURI();
        log.info("请求路径:{}", url);
        int flag = 0;
        /*得到不拦截的路径,并分为一个数组**/
        String[] paths = jwtProperties.getNot_interception_path().split(",");
        for (String path : paths) {
            if (url.equals(path)) {
                flag = 1;
            }
            if ((url.contains(path.substring(0, path.length() - 2)) && path.contains("*"))) {
                flag = 1;
            }
        }
        if (flag != 1) {
            String authToken = request.getHeader(jwtProperties.getTokenName());
            if (authToken == null) {
                log.info("请求头中无token信息");
                throw new AnalyseException(ResultEnum.TOKEN_NON_ERROR);
            }
            try {
                authToken = authToken.substring(7);
            } catch (Exception e) {
                throw new AnalyseException(ResultEnum.TOKEN_ERROR);
            }
            String username = jwtTokenUtil.getUsernameFromToken(authToken);
            if (username == null) {
                throw new AnalyseException(ResultEnum.TOKEN_ERROR);
            }
            /*当token中的username不为空是进行验证token是否是有效的token**/
            try {
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
                /*判断token是否有效**/
                if (jwtTokenUtil.validateToken(authToken, userDetails)) {
                    UsernamePasswordAuthenticationToken authentication = new
                            UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
                log.info("认证信息 : {}", username);
            } catch (Exception e) {
                throw new AnalyseException(ResultEnum.TOKEN_ERROR);
            }
        }
        chain.doFilter(request, response);
    }
}