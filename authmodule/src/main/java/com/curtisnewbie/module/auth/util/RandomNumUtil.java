package com.curtisnewbie.module.auth.util;

import java.security.SecureRandom;

/**
 * @author yongjie.zhuang
 */
public final class RandomNumUtil {

    private static final SecureRandom sr = new SecureRandom();

    private RandomNumUtil() {
    }

    public static final String randomNoStr(int len) {
        if (len <= 0)
            throw new IllegalArgumentException("len must be greater than zero");

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < len; i++) {
            sb.append(sr.nextInt(10));
        }
        return sb.toString();
    }

}
