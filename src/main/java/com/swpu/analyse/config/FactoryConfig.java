package com.swpu.analyse.config;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;

/**
 * @author cyg
 * @date 18-8-26 下午2:49
 **/
@Configuration
@EnableAutoConfiguration
public class FactoryConfig {

    @Bean
    public MethodValidationPostProcessor methodValidationPostProcessor() {

        return new MethodValidationPostProcessor();
    }
}