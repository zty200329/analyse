package com.swpu.analyse.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * 日志处理
 * 参数检测
 *
 * @author cyg
 * @date 18-7-26 下午10:06
 **/
@Aspect
@Slf4j
@Component
public class ServiceLogger {

    @Pointcut("execution(public * com.swpu.analyse.service.*.*(..))")
    public void service() {
    }

    @Before("service()")
    public String before(JoinPoint joinPoint) {
        Signature signature = joinPoint.getSignature();
        String method = signature.getName();
        log.info("执行方法 : " + method);
        Object[] args = joinPoint.getArgs();
        for (Object arg : args) {
            if (arg == null) {
                return "执行方法" + method + "时,检测到参数为空";
            }
            if (arg.toString().length() < 200) {
                log.info("传入参数 : " + arg);
            } else {
                log.info("传入参数 : " + arg.toString().substring(0, 200));
            }
        }
        return null;
    }

    @AfterReturning(pointcut = "service()", returning = "ret")
    public void afterReturn(Object ret) {
        if (ret != null) {
            if (ret.toString().length() < 200) {
                log.info("返回结果 : " + ret);
            } else {
                log.info("返回结果 : " + ret.toString().substring(0, 200));
            }
        }
        log.info("-----------------------------------------------------------------------------");
    }
}
