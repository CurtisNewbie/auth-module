package com.curtisnewbie.module.auth.config;

import org.springframework.security.web.authentication.AuthenticationFailureHandler;

/**
 * An extended implementation of {@code AuthenticationFailureHandler}
 * <p>
 * It is treated as if it's the {@code AuthenticationFailureHandler}, except that it's called indirectly by the {@link
 * AuthenticationFailureHandlerDelegate}
 * </p>
 *
 * @author yongjie.zhuang
 */
public interface AuthenticationFailureHandlerExtender extends AuthenticationFailureHandler {
}
