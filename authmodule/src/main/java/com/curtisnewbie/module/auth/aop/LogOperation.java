package com.curtisnewbie.module.auth.aop;

import java.lang.annotation.*;

/**
 * Annotation indicating that a method is a logged operation
 * <p>
 * This annotation is usually used for the web endpoints
 * </p>
 * Usage example:
 * <pre>
 *    {@code
 *
 *
 *    }
 * </pre>
 *
 * @author yongjie.zhuang
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value = ElementType.METHOD)
@Documented
public @interface LogOperation {

    /**
     * Operation's name
     * <p>
     * Default to ""
     * </p>
     */
    String name() default "";

    /**
     * Operation's description
     * <p>
     * Default to ""
     * </p>
     */
    String description() default "";

    /**
     * Is the operation log enabled
     * <p>
     * Default to true, set to false to disable it
     * </p>
     */
    boolean enabled() default true;

}
