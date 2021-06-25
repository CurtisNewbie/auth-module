package com.curtisnewbie.module.auth.exception;

/**
 * Exceptions related to user
 *
 * @author yongjie.zhuang
 */
public class UserRelatedException extends Exception {

    public UserRelatedException() {

    }

    public UserRelatedException(String msg) {
        super(msg);
    }

    public UserRelatedException(String msg, Throwable t) {
        super(msg, t);
    }

}
