package com.curtisnewbie.module.auth.config;

import com.curtisnewbie.common.util.JsonUtils;
import com.curtisnewbie.common.vo.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * <p>
 * Delegate for handling authentication failure
 * </p>
 * <p>
 * It will delegate the handling to {@link AuthenticationFailureHandler} if found
 * </p>
 *
 * @author yongjie.zhuang
 */
@Component
public class AuthenticationFailureHandlerDelegate implements AuthenticationFailureHandler {

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationFailureHandlerDelegate.class);

    @Autowired(required = false)
    private AuthenticationFailureHandlerExtender extender;

    @PostConstruct
    void postConstruct() {
        if (extender != null) {
            logger.info("Detected extender, will invoke {}'s implementation", extender.getClass().getName());
        }
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception)
            throws IOException, ServletException {
        if (extender != null) {
            extender.onAuthenticationFailure(request, response, exception);
        } else {
            defaultHandler(request, response, exception);
        }
    }

    private void defaultHandler(HttpServletRequest request, HttpServletResponse response, AuthenticationException e)
            throws IOException, ServletException {
        String errorMsg = "Incorrect credentials";
        if (e instanceof DisabledException) {
            errorMsg = "User is disabled";
        } else if (e instanceof InsufficientAuthenticationException) {
            errorMsg = "You are not allowed to use this application";
        }
        response.getWriter().write(JsonUtils.writeValueAsString(Result.error(errorMsg)));
    }

}
