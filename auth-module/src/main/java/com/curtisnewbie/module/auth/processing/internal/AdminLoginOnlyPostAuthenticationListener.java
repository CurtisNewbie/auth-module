package com.curtisnewbie.module.auth.processing.internal;

import com.curtisnewbie.module.auth.config.ModuleConfig;
import com.curtisnewbie.module.auth.processing.AuthenticationContext;
import com.curtisnewbie.module.auth.processing.PostAuthenticationListener;
import com.curtisnewbie.service.auth.remote.consts.UserRole;
import com.curtisnewbie.service.auth.remote.vo.UserVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

/**
 * PostAuthenticationListener that validates if current application only allows admin to login
 *
 * @author yongjie.zhuang
 */
@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE)
@Component
public class AdminLoginOnlyPostAuthenticationListener implements PostAuthenticationListener {

    @Autowired
    private ModuleConfig moduleConfig;

    @Override
    public void doPostAuthentication(AuthenticationContext context) throws AuthenticationException {
        UserVo user = context.getUser();
        if (user == null)
            return;

        if (moduleConfig.isPermitAdminLoginOnly() && UserRole.ADMIN != user.getRole()) {

            log.info("Only allow admin to login, reject authentication");
            throw new InsufficientAuthenticationException("Only admin can login");
        }

    }
}
