package com.curtisnewbie.module.auth.processing;

import com.curtisnewbie.module.auth.config.ModuleConfig;
import com.curtisnewbie.service.auth.remote.api.RemoteUserService;
import com.curtisnewbie.service.auth.remote.exception.PasswordIncorrectException;
import com.curtisnewbie.service.auth.remote.exception.UserDisabledException;
import com.curtisnewbie.service.auth.remote.exception.UserNotAllowedToUseApplicationException;
import com.curtisnewbie.service.auth.remote.exception.UsernameNotFoundException;
import com.curtisnewbie.service.auth.remote.vo.UserVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Objects;

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

    @DubboReference
    private RemoteUserService remoteUserService;

    @Autowired
    private ModuleConfig moduleConfig;

    @Override
    public AuthenticationResult authenticate(Authentication auth) {
        String username = auth.getName();

        try {
            UserVo user;

            // attempt to authenticate, if there is an application name, we validate it
            final String applicationName = moduleConfig.getApplicationName();
            if (applicationName != null)
                user = remoteUserService.login(username, auth.getCredentials().toString(), applicationName);
            else
                user = remoteUserService.login(username, auth.getCredentials().toString());
            Objects.requireNonNull(user);

            log.info("User '{}' authenticated", username);
            return buildSuccessfulAuthentication(user, auth);

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
