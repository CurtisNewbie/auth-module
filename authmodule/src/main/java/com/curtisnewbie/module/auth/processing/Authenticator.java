package com.curtisnewbie.module.auth.processing;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

/**
 * <p>
 * Component for authentication
 * </p>
 *
 * @author yongjie.zhuang
 */
public interface Authenticator {

    /**
     * Attempt to authenticate the user
     *
     * @return the successful result of authentication, should never be null
     * @throws AuthenticationException when the authentication fails
     */
    AuthenticationResult authenticate(Authentication auth) throws AuthenticationException;

    /**
     * Whether this authenticator supports the given {@code Authentication} object
     */
    boolean supports(Class<?> authentication);
}
