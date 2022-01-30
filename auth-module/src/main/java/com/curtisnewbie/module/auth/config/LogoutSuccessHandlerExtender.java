package com.curtisnewbie.module.auth.config;

import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;


/**
 * An extended implementation of {@code LogoutSuccessHandler}
 * <p>
 * It is treated as if it's the {@code LogoutSuccessHandler}, except that it's called indirectly by the {@link
 * LogoutSuccessHandlerDelegate}
 * </p>
 *
 * @author yongjie.zhuang
 */
public interface LogoutSuccessHandlerExtender extends LogoutSuccessHandler {

}
