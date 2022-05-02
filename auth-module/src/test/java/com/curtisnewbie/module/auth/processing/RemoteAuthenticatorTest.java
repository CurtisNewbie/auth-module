package com.curtisnewbie.module.auth.processing;

import com.curtisnewbie.common.vo.Result;
import com.curtisnewbie.service.auth.remote.vo.LoginVo;
import com.curtisnewbie.service.auth.remote.vo.UserVo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author yongj.zhuang
 */
@SpringBootApplication(scanBasePackages = "com.curtisnewbie")
@SpringBootTest
public class RemoteAuthenticatorTest {

    @Autowired
    private RemoteAuthenticator remoteAuthenticator;

    @Test
    public void should_send_login_request() {
        LoginVo loginVo = new LoginVo();
        loginVo.setUsername("username");
        loginVo.setPassword("password");
        Result<UserVo> userVoResult = remoteAuthenticator.sendLoginRequest(loginVo);
        Assertions.assertNotNull(userVoResult);
    }
}
