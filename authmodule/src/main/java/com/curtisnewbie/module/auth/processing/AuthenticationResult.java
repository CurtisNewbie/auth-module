package com.curtisnewbie.module.auth.processing;

import com.curtisnewbie.service.auth.remote.vo.UserVo;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.security.core.Authentication;

/**
 * <p>
 * Result of authentication
 * </p>
 *
 * @author yongjie.zhuang
 */
@Data
@AllArgsConstructor
public class AuthenticationResult {

    /**
     * Authenticated authentication object
     */
    private final Authentication authentication;

    /**
     * User
     */
    private final UserVo user;

}
