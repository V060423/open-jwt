package org.apel.authority.config;

import java.io.Serializable;

/**
 * @author wangbowen
 * @Description  JWT响应对象
 * @Date 2018/6/15 16:04
 */
public class AuthenticationResponse  implements Serializable {

    private final String token;

    private String refresh;

    private Object subject;

    public AuthenticationResponse(String token) {
        this.token = token;
    }

    public AuthenticationResponse(String token, String refresh, Object subject) {
        this.token = token;
        this.refresh =refresh;
        this.subject =subject;
    }

    public String getToken() {
        return this.token;
    }

    public String getRefresh() {
        return refresh;
    }

    public Object getSubject() {
        return subject;
    }

}
