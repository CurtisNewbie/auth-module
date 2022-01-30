package com.curtisnewbie.module.auth.config;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

/**
 * <p>
 * Security-related configuration
 * </p>
 * <p>
 * For custom configuration of HttpSecurity, overrides default value for following properties.
 * <ul>
 *     <li>{@code authmodule.permitted-ant-patterns }: an array of ant patterns permitted by all requests</li>
 *     <li>{@code authmodule.login-processing-url }: custom login processing url</li>
 *     <li>{@code authmodule.custom-login-page }: url of custom login page</li>
 *     <li>{@code authmodule.logout-url }: logout url</li>
 * </ul>
 * </p>
 *
 * @author yongjie.zhuang
 */
@Data
@Slf4j
@Configuration
@ConfigurationProperties(prefix = "authmodule")
public class ModuleConfig {

    private String[] permittedAntPatterns = new String[]{};
    private String loginProcessingUrl = null;
    private String customLoginPage = null;
    private String logoutUrl = null;
    private boolean enableAccessLog = true;
    private boolean permitAdminLoginOnly = false;
    private boolean appAuthorizationChecked = false;

    @Autowired
    private Environment environment;

    /**
     * Check if a custom login page is specified
     */
    public boolean specifiedCustomLoginPage() {
        return customLoginPage != null;
    }

    /**
     * Check if a login processing url is specified
     */
    public boolean specifiedLoginProcessingUrl() {
        return loginProcessingUrl != null;
    }

    /**
     * Check if any permit all ant patterns is specified
     */
    public boolean specifiedPermittedAntPatterns() {
        return permittedAntPatterns.length > 0;
    }

    /**
     * Check if a logout url is specified
     */
    public boolean specifiedLogoutUrl() {
        return logoutUrl != null;
    }
}
