package com.swpu.analyse.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 配置文件参数读取
 * @author cyg
 * @date 18-6-10 下午10:04
 **/
@Data
@Component
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {

    private String secret;

    private String expiration;

    private String tokenStart;

    private String tokenName;

    private String Not_interception_path;
}
