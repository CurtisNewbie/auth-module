package com.curtisnewbie.module.auth.config;

import com.curtisnewbie.service.auth.remote.api.RemoteAccessLogService;
import com.curtisnewbie.service.auth.remote.vo.AccessLogVo;
import com.curtisnewbie.service.auth.remote.vo.UserVo;
import org.apache.dubbo.config.annotation.DubboReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.concurrent.CompletableFuture;

/**
 * @author yongjie.zhuang
 */
@Component
public class AuthenticationSuccessHandlerDelegate implements AuthenticationSuccessHandler {

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationSuccessHandlerDelegate.class);
    @DubboReference
    private RemoteAccessLogService accessLogService;
    @Autowired(required = false)
    private AuthenticationSuccessHandlerExtender extender;

    @PostConstruct
    void postConstruct() {
        if (extender != null) {
            logger.info("Detected extender, will invoke {}'s implementation", extender.getClass().getName());
        }
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
                                        Authentication authentication) throws IOException, ServletException {
        logAccessInfoAsync(httpServletRequest, authentication);
        if (extender != null) {
            extender.onAuthenticationSuccess(httpServletRequest, httpServletResponse, authentication);
        }
    }

    private void logAccessInfoAsync(HttpServletRequest request, Authentication auth) {
        AccessLogVo accessLog = new AccessLogVo();
        accessLog.setIpAddress(request.getRemoteAddr());
        accessLog.setAccessTime(new Date());
        if (auth.getPrincipal() != null && auth.getPrincipal() instanceof UserVo) {
            UserVo user = (UserVo) auth.getPrincipal();
            accessLog.setUserId(user.getId());
            accessLog.setUsername(user.getUsername());
        }
        CompletableFuture.runAsync(() -> {
            logger.info("Logging sign-in info, ip: {}, username: {}, userId: {}",
                    accessLog.getId(),
                    accessLog.getUsername(),
                    accessLog.getUserId());
            accessLogService.save(accessLog);
        }).handle((r, e) -> {
            if (e != null) {
                logger.error("Unable to save access log", e);
            }
            return r;
        });
    }
}

