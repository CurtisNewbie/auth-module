package com.curtisnewbie.module.auth.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.logout.LogoutFilter;

import javax.servlet.Filter;


/**
 * Configuration class for WebSecurity
 * <p>
 * Designed with extensibility in mind, for custom implementation of AuthenticationSuccessHandler,
 * AuthenticationFailureHandler, and LogoutSuccessHandler, see {@link AuthenticationSuccessHandlerExtender}, {@link
 * AuthenticationFailureHandlerExtender} and {@link LogoutSuccessHandlerExtender}
 * </p>
 * <p>
 * For custom configuration of HttpSecurity, overrides default value with properties.
 * <ul>
 *     <li>{@link #VALUE_PERMITTED_ANT_PATTERNS} an array of ant patterns permitted by all requests</li>
 *     <li>{@link #VALUE_LOGIN_PROCESSING_URL} custom login processing url</li>
 *     <li>{@link #VALUE_CUSTOM_LOGIN_PAGE} url of custom login page</li>
 *     <li>{@link #VALUE_LOGOUT_URL} logout url</li>
 * </ul>
 * </p>
 * <p>
 * For more on CORS filter, see {@link CorsConfig}
 * </p>
 *
 * @author yongjie.zhuang
 */
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private static final Logger logger = LoggerFactory.getLogger(SecurityConfig.class);
    private static final String EMPTY_STRING = "";
    private static final String VALUE_PERMITTED_ANT_PATTERNS = "authmodule.permitted-ant-patterns";
    private static final String VALUE_LOGIN_PROCESSING_URL = "authmodule.login-processing-url";
    private static final String VALUE_CUSTOM_LOGIN_PAGE = "authmodule.custom-login-page";
    private static final String VALUE_LOGOUT_URL = "authmodule.logout-url";

    @Autowired
    private ApplicationContext applicationContext;
    @Autowired
    private AuthProvider authProvider;
    @Autowired
    private AuthenticationSuccessHandlerDelegate authenticationSuccessHandlerDelegate;
    @Autowired
    private AuthenticationFailureHandlerDelegate authenticationFailureHandlerDelegate;
    @Autowired
    private LogoutSuccessHandlerDelegate logoutSuccessHandlerDelegate;
    @Autowired
    private CorsConfig corsConfig;


    @Value("${" + VALUE_PERMITTED_ANT_PATTERNS + ":" + EMPTY_STRING + "}") // default to "" empty string
    private String[] permittedAntPatterns;
    @Value("${" + VALUE_LOGIN_PROCESSING_URL + ":" + EMPTY_STRING + "}")
    private String loginProcessingUrl;
    @Value("${" + VALUE_CUSTOM_LOGIN_PAGE + ":" + EMPTY_STRING + "}")
    private String customLoginPage;
    @Value("${" + VALUE_LOGOUT_URL + ":" + EMPTY_STRING + "}")
    private String logoutUrl;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // setup which requests are permitted and which requests require authentication
        if (permittedAntPatterns.length > 0) {
            http.authorizeRequests().antMatchers(permittedAntPatterns).permitAll();
            logger.info("Permit all requests for ant patterns: {}", permittedAntPatterns, toString());
        }

        // any other requests must be authenticated
        http.authorizeRequests().anyRequest().authenticated();

        // setup the processing url, success handle (delegate) and failure handler (delegate)
        http.formLogin()
                .permitAll()
                .successHandler(authenticationSuccessHandlerDelegate)
                .failureHandler(authenticationFailureHandlerDelegate);

        if (!loginProcessingUrl.equals(EMPTY_STRING)) {
            http.formLogin().loginProcessingUrl(loginProcessingUrl);
            logger.info("Setup login processing url: {}", loginProcessingUrl);
        }

        // if customLoginPage is present, use the custom one instead of the generated one
        if (!customLoginPage.equals(EMPTY_STRING)) {
            logger.info("Using custom login page: {}", customLoginPage);
            http.formLogin()
                    .loginPage(customLoginPage);
        }

        // setup logoutUrl and logout success handler (delegate)
        http.logout()
                .permitAll()
                .logoutSuccessHandler(logoutSuccessHandlerDelegate);
        if (!logoutUrl.equals(EMPTY_STRING)) {
            http.logout().logoutUrl(logoutUrl);
            logger.info("Setup logout url: {}", logoutUrl);
        }

        http.cors()
                .disable()
                .csrf()
                .disable();

        if (corsConfig.isCustomCorsFilterEnabled()) {
            Filter corsFilter = corsConfig.getCustomCorsFilter();
            logger.info("Adding customized CORS filter: {}", corsFilter.getClass().getName());
            http.addFilterBefore(corsFilter, LogoutFilter.class);
        }
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        logger.info("Configuring AuthenticationProvider using: {}", authProvider.getClass());
        auth.authenticationProvider(authProvider);
    }
}
