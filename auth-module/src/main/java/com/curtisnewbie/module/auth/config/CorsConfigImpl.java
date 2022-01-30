package com.curtisnewbie.module.auth.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Configuration of CORS filter
 *
 * @author yongjie.zhuang
 */
@Slf4j
@Configuration
@ConfigurationProperties("authmodule.cors")
public class CorsConfigImpl implements CorsConfig {

    private static final String DEFAULT_ALLOW_ORIGIN = "*";
    private static final String DEFAULT_ALLOW_METHODS = "POST, GET, OPTIONS";
    private static final String DEFAULT_ALLOW_HEADERS = "content-type, x-gwt-module-base, " +
            "x-gwt-permutation, clientid, longpush, set-cookie";

    /** whether CORS filter is enabled */
    private boolean isFilterEnabled = false;
    /** Access-Control-Allow-Origin */
    private String allowOrigin = DEFAULT_ALLOW_ORIGIN;
    /** Access-Control-Allow-Methods */
    private String allowMethods = DEFAULT_ALLOW_METHODS;
    /** Access-Control-Allow-Credentials */
    private boolean allowCredentials = false;
    /** Access-Control-Allow-Headers */
    private String allowHeaders = DEFAULT_ALLOW_HEADERS;

    @Override
    public boolean isCustomCorsFilterEnabled() {
        return isFilterEnabled;
    }

    @Override
    public CorsFilter getCustomCorsFilter() {
        log.info("Instantiating CORS filter: allowOrigin='{}', allowMethods='{}', allowCredentials='{}', allowHeaders='{}'",
                allowOrigin, allowMethods, allowCredentials, allowHeaders);
        return new CorsFilter(allowOrigin, allowMethods, allowCredentials, allowHeaders);
    }

    static class CorsFilter extends OncePerRequestFilter {
        private final String allowOrigin;
        private final String allowMethods;
        private final Boolean allowCredentials;
        private final String allowHeaders;

        protected CorsFilter(String allowOrigin, String allowMethods, Boolean allowCredentials, String allowHeaders) {
            this.allowOrigin = allowOrigin;
            this.allowMethods = allowMethods;
            this.allowCredentials = allowCredentials;
            this.allowHeaders = allowHeaders;
        }

        @Override
        protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
            response.addHeader("Access-Control-Allow-Origin", allowOrigin);
            response.setHeader("Access-Control-Allow-Methods", allowMethods);
            response.setHeader("Access-Control-Allow-Credentials", allowCredentials.toString());
            response.setHeader("Access-Control-Allow-Headers", allowHeaders);
            filterChain.doFilter(request, response);
        }
    }
}
