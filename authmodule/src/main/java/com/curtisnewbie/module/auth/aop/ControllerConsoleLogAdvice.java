package com.curtisnewbie.module.auth.aop;

import com.curtisnewbie.module.auth.config.ModuleConfig;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

/**
 * Advice that log the controller's method execution
 * <p>
 * This is very useful for debugging, as you can read the actual arguments received and objects returned.
 * </p>
 * <p>
 * It's by default turned off, but you can turn it on by setting property 'auth-module.enable-controller-console-log'
 * </p>
 *
 * @author yongjie.zhuang
 */
@Slf4j
@Aspect
@Component
@ConditionalOnProperty(name = "authmodule.enable-controller-console-log", matchIfMissing = true)
public class ControllerConsoleLogAdvice {

    @Autowired
    private ModuleConfig moduleConfig;

    @Pointcut("within(@org.springframework.stereotype.Controller *) || within(@org.springframework.web.bind.annotation.RestController *)")
    public void controllerPointcut() {
    }

    @Around("controllerPointcut() && execution(* *(..))")
    public Object methodCall(ProceedingJoinPoint pjp) throws Throwable {
        StopWatch sw = new StopWatch();
        Object result = null;
        try {
            sw.start();
            result = pjp.proceed();
            return result;
        } finally {
            sw.stop();
            log.info("Pointcut '{}',\n args: '{}',\n took '{}' milliseconds,\n result: '{}'\n",
                    pjp.toShortString(),
                    cvtToStr(pjp.getArgs()),
                    sw.getTotalTimeMillis(),
                    respToStr(result));
        }
    }

    private static final String cvtToStr(Object[] args) {
        if (args == null)
            return "[ null ]";

        StringBuilder sb = new StringBuilder("[ ");
        for (Object o : args) {
            append(sb, o == null ? "null" : o.toString());
        }
        sb.append(" ]");
        return sb.toString();
    }

    private static final String respToStr(Object o) {
        if (o == null)
            return "null";

        if (o instanceof ResponseEntity) {
            ResponseEntity respEntity = (ResponseEntity) o;
            StringBuilder sb = new StringBuilder("@ResponseEntity{ ");
            sb.append("statusCode: ").append(respEntity.getStatusCode()).append(", ");
            sb.append("body: ");
            if (respEntity.getBody() == null) {
                sb.append("null");
            } else {
                if (respEntity.getBody() instanceof byte[]) {
                    sb.append(((byte[]) respEntity.getBody()).length + " bytes");
                } else {
                    sb.append(respEntity.getBody().toString());
                }
            }
            sb.append(" }");
            return sb.toString();
        } else {
            return o.toString();
        }
    }

    private static final void append(StringBuilder sb, String text) {
        // 2 is the length of ", "
        if (sb.length() > 2)
            sb.append(", ");
        sb.append("'" + text + "'");
    }
}
