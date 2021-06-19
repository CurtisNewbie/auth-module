package com.curtisnewbie.module.auth.dao;

import java.util.Date;

/**
 * @author yongjie.zhuang
 */
public class AccessLogInfo {

    /** when the user signed in */
    private Date accessTime;

    /** ip address */
    private String ipAddress;

    /** username */
    private String username;

    /** primary key of user */
    private Integer userId;

    public Date getAccessTime() {
        return accessTime;
    }

    public void setAccessTime(Date accessTime) {
        this.accessTime = accessTime;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "AccessLogInfo{" +
                "accessTime=" + accessTime +
                ", ipAddress='" + ipAddress + '\'' +
                ", username='" + username + '\'' +
                ", userId=" + userId +
                '}';
    }
}