package com.curtisnewbie.module.auth.processing;

import com.curtisnewbie.common.vo.Result;
import com.curtisnewbie.module.auth.config.ModuleConfig;
import com.curtisnewbie.service.auth.remote.vo.LoginVo;
import com.curtisnewbie.service.auth.remote.vo.UserVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.util.Arrays;

import static com.curtisnewbie.common.util.AssertUtils.nonNull;
import static java.lang.String.format;

/**
 * <p>
 * Authenticator based on remote RPC call to auth-service
 * </p>
 *
 * @author yongjie.zhuang
 */
@Slf4j
@Component
public class RemoteAuthenticator implements Authenticator {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ModuleConfig moduleConfig;

    @Value("${spring.application.name}")
    private String applicationName;
    @Value("${auth-service.login.url:http://auth-service/open/api/user/login}")
    private String loginUrl;

    @PostConstruct
    public void _doPostConstruct() {
        log.info("Remote Authenticator will use auth-service login url: '{}'", loginUrl);
    }

    @Override
    public AuthenticationResult authenticate(Authentication auth) {
        String username = auth.getName();

        final LoginVo loginVo = LoginVo.builder()
                .username(username)
                .password(auth.getCredentials().toString())
                .appName(applicationName)
                .build();

        // attempt to authenticate but sending requests to auth-service
        final Result<UserVo> userResult = sendLoginRequest(loginVo);

        // throw exception if notOk
        Assert.notNull(userResult, "Result<UserVo> == null");
        userResult.assertIsOk();
        nonNull(userResult.getData(), format("Unable to find user '%s'", username));

        log.info("User '{}' authenticated", username);
        return buildSuccessfulAuthentication(userResult.getData(), auth);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }

    /** Send login request to auth-service */
    protected Result<UserVo> sendLoginRequest(LoginVo loginVo) {
        final ParameterizedTypeReference<Result<UserVo>> resultType = new ParameterizedTypeReference<Result<UserVo>>() {
        };
        return restTemplate.exchange(loginUrl, HttpMethod.POST, new HttpEntity<>(loginVo), resultType)
                .getBody();
    }

    /** Build successful Authentication */
    private static AuthenticationResult buildSuccessfulAuthentication(UserVo ue, Authentication au) {
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(ue,
                au.getCredentials(),
                Arrays.asList(new SimpleGrantedAuthority(ue.getRole().getValue())));

        return new AuthenticationResult(token, ue);
    }
}
