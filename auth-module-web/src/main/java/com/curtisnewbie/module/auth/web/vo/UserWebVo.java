package com.curtisnewbie.module.auth.web.vo;

import com.curtisnewbie.service.auth.remote.consts.UserRole;
import lombok.Data;

/**
 * @author yongjie.zhuang
 */
@Data
public class UserWebVo {

    /** id */
    private Integer id;

    /**
     * username
     */
    private String username;

    /**
     * role
     */
    private UserRole role;
}
