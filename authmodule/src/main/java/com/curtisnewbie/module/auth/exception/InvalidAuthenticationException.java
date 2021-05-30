package com.curtisnewbie.module.auth.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * Exception indicating that the Authentication is currently invalid
 *
 * @author yongjie.zhuang
 */
public class InvalidAuthenticationException extends AuthenticationException {

    public InvalidAuthenticationException(String msg) {
        super(msg);
    }

}
