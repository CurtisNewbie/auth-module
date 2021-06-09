package com.curtisnewbie.module.auth.services.api;

import com.curtisnewbie.module.auth.dao.RegisterUserDto;
import com.curtisnewbie.module.auth.dao.UserEntity;
import com.curtisnewbie.module.auth.dao.UserInfo;
import com.curtisnewbie.module.auth.exception.ExceededMaxAdminCountException;
import com.curtisnewbie.module.auth.exception.UserRegisteredException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;

/**
 * @author yongjie.zhuang
 */
public interface UserService {

    /**
     * Find user by username
     *
     * @throws UsernameNotFoundException user with given username is not found
     */
    UserEntity loadUserByUsername(String username) throws UsernameNotFoundException;

    /**
     * Register user of different role
     *
     * @param registerUserDto
     * @throws UserRegisteredException        username is already registered
     * @throws ExceededMaxAdminCountException the max number of admin exceeded
     */
    void register(RegisterUserDto registerUserDto) throws UserRegisteredException, ExceededMaxAdminCountException;

    /**
     * Update password
     *
     * @param newPassword newPassword
     * @param salt        salt
     * @param id          id
     */
    void updatePassword(String newPassword, String salt, long id);

    /**
     * Fetch list of user info
     */
    List<UserInfo> findUserInfoList();
}
