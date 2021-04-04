package com.curtisnewbie.module.auth.services.api;

import com.curtisnewbie.module.auth.dao.UserEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * @author yongjie.zhuang
 */
public interface UserService {

    UserEntity loadUserByUsername(String s) throws UsernameNotFoundException;
}
