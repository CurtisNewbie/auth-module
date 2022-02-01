package com.curtisnewbie.module.auth.config;

import com.curtisnewbie.common.util.JsonUtils;
import com.curtisnewbie.common.vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
@Slf4j
@Component
public class AuthenticationFailureHandlerDelegate implements AuthenticationFailureHandler {

    @Autowired(required = false)
    private AuthenticationFailureHandlerExtender extender;

    @PostConstruct
    void postConstruct() {
        if (extender != null) {
            log.info("Detected extender, will invoke {}'s implementation", extender.getClass().getName());
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
        response.getWriter().write(JsonUtils.writeValueAsString(Result.error(e.getMessage())));
    }

}
