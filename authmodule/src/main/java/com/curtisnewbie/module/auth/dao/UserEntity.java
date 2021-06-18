package com.curtisnewbie.module.auth.dao;

import java.util.Date;

/**
 * User entity
 *
 * @author yongjie.zhuang
 */
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

    /** the date when the user is disabled */
    private Date disableTime;

    /** whether the user is disabled, 0-normal, 1-disabled */
    private Integer isDisabled;

    /** who disabled this user */
    private String disableBy;

    /** who created this user */
    private String createBy;

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
        this.username = username == null ? null : username.trim();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt == null ? null : salt.trim();
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role == null ? null : role.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getDisableTime() {
        return disableTime;
    }

    public void setDisableTime(Date disableTime) {
        this.disableTime = disableTime;
    }

    public Integer getIsDisabled() {
        return isDisabled;
    }

    public void setIsDisabled(Integer isDisabled) {
        this.isDisabled = isDisabled;
    }

    public String getDisableBy() {
        return disableBy;
    }

    public void setDisableBy(String disableBy) {
        this.disableBy = disableBy == null ? null : disableBy.trim();
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy == null ? null : createBy.trim();
    }
}