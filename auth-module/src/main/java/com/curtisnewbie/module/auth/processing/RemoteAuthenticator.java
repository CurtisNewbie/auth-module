package com.curtisnewbie.module.auth.processing;

import com.curtisnewbie.common.exceptions.UnrecoverableException;
import com.curtisnewbie.common.vo.Result;
import com.curtisnewbie.module.auth.config.ModuleConfig;
import com.curtisnewbie.service.auth.remote.feign.UserServiceFeign;
import com.curtisnewbie.service.auth.remote.vo.LoginVo;
import com.curtisnewbie.service.auth.remote.vo.UserVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * <p>
 * Authenticator based on remote RPC call to auth-service
 * </p>
 *
 * @author yongjie.zhuang
 */
@Slf4j
@Component
public class RemoteAuthenticator implements Authenticator {

    @Autowired
    private UserServiceFeign remoteUserService;

    @Autowired
    private ModuleConfig moduleConfig;

    @Value("${spring.application.name}")
    private String applicationName;

    @Override
    public AuthenticationResult authenticate(Authentication auth) {
        String username = auth.getName();

        try {
            Result<UserVo> userResult;
            final LoginVo loginVo = LoginVo.builder()
                    .username(username)
                    .password(auth.getCredentials().toString())
                    .appName(applicationName)
                    .build();

            // attempt to authenticate, we may also validate whether current user has the right to use current application
            if (moduleConfig.isAppAuthorizationChecked())
                userResult = remoteUserService.loginForApp(loginVo);
            else
                userResult = remoteUserService.login(loginVo);

            // throw exception if notOk
            userResult.assertIsOk();

            log.info("User '{}' authenticated", username);
            return buildSuccessfulAuthentication(userResult.getData(), auth);

        } catch (Exception e) {
            // todo this won't happen, but we keep this before we change auth-service-remote
            throw new UnrecoverableException(e.getMessage(), e);
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }

    /** Build successful Authentication */
    private static AuthenticationResult buildSuccessfulAuthentication(UserVo ue, Authentication au) {
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(ue,
                au.getCredentials(),
                Arrays.asList(new SimpleGrantedAuthority(ue.getRole().getValue())));

        return new AuthenticationResult(token, ue);
    }
}
