package com.curtisnewbie.module.auth.vo;

import com.curtisnewbie.module.auth.consts.UserRole;
import lombok.Data;

/**
 * Vo for registering new user
 *
 * @author yongjie.zhuang
 */
@Data
public class RegisterUserVo {

    /**
     * username
     */
    private String username;

    /**
     * password (in plain text)
     */
    private String password;

    /**
     * role
     */
    private UserRole role;

    /**
     * create by
     */
    private String createBy;
}
