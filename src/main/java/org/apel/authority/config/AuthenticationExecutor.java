package org.apel.authority.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apel.authority.constant.AuthoriyConstant;
import org.apel.authority.model.JwtUserDetails;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author wangbowen
 * @Description jwt工具类，主要提供生成token、解析token的方法
 * @Date 2018/6/15 12:19
 */
public class AuthenticationExecutor{

    /**
     * 加密方式
     */
    private String secret;
    /**
     * 过期时间
     */
    private Long expiration;



    /**
     * 构造函数
     * @param secret
     */
    public AuthenticationExecutor(String secret,Long expiration){
        this.secret = secret;
        this.expiration = expiration;
    }

    /**
     * 生成token
     * @param claims
     * @return
     */
    public String generateToken(Map<String,Object> claims){
        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(generateExpirationDate())
                .signWith(SignatureAlgorithm.HS512,Base64.getEncoder().encodeToString(secret.getBytes()))
                .compact();
    }

    /**
     *根据userDetails创建token
     * @param userDetails
     * @return
     */
    public String generateToken(UserDetails userDetails){
             Map<String, Object> claims = new HashMap<>();
             claims.put(AuthoriyConstant.CLAIM_KEY_USERNAME, userDetails.getUsername());
             claims.put(AuthoriyConstant.CLAIM_KEY_CREATED, new Date());
         return  generateToken(claims);
    }
    /**
     * 解析token
     * @param token
     * @return
     */
    public Jws<Claims> getClaimsFromToken(String  token){
        return Jwts.parser()
                .setSigningKey(Base64.getEncoder().encodeToString(secret.getBytes()))
                .parseClaimsJws(token);
    }

    /**
     * 根据token获取用户名
     * @param token
     * @return
     */
    public String getUserNameFromToken(String token) {
        String username;
        try {
            Jws<Claims> jws = getClaimsFromToken(token);
            username = jws.getBody().getSubject();
        } catch (Exception e) {
            username = null;
        }
        return username;
    }

    /**
     * 根据token获取创建时间
     * @param token
     * @return
     */
    public Date getCreatedDateFromToken(String token) {
        Date created;
        try {
             Claims claims = getClaimsFromToken(token).getBody();
            created = new Date((Long) claims.get(AuthoriyConstant.CLAIM_KEY_CREATED));
        } catch (Exception e) {
            created = null;
        }
        return created;
    }

    /**
     * 根据token获取过期时间
     * @param token
     * @return
     */
    public Date getExpirationDateFromToken(String token) {
        Date expiration;
        try {
             Claims claims = getClaimsFromToken(token).getBody();
            expiration = claims.getExpiration();
        } catch (Exception e) {
            expiration = null;
        }
        return expiration;
    }

    /**
     * 生成过期时间
     * @return
     */
    private Date generateExpirationDate() {

        return new Date(System.currentTimeMillis() + expiration * 1000);
    }

    /**
     * 判断token是否过期
     * @param token
     * @return
     */
    private Boolean isTokenExpired(String token) {

        final Date expiration = getExpirationDateFromToken(token);

        return expiration.before(new Date());
    }

    /**
     * 最后一次密码重置是否实在创建之前后
     * @param created
     * @param lastPasswordReset
     * @return
     */
    private Boolean isCreatedBeforeLastPasswordReset(Date created, Date lastPasswordReset) {

        return (lastPasswordReset != null && created.before(lastPasswordReset));
    }

    /**
     * 判断token是否可以被刷新
     * @param token
     * @param lastPasswordReset
     * @return
     */
    public Boolean canTokenBeRefreshed(String token, Date lastPasswordReset) {
        final Date created = getCreatedDateFromToken(token);
        return !isCreatedBeforeLastPasswordReset(created, lastPasswordReset)
                && !isTokenExpired(token);
    }

    /**
     * 刷新token
     * @param token
     * @return
     */
    public String refreshToken(String token) {
        String refreshedToken;
        try {
             Claims claims = getClaimsFromToken(token).getBody();
             claims.put(AuthoriyConstant.CLAIM_KEY_CREATED, new Date());
            refreshedToken = generateToken(claims);
        } catch (Exception e) {
            refreshedToken = null;
        }
        return refreshedToken;
    }

    /**
     * 验证token是否合法
     * @param token
     * @param userDetails
     * @return
     */
    public Boolean validateToken(String token, UserDetails userDetails) {
        JwtUserDetails user = (JwtUserDetails) userDetails;
        final String username = getUserNameFromToken(token);
        final Date created = getCreatedDateFromToken(token);
        return (
                username.equals(user.getUsername())
                        && !isTokenExpired(token)
                        && !isCreatedBeforeLastPasswordReset(created, user.getLastPasswordResetDate()));
    }

}
