package com.curtisnewbie.module.auth.services.api;

import com.curtisnewbie.module.auth.dao.UserEntity;
import com.curtisnewbie.module.auth.exception.*;
import com.curtisnewbie.module.auth.vo.RegisterUserVo;
import com.curtisnewbie.module.auth.vo.UserInfoVo;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;

/**
 * Service related to user table
 *
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
     * @param registerUserVo
     * @throws UserRegisteredException        username is already registered
     * @throws ExceededMaxAdminCountException the max number of admin exceeded
     */
    void register(RegisterUserVo registerUserVo) throws UserRegisteredException, ExceededMaxAdminCountException;

    /**
     * Update password
     *
     * @param newPassword new password (in plain text)
     * @param oldPassword old password (in plain text)
     * @param id          id
     * @throws UserNotFoundException      when the user with the given id is not found
     * @throws PasswordIncorrectException when the old password is incorrect
     */
    void updatePassword(String newPassword, String oldPassword, long id) throws UserNotFoundException,
            PasswordIncorrectException;

    /**
     * Fetch list of user info, excluding disabled users
     */
    List<UserInfoVo> findNormalUserInfoList();

    /**
     * Fetch list of user info, including disabled users
     */
    List<UserInfoVo> findAllUserInfoList();

    /**
     * Disable user by id
     *
     * @param id
     * @param disabledBy
     */
    void disableUserById(int id, String disabledBy);

    /**
     * Enable user by id
     *
     * @param id
     * @param enabledBy
     */
    void enableUserById(int id, String enabledBy);
}
