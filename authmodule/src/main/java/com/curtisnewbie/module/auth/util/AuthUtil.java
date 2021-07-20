package com.curtisnewbie.module.auth.util;

import com.curtisnewbie.service.auth.remote.exception.InvalidAuthenticationException;
import com.curtisnewbie.service.auth.remote.vo.UserVo;
import org.springframework.lang.Nullable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Utilities class related to Authentication
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
    public static UserVo getUser() throws InvalidAuthenticationException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null)
            throw new InvalidAuthenticationException("Please login first");
        if (auth.getPrincipal() == null || !(auth.getPrincipal() instanceof UserVo))
            throw new InvalidAuthenticationException("Authentication#principal is null or not instance of UserEntity");
        return UserVo.class.cast(auth.getPrincipal());
    }

    /**
     * Check if current user's authentication's principal is present
     *
     * @param targetPrincipalType (optional) target type of the principal, this param checks whether the target type is
     *                            is assignable from the Principal's class; if not, this method returns false
     */
    public static boolean isPrincipalPresent(@Nullable Class<?> targetPrincipalType) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null)
            return false;
        if (auth.getPrincipal() == null) {
            return false;
        }
        if (targetPrincipalType != null)
            return targetPrincipalType.isAssignableFrom(auth.getPrincipal().getClass());
        else
            return true;
    }

    /**
     * Get username
     *
     * @throws InvalidAuthenticationException when the authentication is invalid
     */
    public static String getUsername() throws InvalidAuthenticationException {
        String n = getUser().getUsername();
        if (n == null)
            throw new InvalidAuthenticationException("UserEntity's username is null");
        return n;
    }

    /**
     * Get userId
     *
     * @throws InvalidAuthenticationException when the authentication is invalid
     */
    public static Integer getUserId() throws InvalidAuthenticationException {
        Integer id = getUser().getId();
        if (id == null)
            throw new InvalidAuthenticationException("UserEntity's id is null");
        return id;
    }

}
