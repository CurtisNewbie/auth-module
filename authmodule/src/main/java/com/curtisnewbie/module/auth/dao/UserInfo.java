package com.curtisnewbie.module.auth.dao;

/**
 * Basic info of user
 *
 * @author yongjie.zhuang
 */
public class UserInfo {

    private Integer id;

    /**
     * username
     */
    private String username;

    /**
     * role
     */
    private String role;

    /** whether the user is disabled, 0-normal, 1-disabled */
    private Integer isDisabled;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Integer getIsDisabled() {
        return isDisabled;
    }

    public void setIsDisabled(Integer isDisabled) {
        this.isDisabled = isDisabled;
    }
}
