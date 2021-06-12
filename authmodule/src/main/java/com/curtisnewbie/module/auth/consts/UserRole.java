package com.curtisnewbie.module.auth.consts;

import java.util.Objects;

/**
 * User's role
 *
 * @author yongjie.zhuang
 */
public enum UserRole {

    /** Administrator */
    ADMIN("admin"),

    /** Normal user */
    USER("user"),

    /** Guest */
    GUEST("guest");

    public final String val;

    UserRole(String v) {
        this.val = v;
    }

    public static UserRole parseUserRole(String userRole) {
        Objects.requireNonNull(userRole);
        for (UserRole u : UserRole.values()) {
            if (Objects.equals(u.val, userRole)) {
                return u;
            }
        }
        return null;
    }
}
