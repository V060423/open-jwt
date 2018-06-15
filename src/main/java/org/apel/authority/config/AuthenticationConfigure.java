package org.apel.authority.config;

import io.jsonwebtoken.Jws;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author wangbowen
 * @Description 加载Jwt
 * @Date 2018/6/15 14:33
 */
@Configuration
@ConditionalOnClass(Jws.class)
@EnableConfigurationProperties(AuthenticationProperties.class)
public class AuthenticationConfigure {

    @Bean
    public AuthenticationExecutor authorityExecutor(AuthenticationProperties authorityProperties){

        return new AuthenticationExecutor(authorityProperties.getSecret(),authorityProperties.getActive());
    }

}
