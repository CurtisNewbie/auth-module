package com.curtisnewbie.module.auth.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * <p>
 * Delegate for handling logout success event
 * </p>
 * <p>
 * It will delegate the handling to {@link LogoutSuccessHandlerExtender} if found
 * </p>
 *
 * @author yongjie.zhuang
 */
@Slf4j
@Component
public class LogoutSuccessHandlerDelegate implements LogoutSuccessHandler {

    @Autowired(required = false)
    private LogoutSuccessHandlerExtender extender;

    @PostConstruct
    void postConstruct() {
        if (extender != null) {
            log.info("Detected extender, will invoke {}'s implementation", extender.getClass().getName());
        }
    }


    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {
        // do nothing, it's used to prevent redirect, if extender presented, invoke extender
        if (extender != null) {
            extender.onLogoutSuccess(request, response, authentication);
        }
    }
}
