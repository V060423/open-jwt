package org.apel.authority.factory;


import org.apel.authority.model.JwtUserDetails;
import org.apel.authority.model.SystemUser;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author wangbowen
 * @Description JwtUserDetails创建工厂
 * @Date 2018/6/15 12:18
 */
public  final  class JwtUserDetailsFactory {

    private JwtUserDetailsFactory(){}


    /**
     * 创建JwtUserDetails
     * @param user
     * @return
     */
    public static JwtUserDetails create(SystemUser user){
        return new JwtUserDetails(
                user.getId(),
                user.getUserName(),
                user.getPassword(),
                user.getEmail(),
                mapToGrantedAuthorities(Arrays.asList(user.getRoles())),
                user.getLastPasswordResetDate()
        );


    }

    private static List<GrantedAuthority> mapToGrantedAuthorities(List<String> authorities) {
        return authorities.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

}
