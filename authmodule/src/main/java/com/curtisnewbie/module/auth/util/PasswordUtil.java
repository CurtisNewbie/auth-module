package com.curtisnewbie.module.auth.util;

import org.springframework.security.crypto.password.MessageDigestPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Objects;

/**
 * Utility class for password handling (e.g., password validation, password encoding)
 *
 * @author yongjie.zhuang
 */
public final class PasswordUtil {

    private static final PasswordEncoder sha256PwEncoder = new MessageDigestPasswordEncoder("SHA-256");

    private PasswordUtil() {

    }

    /**
     * Get {@code PasswordValidator} for password validation
     * <p>
     * Use it as follows:
     * <pre>
     * boolean isPasswordMatched = PasswordValidUtil.getValidator()
     *  .givenPassword(enteredPassword)
     *  .compareTo(userEntity.getPassword())
     *  .withSalt(userEntity.getSalt())
     *  .isMatched();
     * </pre>
     */
    public static PasswordValidator getValidator() {
        return new PasswordValidator();
    }

    /**
     * Encode password with salt
     *
     * @param plainPwd password
     * @param salt     salt
     */
    public static String encodePassword(String plainPwd, String salt) {
        return sha256PwEncoder.encode(plainPwd.concat(salt));
    }


    public static class PasswordValidator {
        private String pwdValidated;
        private String correctPwd;
        private String salt;

        private PasswordValidator() {
        }

        /**
         * The password that is being validated (which is the one user entered)
         */
        public PasswordValidator givenPassword(String password) {
            this.pwdValidated = password;
            return this;
        }

        /**
         * The correct password in database (the hashed password)
         */
        public PasswordValidator compareTo(String pwdInDb) {
            this.correctPwd = pwdInDb;
            return this;
        }

        /**
         * Using salt
         */
        public PasswordValidator withSalt(String salt) {
            this.salt = salt;
            return this;
        }

        /**
         * Check if the passwords are matched
         */
        public boolean isMatched() {
            Objects.requireNonNull(pwdValidated);
            Objects.requireNonNull(salt);
            Objects.requireNonNull(correctPwd);
            return sha256PwEncoder.matches(pwdValidated.concat(salt), correctPwd);
        }
    }
}
