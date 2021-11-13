package com.curtisnewbie.module.auth.config;

import com.curtisnewbie.module.auth.aop.*;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Starter for Auth-Module
 *
 * @author yongjie.zhuang
 */
@Configuration
public class AuthModuleStarter {


    @Configuration
    @ConditionalOnProperty(name = "authmodule.enableOperateLog", matchIfMissing = true, havingValue = "true")
    public static class OperateLogConfiguration {

        @Bean
        public OperateLogAdvice operateLogAdvice() {
            return new OperateLogAdvice();
        }

        @Bean
        public OperateLogRecorder operateLogRecorder() {
            return new MqOperateLogRecorder();
        }
    }

    @Configuration
    @ConditionalOnProperty(name = "authmodule.enableAccessLog", matchIfMissing = true, havingValue = "true")
    public static class AccessLogConfiguration {

        @Bean
        public AccessLogRecorder accessLogRecorder() {
            return new MqAccessLogRecorder();
        }

    }

    @Configuration
    @ConditionalOnProperty(name = "authmodule.enableControllerConsoleLog", matchIfMissing = true, havingValue = "true")
    public static class ControllerConsoleLogAdviceConfiguration {

        @Bean
        public ControllerConsoleLogAdvice controllerConsoleLogAdvice() {
            return new ControllerConsoleLogAdvice();
        }

    }
}
