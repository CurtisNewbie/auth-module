package com.curtisnewbie.module.auth.processing;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

/**
 * <p>
 * Generic authentication provider
 * </p>
 * <p>
 * This class doesn't care how the authentication is performed, it merely manages the authentication process by invoking
 * the registered {@link PreAuthenticationListener} and {@link PostAuthenticationListener}, and create the authenticated
 * {@link Authentication} object when everything goes right.
 * </p>
 * <p>
 * Replace {@link Authenticator} to change how th authentication is actually performed.
 * </p>
 *
 * @author yongjie.zhuang
 */
@Slf4j
@Component
public class GenericAuthenticationProvider implements AuthenticationProvider {

    @Autowired(required = false)
    private List<PreAuthenticationListener> preAuthListeners;

    @Autowired(required = false)
    private List<PostAuthenticationListener> postAuthListeners;

    @Autowired
    private Authenticator authenticator;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        if (authentication.isAuthenticated())
            return authentication;

        final AuthenticationContext authContext = loadAuthenticationContext(authentication);

        // do operation before authentication
        doPreAuthentication(authContext);

        // do authentication
        AuthenticationResult result = authenticator.authenticate(authentication);
        Objects.requireNonNull(result);

        // store user object into context map, so that the post authentication listener can have access to it
        Objects.requireNonNull(result.getUser());
        authContext.setUser(result.getUser());

        // do operation after authentication
        doPostAuthentication(authContext);

        return result.getAuthentication();
    }

    private AuthenticationContext loadAuthenticationContext(Authentication authentication) {
        AuthenticationContext ctx = new AuthenticationContext();
        ctx.setAuthentication(authentication);
        return ctx;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authenticator.supports(authentication);
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

