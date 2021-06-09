package com.curtisnewbie.module.auth.services.impl;

import com.curtisnewbie.module.auth.consts.UserRole;
import com.curtisnewbie.module.auth.dao.RegisterUserDto;
import com.curtisnewbie.module.auth.dao.UserEntity;
import com.curtisnewbie.module.auth.dao.UserInfo;
import com.curtisnewbie.module.auth.dao.UserMapper;
import com.curtisnewbie.module.auth.exception.ExceededMaxAdminCountException;
import com.curtisnewbie.module.auth.exception.UserRegisteredException;
import com.curtisnewbie.module.auth.services.api.UserService;
import com.curtisnewbie.module.auth.util.PasswordUtil;
import com.curtisnewbie.module.auth.util.RandomNumUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author yongjie.zhuang
 */
@Service
@Transactional
public class UserServiceImpl implements UserService {

    private static final String ADMIN_LIMIT_COUNT_KEY = "admin.count.limit";
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private Environment environment;

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public UserEntity loadUserByUsername(String s) throws UsernameNotFoundException {
        Objects.requireNonNull(s);
        UserEntity userEntity = userMapper.findByUsername(s);
        if (userEntity == null)
            throw new UsernameNotFoundException("Username '" + s + "' not found");
        return userEntity;
    }

    @Override
    public void register(RegisterUserDto registerUserDto) throws UserRegisteredException, ExceededMaxAdminCountException {
        Objects.requireNonNull(registerUserDto);
        Objects.requireNonNull(registerUserDto.getUsername());
        Objects.requireNonNull(registerUserDto.getPassword());
        Objects.requireNonNull(registerUserDto.getRole());

        if (userMapper.findIdByUsername(registerUserDto.getUsername()) != null) {
            throw new UserRegisteredException(registerUserDto.getUsername());
        }

        // make sure the role is either 'admin' or 'guest'
        if (registerUserDto.getRole().equalsIgnoreCase(UserRole.ADMIN.val)) {
            registerUserDto.setRole(UserRole.ADMIN.val);
        } else {
            registerUserDto.setRole(UserRole.GUEST.val);
        }

        // limit the total number of administrators
        Optional<Integer> optInt = parseInteger(environment.getProperty(ADMIN_LIMIT_COUNT_KEY));
        if (optInt.isPresent() && registerUserDto.getRole().equals(UserRole.ADMIN.val)) {
            int currCntOfAdmin = userMapper.countAdmin();
            // exceeded the max num of administrators
            if (currCntOfAdmin >= optInt.get()) {
                throw new ExceededMaxAdminCountException(MessageFormat.format("Max: {0}, curr: {1}",
                        optInt.get(), currCntOfAdmin));
            }
        }
        userMapper.insert(toUserEntity(registerUserDto));
    }

    @Override
    public void updatePassword(String newPassword, String salt, long id) {
        userMapper.updatePwd(PasswordUtil.encodePassword(newPassword, salt), id);
    }

    @Override
    public List<UserInfo> findUserInfoList() {
        return userMapper.findUserInfoList().stream().map(ue -> {
            UserInfo ui = new UserInfo();
            BeanUtils.copyProperties(ue, ui);
            return ui;
        }).collect(Collectors.toList());
    }

    private UserEntity toUserEntity(RegisterUserDto registerUserDto) {
        UserEntity u = new UserEntity();
        u.setUsername(registerUserDto.getUsername());
        u.setRole(registerUserDto.getRole());
        u.setSalt(RandomNumUtil.randomNoStr(5));
        u.setPassword(PasswordUtil.encodePassword(registerUserDto.getPassword(), u.getSalt()));
        return u;
    }

    private static Optional<Integer> parseInteger(String value) {
        if (value == null)
            return Optional.empty();
        try {
            int count = Integer.parseInt(value);
            if (count < 0)
                return Optional.empty();
            return Optional.of(count);
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }

}
