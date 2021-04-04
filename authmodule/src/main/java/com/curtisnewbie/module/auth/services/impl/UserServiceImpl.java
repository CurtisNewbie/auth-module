package com.curtisnewbie.module.auth.services.impl;

import com.curtisnewbie.module.auth.dao.UserEntity;
import com.curtisnewbie.module.auth.dao.UserMapper;
import com.curtisnewbie.module.auth.services.api.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author yongjie.zhuang
 */
@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public UserEntity loadUserByUsername(String s) throws UsernameNotFoundException {
        UserEntity userEntity = userMapper.findByUsername(s);
        if (userEntity == null)
            throw new UsernameNotFoundException("Username '" + s + "' not found");
        return userEntity;
    }
}
