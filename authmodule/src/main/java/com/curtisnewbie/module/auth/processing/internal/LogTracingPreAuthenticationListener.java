package com.curtisnewbie.module.auth.processing.internal;

import com.curtisnewbie.module.auth.processing.AuthenticationContext;
import com.curtisnewbie.module.auth.processing.PreAuthenticationListener;
import com.curtisnewbie.module.tracing.common.MdcUtil;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * <p>
 * PreAuthenticationListener that store username into MDC for log tracing
 * </p>
 *
 * @author yongjie.zhuang
 */
@Order(value = Ordered.HIGHEST_PRECEDENCE)
@Component
public class LogTracingPreAuthenticationListener implements PreAuthenticationListener {

    @Override
    public void doPreAuthentication(AuthenticationContext context) throws AuthenticationException {
        Authentication auth = context.getAuthentication();
        Objects.requireNonNull(auth);

        // for log tracing
        MdcUtil.setTraceId(auth.getName());
    }
}
