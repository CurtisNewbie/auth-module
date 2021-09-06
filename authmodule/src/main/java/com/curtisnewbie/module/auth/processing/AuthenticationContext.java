package com.curtisnewbie.module.auth.processing;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * Context of user authentication
 * </p>
 * <p>
 * Internally contains a map, it's not thread-safe, however, as long as the map is used within the same thread, it's
 * thread-safe
 * </p>
 *
 * @author yongjie.zhuang
 */
@Data
public class AuthenticationContext {

    private Map<String, Object> contextMap = new HashMap<>();

}
