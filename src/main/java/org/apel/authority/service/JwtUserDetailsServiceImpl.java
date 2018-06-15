package org.apel.authority.service;

import org.apel.authority.factory.JwtUserDetailsFactory;
import org.apel.authority.model.SystemUser;
import org.apel.authority.repository.SystemUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * @author wangbowen
 * @Description 查询用户
 * @Date 2018/6/15 12:20
 */
@Service
public class JwtUserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private SystemUserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        SystemUser user = userRepository.findByUserName(username);

        if (Objects.isNull(user)) {

            throw new UsernameNotFoundException(String.format("No user found with username '%s'.", username));

        } else {

            return JwtUserDetailsFactory.create(user);//创建用户安全模型对象
        }
    }
}

