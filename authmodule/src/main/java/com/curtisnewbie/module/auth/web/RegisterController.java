package com.curtisnewbie.module.auth.web;

import com.curtisnewbie.module.auth.consts.Role;
import com.curtisnewbie.module.auth.dao.RegisterUserDto;
import com.curtisnewbie.module.auth.exception.ExceededMaxAdminCountException;
import com.curtisnewbie.module.auth.exception.UserRegisteredException;
import com.curtisnewbie.module.auth.services.api.UserService;
import com.curtisnewbie.module.auth.vo.RegisterUserVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author yongjie.zhuang
 */
@RestController
@RequestMapping("/user/register")
public class RegisterController {

    @Autowired
    private UserService userService;

    @PostMapping("/guest")
    public ResponseEntity<?> guestRegister(RegisterUserVo registerUserVo) throws UserRegisteredException,
            ExceededMaxAdminCountException {
        RegisterUserDto dto = new RegisterUserDto();
        BeanUtils.copyProperties(registerUserVo, dto);
        dto.setRole(Role.GUEST.val); // only allow guest registration
        userService.register(dto);
        return ResponseEntity.ok().build();
    }
}
