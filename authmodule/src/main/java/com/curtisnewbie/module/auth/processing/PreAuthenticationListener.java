package com.curtisnewbie.module.auth.processing;


import org.springframework.security.core.AuthenticationException;

/**
 * Before calling remote service to authenticate
 *
 * @author yongjie.zhuang
 */
@FunctionalInterface
public interface PreAuthenticationListener {

    void doPreAuthentication(AuthenticationContext context) throws AuthenticationException;

}

