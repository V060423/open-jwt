package org.apel.authority.service;

import org.apel.authority.config.AuthenticationExecutor;
import org.apel.authority.config.AuthenticationProperties;
import org.apel.authority.model.JwtUserDetails;
import org.apel.authority.model.SystemUser;
import org.apel.authority.repository.SystemUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Date;
import java.util.Objects;

/**
 * @author wangbowen
 * @Description TODO
 * @Date 2018/6/15 15:37
 */
@Service
public class AuthServiceImpl implements  AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private AuthenticationExecutor authenticationExecutor;

    @Autowired
    private SystemUserRepository systemUserRepository;

    @Autowired
    private AuthenticationProperties authenticationProperties;



    @Override
    public SystemUser add(SystemUser user) {

        if(Objects.nonNull(user)){

            //check user is exist
            String userName = user.getUserName();

            SystemUser sysUser = systemUserRepository.findByUserName(userName);

            if(Objects.nonNull(sysUser)){

                throw  new RuntimeException(userName+"已经被注册了！");
            }
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

            String password = encoder.encode(user.getPassword());

            user.setPassword(password);

            user.setLastPasswordResetDate(new Date());

            user.setRoles(Arrays.asList("ROLE_USER"));

            return systemUserRepository.save(user);
        }
        return null;
    }

    @Override
    public String login(String name, String password) {

        UsernamePasswordAuthenticationToken upToken = new UsernamePasswordAuthenticationToken(name,password);

        //Perform  security

        Authentication authenticate = authenticationManager.authenticate(upToken);

        //save authenticate
        SecurityContextHolder.getContext().setAuthentication(authenticate);

        UserDetails userDetails = userDetailsService.loadUserByUsername(name);

        String token = authenticationExecutor.generateToken(userDetails);

        return token;
    }

    @Override
    public String refresh(String oldToken) {
        final String token = oldToken.substring(authenticationProperties.getTokenHead().length());

        String username = authenticationExecutor.getUserNameFromToken(token);

        JwtUserDetails user = (JwtUserDetails) userDetailsService.loadUserByUsername(username);

        if (authenticationExecutor.canTokenBeRefreshed(token, user.getLastPasswordResetDate())){

            return authenticationExecutor.refreshToken(token);
        }
        return null;
    }
}
