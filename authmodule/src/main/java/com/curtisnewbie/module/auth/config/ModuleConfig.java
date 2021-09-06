package com.curtisnewbie.module.auth.config;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

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
@Configuration
@Slf4j
public class ModuleConfig {

    private static final String EMPTY_STRING = "";
    public static final String PROP_NAME_PERMITTED_ANT_PATTERNS = "authmodule.permitted-ant-patterns";
    public static final String PROP_NAME_LOGIN_PROCESSING_URL = "authmodule.login-processing-url";
    public static final String PROP_NAME_CUSTOM_LOGIN_PAGE = "authmodule.custom-login-page";
    public static final String PROP_NAME_LOGOUT_URL = "authmodule.logout-url";
    public static final String PROP_NAME_ENABLE_OPERATE_LOG = "authmodule.enable-operate-log";
    public static final String PROP_NAME_ENABLE_ACCESS_LOG = "authmodule.enable-access-log";
    public static final String PROP_NAME_ONLY_ADMIN_LOGIN = "authmodule.permit-admin-login-only";
    public static final String PROP_NAME_ENABLE_CONTROLLER_CONSOLE_LOG = "authmodule.enable-controller-console-log";

    @Value("${" + PROP_NAME_ENABLE_CONTROLLER_CONSOLE_LOG + ":false}")
    private boolean controllerConsoleLogEnabled;
    @Value("${" + PROP_NAME_PERMITTED_ANT_PATTERNS + ":" + EMPTY_STRING + "}") // default to "" empty string
    private String[] permittedAntPatterns;
    @Value("${" + PROP_NAME_LOGIN_PROCESSING_URL + ":" + EMPTY_STRING + "}")
    private String loginProcessingUrl;
    @Value("${" + PROP_NAME_CUSTOM_LOGIN_PAGE + ":" + EMPTY_STRING + "}")
    private String customLoginPage;
    @Value("${" + PROP_NAME_LOGOUT_URL + ":" + EMPTY_STRING + "}")
    private String logoutUrl;
    @Value("${" + PROP_NAME_ENABLE_OPERATE_LOG + ":true}")
    private boolean operateLogEnabled;
    @Value("${" + PROP_NAME_ENABLE_ACCESS_LOG + ":true}")
    private boolean accessLoginEnabled;
    @Value("${" + PROP_NAME_ONLY_ADMIN_LOGIN + ": false}")
    private boolean adminLoginOnly;

    /**
     * Check if a custom login page is specified
     */
    public boolean specifiedCustomLoginPage() {
        return !customLoginPage.equals(EMPTY_STRING);
    }

    /**
     * Check if a login processing url is specified
     */
    public boolean specifiedLoginProcessingUrl() {
        return !loginProcessingUrl.equals(EMPTY_STRING);
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
        return !logoutUrl.equals(EMPTY_STRING);
    }
}