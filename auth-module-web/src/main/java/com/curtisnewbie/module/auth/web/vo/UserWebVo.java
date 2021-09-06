package com.curtisnewbie.module.auth.web.vo;

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
    private String role;
}
