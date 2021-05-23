package com.curtisnewbie.module.auth.config;

import com.curtisnewbie.module.auth.dao.AccessLogEntity;
import com.curtisnewbie.module.auth.dao.UserEntity;
import com.curtisnewbie.module.auth.services.api.AccessLogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

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
public class AccessLogTracker implements AuthenticationSuccessHandler {

    private static final Logger logger = LoggerFactory.getLogger(AccessLogTracker.class);
    @Autowired
    private AccessLogService accessLogService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
                                        Authentication authentication) throws IOException, ServletException {
        logAccessInfoAsync(httpServletRequest, authentication);
        httpServletResponse.sendRedirect("/index.html");
    }

    private void logAccessInfoAsync(HttpServletRequest request, Authentication auth) {
        AccessLogEntity accessLogEntity = new AccessLogEntity();
        accessLogEntity.setIpAddress(request.getRemoteAddr());
        accessLogEntity.setAccessTime(new Date());
        if (auth.getPrincipal() != null && auth.getPrincipal() instanceof UserEntity) {
            UserEntity userEntity = (UserEntity) auth.getPrincipal();
            accessLogEntity.setUserId(userEntity.getId());
            accessLogEntity.setUsername(userEntity.getUsername());
        }
        CompletableFuture.runAsync(() -> {
            logger.info("Logging sign-in info: {}", accessLogEntity.toString());
            accessLogService.save(accessLogEntity);
        }).handle((r, e) -> {
            if (e != null) {
                logger.error("Unable to save access log", e);
            }
            return r;
        });
    }
}

