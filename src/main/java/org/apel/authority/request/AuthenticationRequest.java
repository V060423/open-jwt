package org.apel.authority.request;

import java.io.Serializable;

/**
 * @author wangbowen
 * @Description 请求对象
 * @Date 2018/6/15 16:08
 */
public class AuthenticationRequest  implements Serializable {

    private String  userName;

    private String password;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
