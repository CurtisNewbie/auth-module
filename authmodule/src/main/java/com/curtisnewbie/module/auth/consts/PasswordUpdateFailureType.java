package com.curtisnewbie.module.auth.consts;

/**
 * Failure type of password update failure
 *
 * @author yongjie.zhuang
 */
public enum PasswordUpdateFailureType {

    /**
     * Old password is incorrect
     */
    OLD_PASSWORD_INCORRECT,

    /**
     * User doesn't exist
     */
    USER_NOT_EXISTS;
}
