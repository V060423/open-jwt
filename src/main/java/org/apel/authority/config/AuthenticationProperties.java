package org.apel.authority.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @author wangbowen
 * @Description JWT配置文件
 * @Date 2018/6/15 10:30
 */
@Data
@ConfigurationProperties("auth")
public class AuthenticationProperties {


    /**
     * 存活时间
     */
    @NotNull
    private Long  active;

    /**
     * 刷新时间
     */
    @NotNull
    private Long refresh;

    /**
     * 加密方式
     */
    @NotEmpty
    private String secret;

    /**
     * 头部信息
     */
    @NotEmpty
    private String header;

    /**
     * token头部信息
     */
    @NotEmpty
    private String tokenHead;

}
