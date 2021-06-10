package com.curtisnewbie.module.auth.config;

import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

/**
 * An extended implementation of {@code AuthenticationSuccessHandler}
 * <p>
 * It is treated as if it's the {@code AuthenticationSuccessHandler}, except that it's called indirectly by the {@link
 * AuthenticationFailureHandlerDelegate}
 * </p>
 *
 * @author yongjie.zhuang
 */
public interface AuthenticationSuccessHandlerExtender extends AuthenticationSuccessHandler {
}
