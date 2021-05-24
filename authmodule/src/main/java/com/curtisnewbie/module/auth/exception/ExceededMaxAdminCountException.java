package com.curtisnewbie.module.auth.exception;

/**
 * @author yongjie.zhuang
 */
public class ExceededMaxAdminCountException extends Exception {

    public ExceededMaxAdminCountException(String msg) {
        super(msg);
    }

    public ExceededMaxAdminCountException() {
    }

}
