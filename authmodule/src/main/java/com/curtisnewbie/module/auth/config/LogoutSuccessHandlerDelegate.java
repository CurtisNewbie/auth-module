package com.curtisnewbie.module.auth.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
 * @author yongjie.zhuang
 */
@Component
public class LogoutSuccessHandlerDelegate implements LogoutSuccessHandler {
    private static final Logger logger = LoggerFactory.getLogger(LogoutSuccessHandlerDelegate.class);
    @Autowired(required = false)
    private LogoutSuccessHandlerExtender extender;

    @PostConstruct
    void postConstruct() {
        if (extender != null) {
            logger.info("Detected extender, will invoke {}'s implementation", extender.getClass().getName());
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
