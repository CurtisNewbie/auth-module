package com.curtisnewbie.module.auth.exception;

/**
 * The maximum number of administrators is exceeded
 *
 * @author yongjie.zhuang
 */
public class ExceededMaxAdminCountException extends Exception {

    public ExceededMaxAdminCountException(String msg) {
        super(msg);
    }

    public ExceededMaxAdminCountException() {
    }

}
