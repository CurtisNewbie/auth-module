package com.curtisnewbie.module.auth.processing;

import com.curtisnewbie.service.auth.remote.vo.UserVo;
import org.springframework.security.core.Authentication;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * Context of user authentication
 * </p>
 * <p>
 * This context internally contains a map, it's used by {@link GenericAuthenticationProvider} to share information
 * related to user authentication between the registered listeners. No extra synchronization is used, so this object
 * should only be used within the same thread.
 * </p>
 *
 * @author yongjie.zhuang
 */
public class AuthenticationContext {

    /**
     * Key to {@link Authentication} object in {@link AuthenticationContext}
     */
    public static final String AUTH_CONTEXT_AUTHENTICATION_OBJECT = "authentication-object";

    /**
     * Key to {@link UserVo} object in {@link AuthenticationContext}
     */
    public static final String AUTH_CONTEXT_USER_OBJECT = "user-object";

    private Map<String, Object> contextMap = new HashMap<>();

    public void setAuthentication(Authentication auth) {
        this.contextMap.put(AUTH_CONTEXT_AUTHENTICATION_OBJECT, auth);
    }

    public Authentication getAuthentication() {
        Object o = this.contextMap.get(AUTH_CONTEXT_AUTHENTICATION_OBJECT);
        if (o == null)
            return null;
        return (Authentication) o;
    }

    public void setUser(UserVo user) {
        this.contextMap.put(AUTH_CONTEXT_USER_OBJECT, user);
    }

    public UserVo getUser() {
        Object o = this.contextMap.get(AUTH_CONTEXT_USER_OBJECT);
        if (o == null)
            return null;
        return (UserVo) o;
    }


}
