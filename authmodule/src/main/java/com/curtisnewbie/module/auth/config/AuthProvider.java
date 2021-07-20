package com.curtisnewbie.module.auth.config;

import com.curtisnewbie.service.auth.remote.api.RemoteUserService;
import com.curtisnewbie.service.auth.remote.exception.PasswordIncorrectException;
import com.curtisnewbie.service.auth.remote.exception.UserDisabledException;
import com.curtisnewbie.service.auth.remote.exception.UsernameNotFoundException;
import com.curtisnewbie.service.auth.remote.vo.UserVo;
import org.apache.dubbo.config.annotation.DubboReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * Custom authentication provider
 *
 * @author yongjie.zhuang
 */
@Component
public class AuthProvider implements AuthenticationProvider {

    private static final Logger logger = LoggerFactory.getLogger(AuthProvider.class);

    @DubboReference
    private RemoteUserService remoteUserService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        if (authentication.isAuthenticated())
            return authentication;

        String username = authentication.getName();
        UserVo user;
        try {
            user = remoteUserService.login(username, authentication.getCredentials().toString());
            logger.info("User '{}' authenticated", username);
            return buildSuccessfulAuthentication(user, authentication);
        } catch (UserDisabledException e) {
            logger.info("User '{}' is disabled", username);
            throw new DisabledException("User '" + username + "' is disabled");
        } catch (UsernameNotFoundException e) {
            logger.info("User '{}' not found", username);
            throw new org.springframework.security.core.userdetails.UsernameNotFoundException("User '" + username + "' not found");
        } catch (PasswordIncorrectException e) {
            logger.info("Incorrect password for user '{}'", username);
        }
        throw new BadCredentialsException("Incorrect username or password");
    }

    @Override
    public boolean supports(Class<?> authentication) {
        boolean result = UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
        return result;
    }

    /** Build successful Authentication */
    private static UsernamePasswordAuthenticationToken buildSuccessfulAuthentication(UserVo ue, Authentication au) {
        return new UsernamePasswordAuthenticationToken(ue,
                au.getCredentials(),
                Arrays.asList(new SimpleGrantedAuthority(ue.getRole())));
    }
}
