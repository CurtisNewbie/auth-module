package com.curtisnewbie.module.auth.exception;

/**
 * User's password is incorrect
 *
 * @author yongjie.zhuang
 */
public class PasswordIncorrectException extends UserRelatedException {

    public PasswordIncorrectException() {

    }

    public PasswordIncorrectException(String m) {
        super(m);
    }
}
