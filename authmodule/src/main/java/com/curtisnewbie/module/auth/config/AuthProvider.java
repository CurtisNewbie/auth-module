package com.curtisnewbie.module.auth.config;

import com.curtisnewbie.module.auth.consts.UserIsDisabled;
import com.curtisnewbie.module.auth.dao.UserEntity;
import com.curtisnewbie.module.auth.services.api.UserService;
import com.curtisnewbie.module.auth.util.PasswordUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Objects;

/**
 * Customer authentication provider
 *
 * @author yongjie.zhuang
 */
@Component
public class AuthProvider implements AuthenticationProvider {

    private static final Logger logger = LoggerFactory.getLogger(AuthProvider.class);

    private final UserService userService;

    public AuthProvider(UserService userService) {
        this.userService = userService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        if (authentication.isAuthenticated())
            return authentication;

        String username = authentication.getName();
        UserEntity user = userService.loadUserByUsername(username);
        if (user == null) {
            logger.info("User '{}' not found", username);
            throw new UsernameNotFoundException("User '" + username + "' not found");
        }
        if (Objects.equals(user.getIsDisabled(), UserIsDisabled.DISABLED.getValue())) {
            logger.info("User '{}' is disabled", username);
            throw new DisabledException("User '" + username + "' is disabled");
        }
        String password = authentication.getCredentials().toString();
        boolean isPasswordMatched = PasswordUtil.getValidator()
                .givenPasswordAndSalt(password, user.getSalt())
                .compareTo(user.getPassword())
                .isMatched();
        if (isPasswordMatched) {
            logger.info("User '{}' authenticated", username);
            return buildSuccessfulAuthentication(user, authentication);
        }
        throw new BadCredentialsException("Incorrect username or password");
    }

    @Override
    public boolean supports(Class<?> authentication) {
        boolean result = UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
        return result;
    }

    /** Build successful Authentication */
    private static UsernamePasswordAuthenticationToken buildSuccessfulAuthentication(UserEntity ue, Authentication au) {
        return new UsernamePasswordAuthenticationToken(ue,
                au.getCredentials(),
                Arrays.asList(new SimpleGrantedAuthority(ue.getRole())));
    }
}
