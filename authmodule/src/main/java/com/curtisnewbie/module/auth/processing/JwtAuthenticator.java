package com.curtisnewbie.module.auth.processing;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.curtisnewbie.module.auth.config.JwtAuthenticationToken;
import com.curtisnewbie.module.jwt.domain.api.JwtDecoder;
import com.curtisnewbie.service.auth.remote.vo.UserVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.Arrays;

/**
 * Authenticator that relies on JWT
 *
 * @author yongjie.zhuang
 */
@Slf4j
@Component
public class JwtAuthenticator implements Authenticator {

    @Autowired
    private JwtDecoder jwtDecoder;

    @Override
    public AuthenticationResult authenticate(Authentication auth) throws AuthenticationException {
        final Object credentials = auth.getCredentials();
        Assert.notNull(credentials, "token == null, unable to retrieve JWT");
        final String encoded = credentials.toString();

        try {
            DecodedJWT decoded = jwtDecoder.decode(encoded);
            UserVo userVo = new UserVo();
            userVo.setId(Integer.parseInt(decoded.getClaim("id").asString()));
            userVo.setUsername(decoded.getClaim("username").asString());
            userVo.setRole(decoded.getClaim("role").asString());

            Assert.notNull(userVo.getId(), "id == null");
            Assert.notNull(userVo.getUsername(), "username == null");
            Assert.notNull(userVo.getRole(), "role == null");

            JwtAuthenticationToken token = new JwtAuthenticationToken(encoded,
                    userVo,
                    Arrays.asList(new SimpleGrantedAuthority(userVo.getRole())));
            return new AuthenticationResult(token, userVo);
        } catch (JWTVerificationException e) {
            throw new BadCredentialsException("JWT invalid", e);
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return JwtAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
