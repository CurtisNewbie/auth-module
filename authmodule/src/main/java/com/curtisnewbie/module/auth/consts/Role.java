package com.curtisnewbie.module.auth.consts;

/**
 * @author yongjie.zhuang
 */
public enum Role {

    ADMIN("admin"),

    GUEST("guest");

    public final String val;

    Role(String v) {
        this.val = v;
    }
}
