package com.curtisnewbie.module.auth.dao;

import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * @author yongjie.zhuang
 */
public interface UserMapper {

    UserEntity findByUsername(@Param("username") String username);

    void insert(UserEntity userEntity);

    Integer findIdByUsername(@Param("username") String username);

    int countAdmin();

    void updatePwd(@Param("hashedPwd") String hashedPwd, @Param("id") Long id);

    List<UserEntity> findUserInfoList();

    void disableUserById(@Param("id") int id, @Param("disabledBy") String disabledBy, @Param("disableTime") Date disableTime);
}
