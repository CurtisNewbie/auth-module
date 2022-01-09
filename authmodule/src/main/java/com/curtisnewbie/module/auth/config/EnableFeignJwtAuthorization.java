package com.curtisnewbie.module.auth.config;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * Enable Feign's interceptor to add JWT token in "Authorization" header
 *
 * @author yongjie.zhuang
 */
@Import({FeignJwtHeaderInterceptor.class})
@Target({ElementType.TYPE})
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface EnableFeignJwtAuthorization {
}
