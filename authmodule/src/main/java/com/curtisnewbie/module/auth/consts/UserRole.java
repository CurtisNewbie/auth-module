package com.curtisnewbie.module.auth.consts;

/**
 * User's role
 *
 * @author yongjie.zhuang
 */
public enum UserRole {

    ADMIN("admin"),

    GUEST("guest");

    public final String val;

    UserRole(String v) {
        this.val = v;
    }
}
