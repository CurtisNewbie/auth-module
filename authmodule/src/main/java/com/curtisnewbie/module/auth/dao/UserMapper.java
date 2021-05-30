package com.curtisnewbie.module.auth.dao;

import org.apache.ibatis.annotations.Param;

/**
 * @author yongjie.zhuang
 */
public interface UserMapper {

    UserEntity findByUsername(@Param("username") String username);

    void insert(UserEntity userEntity);

    Integer findIdByUsername(@Param("username") String username);

    int countAdmin();

    void updatePwd(@Param("hashedPwd") String hashedPwd, @Param("id") Long id);
}
