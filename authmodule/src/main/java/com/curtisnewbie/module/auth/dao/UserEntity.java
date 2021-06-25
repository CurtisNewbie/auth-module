package com.curtisnewbie.module.auth.dao;

import lombok.Data;

import java.util.Date;

/**
 * User entity
 *
 * @author yongjie.zhuang
 */
@Data
public class UserEntity {
    /** primary key */
    private Integer id;

    /** username (must be unique) */
    private String username;

    /** password in hash */
    private String password;

    /** salt */
    private String salt;

    /** role */
    private String role;

    /** when the user is created */
    private Date createTime;

    /** when the user is updated */
    private Date updateTime;

    /** whether the user is disabled, 0-normal, 1-disabled */
    private Integer isDisabled;

    /** who updated this user */
    private String updateBy;

    /** who created this user */
    private String createBy;
}