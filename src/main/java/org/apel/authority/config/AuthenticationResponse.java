package org.apel.authority.config;

import lombok.Data;

import java.io.Serializable;

/**
 * @author wangbowen
 * @Description  JWT响应对象
 * @Date 2018/6/15 16:04
 */
@Data
public class AuthenticationResponse  implements Serializable {

    private  String token;

    private Long refresh;

    private Object subject;

    public AuthenticationResponse(String token) {
        this.token = token;
    }

    public AuthenticationResponse(String token, Long refresh, Object subject) {
        this.token = token;
        this.refresh =refresh;
        this.subject =subject;
    }
}
