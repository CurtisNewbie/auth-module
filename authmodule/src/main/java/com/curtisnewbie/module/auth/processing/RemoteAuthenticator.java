package com.curtisnewbie.module.auth.processing;

import com.curtisnewbie.common.vo.Result;
import com.curtisnewbie.module.auth.config.ModuleConfig;
import com.curtisnewbie.service.auth.remote.exception.PasswordIncorrectException;
import com.curtisnewbie.service.auth.remote.exception.UserDisabledException;
import com.curtisnewbie.service.auth.remote.exception.UserNotAllowedToUseApplicationException;
import com.curtisnewbie.service.auth.remote.exception.UsernameNotFoundException;
import com.curtisnewbie.service.auth.remote.feign.UserServiceFeign;
import com.curtisnewbie.service.auth.remote.vo.LoginVo;
import com.curtisnewbie.service.auth.remote.vo.UserVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
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

            userResult.assertIsOk();

            log.info("User '{}' authenticated", username);
            return buildSuccessfulAuthentication(userResult.getData(), auth);

        } catch (UserDisabledException e) {
            log.info("User '{}' is disabled", username);
            throw new DisabledException("User '" + username + "' is disabled");
        } catch (UsernameNotFoundException e) {
            log.info("User '{}' not found", username);
            throw new org.springframework.security.core.userdetails.UsernameNotFoundException("User '" + username + "' not found");
        } catch (PasswordIncorrectException e) {
            log.info("Incorrect password for user '{}'", username);
            throw new BadCredentialsException("Incorrect username or password");
        } catch (UserNotAllowedToUseApplicationException e) {
            log.info("User '{}' not allowed to use this application", username);
            throw new InsufficientAuthenticationException("User '" + username + "' not allowed to use this application");
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
                Arrays.asList(new SimpleGrantedAuthority(ue.getRole())));

        return new AuthenticationResult(token, ue);
    }
}
