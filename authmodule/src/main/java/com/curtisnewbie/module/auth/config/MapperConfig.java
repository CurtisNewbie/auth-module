package com.curtisnewbie.module.auth.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author yongjie.zhuang
 */
@Configuration
@MapperScan(basePackages = "com.curtisnewbie.module.auth.dao")
public class MapperConfig {
}
