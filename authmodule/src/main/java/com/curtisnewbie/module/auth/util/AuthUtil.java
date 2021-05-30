package com.curtisnewbie.module.auth.util;

import com.curtisnewbie.module.auth.dao.UserEntity;
import com.curtisnewbie.module.auth.exception.InvalidAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Utilities class related Authentication
 *
 * @author yongjie.zhuang
 */
public final class AuthUtil {

    private AuthUtil() {
    }

    /**
     * Get current user's authentication
     *
     * @throws InvalidAuthenticationException when it's unable to get {@code Authentication} object; or the {@code
     *                                        Principal} object is null or not instance of {@code UserEntity}
     */
    public static UserEntity getUserEntity() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null)
            throw new InvalidAuthenticationException("Please login first");
        if (auth.getPrincipal() == null || !(auth.getPrincipal() instanceof UserEntity))
            throw new InvalidAuthenticationException("Authentication#principal is null or not instance of UserEntity");
        return UserEntity.class.cast(auth.getPrincipal());
    }

    /**
     * Get username
     *
     * @throws InvalidAuthenticationException when it's unable to get {@code Authentication} object; or the {@code
     *                                        Principal} object is null or not instance of {@code UserEntity}
     */
    public static String getUsername() {
        return getUserEntity().getUsername();
    }

}
