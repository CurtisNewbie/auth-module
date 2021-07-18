package com.curtisnewbie.module.auth.config;


import javax.servlet.Filter;

/**
 * Configuration of CORS filter
 *
 * @author yongjie.zhuang
 */
public interface CorsConfig {

    /**
     * Check if the custom CORS filter is enabled in configuration
     */
    boolean isCustomCorsFilterEnabled();

    /**
     * Get the customised CORS filter
     */
    Filter getCustomCorsFilter();
}
