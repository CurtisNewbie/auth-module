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
import java.util.Arrays;


/**
 * Configuration class for WebSecurity
 * <p>
 * For custom implementation of AuthenticationSuccessHandler, AuthenticationFailureHandler, and LogoutSuccessHandler,
 * see {@link AuthenticationSuccessHandlerExtender}, {@link AuthenticationFailureHandlerExtender} and {@link
 * LogoutSuccessHandlerExtender}
 * </p>
 * <p>
 * For settings such as permit all ant patterns, login processing url, see {@link SecurityConfigHolder}
 * </p>
 * <p>
 * For more on CORS filter, see {@link CorsConfig}
 * </p>
 *
 * @author yongjie.zhuang
 */
@Configuration
public class SecurityConfigurer extends WebSecurityConfigurerAdapter {
    private static final Logger logger = LoggerFactory.getLogger(SecurityConfigurer.class);

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
    @Autowired
    private SecurityConfigHolder securityConfigHolder;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // setup which requests are permitted and which requests require authentication
        if (securityConfigHolder.specifiedPermittedAntPatterns()) {
            http.authorizeRequests().antMatchers(securityConfigHolder.getPermittedAntPatterns()).permitAll();
            logger.info("Permit all requests for ant patterns: {}",
                    Arrays.toString(securityConfigHolder.getPermittedAntPatterns()));
        }

        // any other requests must be authenticated
        http.authorizeRequests().anyRequest().authenticated();

        // setup the processing url, success handle (delegate) and failure handler (delegate)
        http.formLogin()
                .permitAll()
                .successHandler(authenticationSuccessHandlerDelegate)
                .failureHandler(authenticationFailureHandlerDelegate);

        if (securityConfigHolder.specifiedLoginProcessingUrl()) {
            http.formLogin().loginProcessingUrl(securityConfigHolder.getLoginProcessingUrl());
            logger.info("Setup login processing url: {}", securityConfigHolder.getLoginProcessingUrl());
        }

        // if customLoginPage is present, use the custom one instead of the generated one
        if (securityConfigHolder.specifiedCustomLoginPage()) {
            logger.info("Using custom login page: {}", securityConfigHolder.getCustomLoginPage());
            http.formLogin()
                    .loginPage(securityConfigHolder.getCustomLoginPage());
        }

        // setup logoutUrl and logout success handler (delegate)
        http.logout()
                .permitAll()
                .logoutSuccessHandler(logoutSuccessHandlerDelegate);
        if (securityConfigHolder.specifiedLogoutUrl()) {
            http.logout().logoutUrl(securityConfigHolder.getLogoutUrl());
            logger.info("Setup logout url: {}", securityConfigHolder.getLogoutUrl());
        }

        // todo enable csrf
        http.cors()
                .disable()
                .csrf()
                .disable();

        // todo change to spring's cors configuration
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
