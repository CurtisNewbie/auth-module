package com.curtisnewbie.module.auth.web;

import com.curtisnewbie.common.exceptions.MsgEmbeddedException;
import com.curtisnewbie.common.util.BeanCopyUtils;
import com.curtisnewbie.common.util.ValidUtils;
import com.curtisnewbie.common.vo.Result;
import com.curtisnewbie.module.auth.aop.LogOperation;
import com.curtisnewbie.module.auth.util.AuthUtil;
import com.curtisnewbie.module.auth.web.vo.RequestRegisterUserWebVo;
import com.curtisnewbie.module.auth.web.vo.UpdatePasswordWebVo;
import com.curtisnewbie.module.auth.web.vo.UserWebVo;
import com.curtisnewbie.service.auth.remote.consts.UserRole;
import com.curtisnewbie.service.auth.remote.exception.InvalidAuthenticationException;
import com.curtisnewbie.service.auth.remote.exception.UserRelatedException;
import com.curtisnewbie.service.auth.remote.feign.UserServiceFeign;
import com.curtisnewbie.service.auth.remote.vo.RegisterUserVo;
import com.curtisnewbie.service.auth.remote.vo.UpdatePasswordVo;
import com.curtisnewbie.service.auth.remote.vo.UserVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

/**
 * <p>
 * Basic controller for user
 * </p>
 * <p>
 * Provides common endpoints such as registration, getUserInfo and updatePassword
 * </p>
 * <p>
 * The base path is specified using SPEL, it's 'web.base-path'
 * </p>
 *
 * @author yongjie.zhuang
 */
@Slf4j
@RestController
@RequestMapping("${web.base-path}/user")
public class BasicUserController {

    private static final int PASSWORD_LENGTH = 6;

    @Autowired
    private UserServiceFeign userService;

    @LogOperation(name = "/user/register/request", description = "User request's registration approval")
    @PostMapping("/register/request")
    public Result<?> requestRegistration(@RequestBody RequestRegisterUserWebVo registerUserVo) throws UserRelatedException,
            MsgEmbeddedException {
        RegisterUserVo vo = new RegisterUserVo();
        BeanUtils.copyProperties(registerUserVo, vo);

        // validate whether username and password is entered
        ValidUtils.requireNotEmpty(vo.getUsername(), "Please enter username");
        ValidUtils.requireNotEmpty(vo.getPassword(), "Please enter password");

        // validate if the username and password are the same
        ValidUtils.requireNotEquals(vo.getUsername(), vo.getPassword(), "Username and password must be different");

        // validate if the password is too short
        if (vo.getPassword().length() < PASSWORD_LENGTH)
            return Result.error("Password must have at least " + PASSWORD_LENGTH + " characters");

        // by default role is guest
        vo.setRole(UserRole.GUEST);
        // created by this user himself/herself
        vo.setCreateBy(vo.getUsername());

        return userService.requestRegistrationApproval(vo);
    }

    @GetMapping("/info")
    public Result<UserWebVo> getUserInfo() throws InvalidAuthenticationException {
        // user is not authenticated yet
        Optional<UserVo> optionalUser = AuthUtil.getOptionalUser();
        if (!optionalUser.isPresent()) {
            return Result.ok();
        }
        UserVo ue = optionalUser.get();
        return Result.of(BeanCopyUtils.toType(ue, UserWebVo.class));
    }

    @LogOperation(name = "/user/password/update", description = "update password")
    @PostMapping("/password/update")
    public Result<Void> updatePassword(@RequestBody UpdatePasswordWebVo vo) throws MsgEmbeddedException, InvalidAuthenticationException {
        ValidUtils.requireNotEmpty(vo.getNewPassword());
        ValidUtils.requireNotEmpty(vo.getPrevPassword());

        // check if the old password and prev password are equal
        ValidUtils.requireNotEquals(vo.getNewPassword(), vo.getPrevPassword(), "New password must be different");

        // validate if the new password is too short
        if (vo.getNewPassword().length() < PASSWORD_LENGTH)
            return Result.error("Password must have at least " + PASSWORD_LENGTH + " characters");

        UserVo uv = AuthUtil.getUser();
        try {
            return userService.updatePassword(UpdatePasswordVo.builder()
                    .oldPassword(vo.getPrevPassword())
                    .newPassword(vo.getNewPassword())
                    .userId(uv.getId())
                    .build());
        } catch (UserRelatedException ignore) {
            return Result.error("Password incorrect");
        }
    }
}
