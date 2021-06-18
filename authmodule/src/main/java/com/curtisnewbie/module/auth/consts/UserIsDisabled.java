package com.curtisnewbie.module.auth.consts;

/**
 * User isDisabled enum
 *
 * @author yongjie.zhuang
 */
public enum UserIsDisabled {

    /** normal */
    NORMAL(0),

    /** user is disabled */
    DISABLED(1);

    private final int v;

    UserIsDisabled(int v) {
        this.v = v;
    }

    public int getValue() {
        return v;
    }
}
