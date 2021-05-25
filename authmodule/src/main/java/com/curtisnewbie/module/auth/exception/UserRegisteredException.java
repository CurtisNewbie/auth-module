package com.curtisnewbie.module.auth.exception;

/**
 * User with the same name is already registered
 *
 * @author yongjie.zhuang
 */
public class UserRegisteredException extends Exception {

    public UserRegisteredException() {

    }

    public UserRegisteredException(String m) {
        super(m);
    }
}
