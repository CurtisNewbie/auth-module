package com.curtisnewbie.module.auth.config;

import com.curtisnewbie.module.auth.processing.GenericAuthenticationProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.stereotype.Component;
import org.springframework.web.cors.CorsConfigurationSource;

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
 * For settings such as permit all ant patterns, login processing url, see {@link ModuleConfig}
 * </p>
 * <p>
 * For more on CORS filter, see {@link CorsConfig}
 * </p>
 *
 * @author yongjie.zhuang
 */
@Slf4j
@Component
public class SecurityConfigurer extends WebSecurityConfigurerAdapter {

    @Autowired
    private GenericAuthenticationProvider authProvider;
    @Autowired
    private AuthenticationSuccessHandlerDelegate authenticationSuccessHandlerDelegate;
    @Autowired
    private AuthenticationFailureHandlerDelegate authenticationFailureHandlerDelegate;
    @Autowired
    private LogoutSuccessHandlerDelegate logoutSuccessHandlerDelegate;
    @Autowired
    private CorsConfig corsConfig;
    @Autowired
    private ModuleConfig moduleConfig;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // setup which requests are permitted and which requests require authentication
        if (moduleConfig.specifiedPermittedAntPatterns()) {
            http.authorizeRequests().antMatchers(moduleConfig.getPermittedAntPatterns()).permitAll();
            log.info("Permit all requests for ant patterns: {}",
                    Arrays.toString(moduleConfig.getPermittedAntPatterns()));
        }

        // any other requests must be authenticated
        http.authorizeRequests().anyRequest().authenticated();

        // setup the processing url, success handle (delegate) and failure handler (delegate)
        http.formLogin()
                .permitAll()
                .successHandler(authenticationSuccessHandlerDelegate)
                .failureHandler(authenticationFailureHandlerDelegate);

        if (moduleConfig.specifiedLoginProcessingUrl()) {
            http.formLogin().loginProcessingUrl(moduleConfig.getLoginProcessingUrl());
            log.info("Setup login processing url: {}", moduleConfig.getLoginProcessingUrl());
        }

        // if customLoginPage is present, use the custom one instead of the generated one
        if (moduleConfig.specifiedCustomLoginPage()) {
            log.info("Using custom login page: {}", moduleConfig.getCustomLoginPage());
            http.formLogin()
                    .loginPage(moduleConfig.getCustomLoginPage());
        }

        // setup logoutUrl and logout success handler (delegate)
        http.logout()
                .permitAll()
                .clearAuthentication(true)
                .invalidateHttpSession(true)
                .logoutSuccessHandler(logoutSuccessHandlerDelegate);
        if (moduleConfig.specifiedLogoutUrl()) {
            http.logout().logoutUrl(moduleConfig.getLogoutUrl());
            log.info("Setup logout url: {}", moduleConfig.getLogoutUrl());
        }

        // todo enable csrf
        http.cors()
                .disable()
                .csrf()
                .disable();

        // cors configuration
        if (corsConfig.isCustomCorsFilterEnabled()) {
            Filter corsFilter = corsConfig.getCustomCorsFilter();
            log.info("Adding customized CORS filter: {}, you may consider using {} instead", corsFilter.getClass().getName(),
                    CorsConfigurationSource.class.getName());
            http.addFilterBefore(corsFilter, LogoutFilter.class);
        }
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        log.info("Configuring AuthenticationProvider using: {}", authProvider.getClass());
        auth.authenticationProvider(authProvider);
    }
}
