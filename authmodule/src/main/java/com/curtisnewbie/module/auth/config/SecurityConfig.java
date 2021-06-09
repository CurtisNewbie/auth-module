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


/**
 * @author yongjie.zhuang
 */
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private static final Logger logger = LoggerFactory.getLogger(SecurityConfig.class);
    @Autowired
    private ApplicationContext applicationContext;
    @Autowired
    private AuthProvider authProvider;
    @Autowired
    private AuthenticationSuccessHandlerDelegate authenticationSuccessHandlerDelegate;
    @Autowired
    private AuthenticationFailureHandlerDelegate onAuthFailureHandler;
    @Autowired
    private OnLogoutSuccessHandler onLogoutSuccessHandler;
    @Value("${permittedAntPatterns:}") // default to "" empty string
    private String[] permittedAntPatterns = new String[]{};

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        logger.info("Permit all requests for ant patterns: {}", permittedAntPatterns,toString());
        http
            .authorizeRequests()
                .antMatchers(permittedAntPatterns).permitAll()
                .anyRequest().authenticated()
                .and()
            .formLogin()
                .loginProcessingUrl("/login")
                .permitAll()
                .successHandler(authenticationSuccessHandlerDelegate)
                .failureHandler(onAuthFailureHandler)
                .and()
            .logout()
                .permitAll()
                .logoutUrl("/logout")
                .logoutSuccessHandler(onLogoutSuccessHandler)
                .and()
            .cors()
                .disable()
            .csrf()
                .disable();
//            .addFilterBefore(new CorsFilter(), LogoutFilter.class);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        logger.info("Configuring AuthenticationProvider using: {}", authProvider.getClass());
        auth.authenticationProvider(authProvider);
    }

//    static class CorsFilter extends OncePerRequestFilter {
//
//        @Override
//        protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
//
//            response.addHeader("Access-Control-Allow-Origin", "*");
//            response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS");
//            response.setHeader("Access-Control-Allow-Credentials", "true");
//            response.setHeader("Access-Control-Allow-Headers",
//                    "content-type, x-gwt-module-base, x-gwt-permutation, clientid, longpush, set-cookie");
//            filterChain.doFilter(request, response);
//        }
//    }
}
