package com.curtisnewbie.module.auth.processing;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.curtisnewbie.common.util.EnumUtils;
import com.curtisnewbie.module.auth.config.JwtAuthenticationToken;
import com.curtisnewbie.module.auth.config.ModuleConfig;
import com.curtisnewbie.module.jwt.domain.api.JwtDecoder;
import com.curtisnewbie.module.jwt.vo.DecodeResult;
import com.curtisnewbie.service.auth.remote.consts.UserRole;
import com.curtisnewbie.service.auth.remote.feign.UserAppServiceFeign;
import com.curtisnewbie.service.auth.remote.vo.UserVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import static java.util.Collections.singletonList;

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

    @Autowired
    private ModuleConfig moduleConfig;

    @Value("${spring.application.name}")
    private String applicationName;

    @Override
    public AuthenticationResult authenticate(Authentication auth) throws AuthenticationException {
        final Object credentials = auth.getCredentials();
        Assert.notNull(credentials, "token == null, unable to retrieve JWT");
        final String encoded = credentials.toString();

        final DecodeResult decodeResult = jwtDecoder.decode(encoded);
        if (!decodeResult.isValid())
            throw new BadCredentialsException("JWT invalid" + (decodeResult.isExpired() ? ", token expired" : ""));

        final DecodedJWT decoded = decodeResult.getDecodedJWT();
        UserVo userVo = new UserVo();
        userVo.setId(Integer.parseInt(decoded.getClaim("id").asString()));
        userVo.setUsername(decoded.getClaim("username").asString());
        userVo.setRole(EnumUtils.parse(decoded.getClaim("role").asString(), UserRole.class));

        Assert.notNull(userVo.getId(), "id == null");
        Assert.notNull(userVo.getUsername(), "username == null");
        Assert.notNull(userVo.getRole(), "role == null");

        JwtAuthenticationToken token = new JwtAuthenticationToken(encoded,
                userVo,
                singletonList(new SimpleGrantedAuthority(userVo.getRole().getValue())));
        return new AuthenticationResult(token, userVo);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return JwtAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
