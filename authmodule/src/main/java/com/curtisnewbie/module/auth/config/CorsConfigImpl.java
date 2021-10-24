package com.curtisnewbie.module.auth.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
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
@Component
public class CorsConfigImpl implements CorsConfig {

    private static final String CORS_IS_FILTER_ENABLED = "authmodule.cors.is-filter-enabled";
    private static final String DEFAULT_IS_FILTER_ENABLED = "false";

    private static final String CORS_ALLOW_ORIGIN_KEY = "authmodule.cors.allow-origin";
    private static final String DEFAULT_ALLOW_ORIGIN = "*";

    private static final String CORS_ALLOW_METHODS_KEY = "authmodule.cors.allow-methods";
    private static final String DEFAULT_ALLOW_METHODS = "POST, GET, OPTIONS";

    private static final String CORS_ALLOW_CREDENTIAL_KEY = "authmodule.cors.allow-credential";
    private static final String DEFAULT_ALLOW_CREDENTIAL = "false";

    private static final String CORS_ALLOW_HEADERS_KEY = "authmodule.cors.allow-headers";
    private static final String DEFAULT_ALLOW_HEADERS = "content-type, x-gwt-module-base, " +
            "x-gwt-permutation, clientid, longpush, set-cookie";

    @Value("${" + CORS_IS_FILTER_ENABLED + ":" + DEFAULT_IS_FILTER_ENABLED + "}") // default to false
    private boolean corsFilterEnabled;

    @Value("${" + CORS_ALLOW_ORIGIN_KEY + ":" + DEFAULT_ALLOW_ORIGIN + "}")
    private String allowOrigin;

    @Value("${" + CORS_ALLOW_METHODS_KEY + ":" + DEFAULT_ALLOW_METHODS + "}")
    private String allowMethods;

    @Value("${" + CORS_ALLOW_CREDENTIAL_KEY + ":" + DEFAULT_ALLOW_CREDENTIAL + "}")
    private Boolean allowCredentials;

    @Value("${" + CORS_ALLOW_HEADERS_KEY + ":" + DEFAULT_ALLOW_HEADERS + "}")
    private String allowHeaders;

    @Override
    public boolean isCustomCorsFilterEnabled() {
        return corsFilterEnabled;
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
