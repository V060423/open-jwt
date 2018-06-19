package org.apel.authority.controller;

import org.apel.authority.config.AuthenticationProperties;
import org.apel.authority.config.AuthenticationResponse;
import org.apel.authority.model.SystemUser;
import org.apel.authority.request.AuthenticationRequest;
import org.apel.authority.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @author wangbowen
 * @Description 登录
 * @Date 2018/6/15 16:52
 */
@RestController
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private AuthenticationProperties authenticationProperties;

    /**
     * 用户登录
     * @param authenticationRequest
     * @return
     */
    @PostMapping("/auth/gain")
    public ResponseEntity<AuthenticationResponse> createAuthenticationToken( @RequestBody AuthenticationRequest authenticationRequest){

        final String token = authService.login(authenticationRequest.getUserName(), authenticationRequest.getPassword());

        // Return the token
        return ResponseEntity.ok(new AuthenticationResponse(token,authenticationProperties.getRefresh(),authenticationRequest.getUserName()));
    }

    /**
     * 刷新token
     * @param request
     * @return
     */
    @GetMapping("/refresh")
    public ResponseEntity<AuthenticationResponse> refreshAndGetAuthenticationToken(HttpServletRequest request) {
        String token = request.getHeader(authenticationProperties.getTokenHead());
        String refreshedToken = authService.refresh(token);
        if(refreshedToken == null) {
            return ResponseEntity.badRequest().body(null);
        } else {
            return ResponseEntity.ok(new AuthenticationResponse(refreshedToken));
        }
    }

    /**
     * 注册用户
     * @param user
     * @return
     */
    @PostMapping("/auth/register")
    public SystemUser register(@RequestBody SystemUser user){
        return authService.add(user);
    }
}
