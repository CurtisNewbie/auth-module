package com.curtisnewbie.module.auth.exception;

/**
 * User is not found
 *
 * @author yongjie.zhuang
 */
public class UserNotFoundException extends UserRelatedException {

    public UserNotFoundException() {

    }

    public UserNotFoundException(String m) {
        super(m);
    }
}
