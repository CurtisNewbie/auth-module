package com.curtisnewbie.module.auth.processing;

import org.springframework.security.core.AuthenticationException;

/**
 * After calling remote service to authenticate
 *
 * @author yongjie.zhuang
 */
public interface PostAuthenticationListener {

    void doPostAuthentication(AuthenticationContext context) throws AuthenticationException;

}
