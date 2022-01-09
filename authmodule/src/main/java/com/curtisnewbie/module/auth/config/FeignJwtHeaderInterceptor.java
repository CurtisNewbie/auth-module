package com.curtisnewbie.module.auth.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

/**
 * @author yongjie.zhuang
 */
public class FeignJwtHeaderInterceptor {

    @Value("${authmodule.service.token}")
    private String token;

    @Bean
    public RequestInterceptor oauth2FeignRequestInterceptor() {
        return new TokenRequestInterceptor(token);
    }

    // todo make the jwt token temporary
    public static class TokenRequestInterceptor implements RequestInterceptor {

        private final String token;

        public TokenRequestInterceptor(String token) {
            this.token = token;
        }

        @Override
        public void apply(RequestTemplate requestTemplate) {
            requestTemplate.header("Authorization", token);

        }
    }

}
