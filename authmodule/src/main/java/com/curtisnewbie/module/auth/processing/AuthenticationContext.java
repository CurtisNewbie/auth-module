package com.curtisnewbie.module.auth.processing;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * Context of user authentication
 * </p>
 * <p>
 * This context internally contains a map, it's used by {@link RemoteAuthenticationProvider} to share information
 * related to user authentication between the registered listeners. No extra synchronization is used, so this object
 * should only be used within the same thread.
 * </p>
 *
 * @author yongjie.zhuang
 */
@Data
public class AuthenticationContext {

    private Map<String, Object> contextMap = new HashMap<>();

}
