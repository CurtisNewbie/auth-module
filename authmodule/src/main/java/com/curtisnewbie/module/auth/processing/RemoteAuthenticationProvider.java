package com.curtisnewbie.module.auth.processing;

import com.curtisnewbie.service.auth.remote.api.RemoteUserService;
import com.curtisnewbie.service.auth.remote.exception.PasswordIncorrectException;
import com.curtisnewbie.service.auth.remote.exception.UserDisabledException;
import com.curtisnewbie.service.auth.remote.exception.UsernameNotFoundException;
import com.curtisnewbie.service.auth.remote.vo.UserVo;
import org.apache.dubbo.config.annotation.DubboReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * Custom authentication provider
 *
 * @author yongjie.zhuang
 */
@Component
public class RemoteAuthenticationProvider implements AuthenticationProvider {

    public static final String AUTH_CONTEXT_AUTHENTICATION_OBJECT = "authentication-object";
    public static final String AUTH_CONTEXT_USER_OBJECT = "user-object";

    private static final Logger logger = LoggerFactory.getLogger(RemoteAuthenticationProvider.class);

    @DubboReference(lazy = true)
    private RemoteUserService remoteUserService;

    @Autowired(required = false)
    private List<PreAuthenticationListener> preAuthListeners;

    @Autowired(required = false)
    private List<PostAuthenticationListener> postAuthListeners;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        if (authentication.isAuthenticated())
            return authentication;

        final AuthenticationContext authContext = loadAuthenticationContext(authentication);

        String username = authentication.getName();
        UserVo user;
        try {
            // do some operation before authentication
            doPreAuthentication(authContext);

            // attempt to authenticate
            user = remoteUserService.login(username, authentication.getCredentials().toString());
            Objects.requireNonNull(user);
            logger.info("User '{}' authenticated", username);

            // store user object into context map, so that the post authentication listener can have access to it
            authContext.getContextMap().put(AUTH_CONTEXT_USER_OBJECT, user);

            // do some operation after authentication
            doPostAuthentication(authContext);

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

    private AuthenticationContext loadAuthenticationContext(Authentication authentication) {
        AuthenticationContext ctx = new AuthenticationContext();
        ctx.getContextMap().put(AUTH_CONTEXT_AUTHENTICATION_OBJECT, authentication);
        return ctx;
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

    private void doPreAuthentication(AuthenticationContext ctx) {
        if (preAuthListeners != null)
            preAuthListeners.forEach(p -> p.doPreAuthentication(ctx));
    }

    private void doPostAuthentication(AuthenticationContext ctx) {
        if (postAuthListeners != null)
            postAuthListeners.forEach(p -> p.doPostAuthentication(ctx));
    }
}

