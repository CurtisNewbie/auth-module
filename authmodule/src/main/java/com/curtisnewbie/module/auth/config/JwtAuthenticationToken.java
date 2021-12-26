package com.curtisnewbie.module.auth.config;

import com.curtisnewbie.service.auth.remote.vo.UserVo;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * JWT-based authentication token
 * <p>
 * Principal is the {@link UserVo}, if it's not authenticated, it will be null
 * </p>
 * <p>
 * Credential is the encoded JWT token
 * </p>
 *
 * @author yongjie.zhuang
 */
public class JwtAuthenticationToken extends AbstractAuthenticationToken {

    private UserVo principal;
    private String jwtToken;

    public JwtAuthenticationToken(String jwtToken) {
        super(null);
        this.jwtToken = jwtToken;
        setAuthenticated(false);
    }

    public JwtAuthenticationToken(String jwtToken, UserVo principal, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.jwtToken = jwtToken;
        this.principal = principal;
        setAuthenticated(true);
    }


    @Override
    public Object getCredentials() {
        return jwtToken;
    }

    @Override
    public Object getPrincipal() {
        return principal;
    }
}
