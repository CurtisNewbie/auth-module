package com.curtisnewbie.module.auth.config;

import com.curtisnewbie.module.tracing.common.MdcUtil;
import com.curtisnewbie.service.auth.remote.api.RemoteUserService;
import com.curtisnewbie.service.auth.remote.consts.UserRole;
import com.curtisnewbie.service.auth.remote.exception.PasswordIncorrectException;
import com.curtisnewbie.service.auth.remote.exception.UserDisabledException;
import com.curtisnewbie.service.auth.remote.exception.UsernameNotFoundException;
import com.curtisnewbie.service.auth.remote.vo.UserVo;
import org.apache.dubbo.config.annotation.DubboReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Objects;

import static com.curtisnewbie.common.util.EnumUtils.parse;

/**
 * Custom authentication provider
 *
 * @author yongjie.zhuang
 */
@Component
public class AuthProvider implements AuthenticationProvider {

    private static final Logger logger = LoggerFactory.getLogger(AuthProvider.class);

    @DubboReference(lazy = true)
    private RemoteUserService remoteUserService;

    @Autowired
    private ModuleConfig moduleConfig;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        if (authentication.isAuthenticated())
            return authentication;

        String username = authentication.getName();
        UserVo user;
        try {
            // for log tracing
            MdcUtil.setTraceId(username);
            // attempt to authenticate
            user = remoteUserService.login(username, authentication.getCredentials().toString());
            logger.info("User '{}' authenticated", username);

            if (moduleConfig.isAdminLoginOnly()
                    && !Objects.equals(UserRole.ADMIN, parse(user.getRole(), UserRole.class))) {
                logger.info("Only allow admin to login, reject authentication");
                throw new InsufficientAuthenticationException("Only admin can login");
            }

            return buildSuccessfulAuthentication(user, authentication);
        } catch (UserDisabledException e) {
            logger.info("User '{}' is disabled", username);
            throw new DisabledException("User '" + username + "' is disabled");
        } catch (UsernameNotFoundException e) {
            logger.info("User '{}' not found", username);
            throw new org.springframework.security.core.userdetails.UsernameNotFoundException("User '" + username + "' not found");
        } catch (PasswordIncorrectException e) {
            logger.info("Incorrect password for user '{}'", username);
        }
        throw new BadCredentialsException("Incorrect username or password");
    }

    @Override
    public boolean supports(Class<?> authentication) {
        boolean result = UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
        return result;
    }

    /** Build successful Authentication */
    private static UsernamePasswordAuthenticationToken buildSuccessfulAuthentication(UserVo ue, Authentication au) {
        return new UsernamePasswordAuthenticationToken(ue,
                au.getCredentials(),
                Arrays.asList(new SimpleGrantedAuthority(ue.getRole())));
    }
}
