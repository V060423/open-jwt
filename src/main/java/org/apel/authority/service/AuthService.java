package org.apel.authority.service;

import org.apel.authority.model.SystemUser;

/**
 * @author wangbowen
 * @Description TODO
 * @Date 2018/6/15 15:36
 */
public interface AuthService {

    SystemUser add(SystemUser user);


    String  login(String name,String password);

    String  refresh(String  token);
}
