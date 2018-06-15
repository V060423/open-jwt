package org.apel.authority.filter;

import org.apel.authority.config.AuthenticationExecutor;
import org.apel.authority.config.AuthenticationProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

/**
 * @author wangbowen
 * @Description jwt拦截器
 * @Date 2018/6/15 11:20
 */
@Component
public class JwtAuthenticationTokenFilter  extends OncePerRequestFilter {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private AuthenticationExecutor authorityExecutor;

    @Autowired
    private AuthenticationProperties authorityProperties;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {

        //获取请求的头部信息
        String  authHeader = request.getHeader(authorityProperties.getHeader());

        if(Objects.isNull(authHeader)){

            throw  new  RuntimeException("access without json web token");

        }

        if(Objects.nonNull(authHeader) && authHeader.startsWith(authorityProperties.getTokenHead())){

            final String authToken = authHeader.substring(authorityProperties.getTokenHead().length());

            String username = authorityExecutor.getUserNameFromToken(authToken);

            logger.info("checking authentication " + username);


            //在足够相信token中的数据的话，可以不在查询数据库
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

                UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

                if (authorityExecutor.validateToken(authToken, userDetails)) {

                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());

                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    logger.info("authenticated user " + username + ", setting security context");

                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        }

        chain.doFilter(request, response);

        }
}
