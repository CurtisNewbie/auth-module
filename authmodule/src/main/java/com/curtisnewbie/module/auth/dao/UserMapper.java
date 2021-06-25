package com.curtisnewbie.module.auth.dao;

import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * @author yongjie.zhuang
 */
public interface UserMapper {

    /**
     * Select * by username
     *
     * @param username username
     */
    UserEntity findByUsername(@Param("username") String username);

    /**
     * Insert user
     */
    void insert(UserEntity userEntity);

    /**
     * Select id by username
     *
     * @param username username
     */
    Integer findIdByUsername(@Param("username") String username);

    /**
     * Count number of administrators
     */
    int countAdmin();

    /**
     * Update password
     *
     * @param hashedPwd hashed password
     * @param id        id
     */
    void updatePwd(@Param("hashedPwd") String hashedPwd, @Param("id") Long id);

    /**
     * Select id, username, role of normal users
     */
    List<UserEntity> findNormalUserInfoList();

    /**
     * Select id, username, role, is_disabled of all users
     */
    List<UserEntity> findAllUserInfoList();

    /**
     * Set user disabled
     *
     * @param id          id
     * @param disabledBy  who disabled this user
     * @param disableTime the time when this user is disabled
     */
    void disableUserById(@Param("id") int id, @Param("disabledBy") String disabledBy, @Param("disableTime") Date disableTime);

    /**
     * Set user enabled
     *
     * @param id         id
     * @param enabledBy  who disabled this user
     * @param enableTime the time when this user is disabled
     */
    void enableUserById(@Param("id") int id, @Param("enabledBy") String enabledBy, @Param("enableTime") Date enableTime);

    /**
     * Select *
     *
     * @param id id
     */
    UserEntity findById(@Param("id") long id);
}
