package com.curtisnewbie.module.auth.config;

import com.curtisnewbie.common.util.JsonUtils;
import com.curtisnewbie.common.vo.Result;
import com.curtisnewbie.module.auth.aop.AccessLogRecorder;
import com.curtisnewbie.module.auth.aop.RecordAccessCmd;
import com.curtisnewbie.service.auth.remote.vo.UserVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * <p>
 * Delegate for handling authentication success
 * </p>
 * <p>
 * It will delegate the handling to {@link AuthenticationSuccessHandlerExtender} if found
 * </p>
 *
 * @author yongjie.zhuang
 */
@Slf4j
@Component
public class AuthenticationSuccessHandlerDelegate implements AuthenticationSuccessHandler {

    @Autowired(required = false)
    private AuthenticationSuccessHandlerExtender extender;

    @Autowired
    private ModuleConfig moduleConfig;

    @Autowired
    private AccessLogRecorder accessLogRecorder;

    @PostConstruct
    void postConstruct() {
        if (extender != null) {
            log.info("Detected extender, will invoke {}'s implementation", extender.getClass().getName());
        }
        if (!moduleConfig.isEnableAccessLog())
            log.info("Access log disabled");
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
                                        Authentication authentication) throws IOException, ServletException {
        if (moduleConfig.isEnableAccessLog())
            logAccessInfo(httpServletRequest, authentication);
        if (extender != null) {
            extender.onAuthenticationSuccess(httpServletRequest, httpServletResponse, authentication);
        } else {
            defaultHandler(httpServletRequest, httpServletResponse, authentication);
        }
    }

    private void logAccessInfo(HttpServletRequest request, Authentication auth) {
        RecordAccessCmd cmd = new RecordAccessCmd();
        cmd.setRemoteAddr(request.getRemoteAddr());

        if (auth.getPrincipal() != null && auth.getPrincipal() instanceof UserVo) {
            UserVo user = (UserVo) auth.getPrincipal();
            cmd.setUser(user);
        }
        accessLogRecorder.recordAccess(cmd);
    }

    void defaultHandler(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {
        response.getWriter().write(JsonUtils.writeValueAsString(Result.ok()));
    }
}

