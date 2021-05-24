package com.curtisnewbie.module.auth.services.api;

import com.curtisnewbie.module.auth.dao.RegisterUserDto;
import com.curtisnewbie.module.auth.dao.UserEntity;
import com.curtisnewbie.module.auth.exception.ExceededMaxAdminCountException;
import com.curtisnewbie.module.auth.exception.UserRegisteredException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * @author yongjie.zhuang
 */
public interface UserService {

    UserEntity loadUserByUsername(String s) throws UsernameNotFoundException;

    void register(RegisterUserDto registerUserDto) throws UserRegisteredException, ExceededMaxAdminCountException;
}
