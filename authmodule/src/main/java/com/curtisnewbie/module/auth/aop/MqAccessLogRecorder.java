package com.curtisnewbie.module.auth.aop;

import com.curtisnewbie.service.auth.messaging.services.AuthMessageDispatcher;
import com.curtisnewbie.service.auth.remote.vo.AccessLogInfoVo;
import com.curtisnewbie.service.auth.remote.vo.UserVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;

/**
 * <p>
 * AccessLog Recorder based on MQ
 * </p>
 *
 * @author yongjie.zhuang
 */
@Slf4j
public class MqAccessLogRecorder implements AccessLogRecorder {

    @Autowired
    private AuthMessageDispatcher dispatcher;

    @Override
    public void recordAccess(RecordAccessCmd cmd) {
        AccessLogInfoVo acsLog = new AccessLogInfoVo();
        acsLog.setIpAddress(cmd.getRemoteAddr());
        acsLog.setAccessTime(LocalDateTime.now());

        UserVo user = cmd.getUser();
        if (user != null) {
            acsLog.setUserId(user.getId());
            acsLog.setUsername(user.getUsername());
        }

        try {
            log.info("Logging sign-in info, ip: {}, username: {}, userId: {}",
                    acsLog.getIpAddress(),
                    acsLog.getUsername(),
                    acsLog.getUserId());

            dispatcher.dispatchAccessLog(acsLog);
        } catch (Exception e) {
            log.warn("Unable to save access-log", e);
        }

    }
}
