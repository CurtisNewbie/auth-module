package com.curtisnewbie.module.auth.processing;

import com.curtisnewbie.module.messaging.service.MessagingParam;
import com.curtisnewbie.module.messaging.service.MessagingService;
import com.curtisnewbie.service.auth.messaging.routing.AuthServiceRoutingInfo;
import com.curtisnewbie.service.auth.remote.vo.AccessLogInfoVo;
import com.curtisnewbie.service.auth.remote.vo.UserVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author yongjie.zhuang
 */
@Slf4j
@Component
public class AccessLogRecorderImpl implements AccessLogRecorder {

    @Autowired
    private MessagingService messagingService;

    @Override
    public void recordAccess(RecordAccessCmd cmd) {
        AccessLogInfoVo acsLog = new AccessLogInfoVo();
        acsLog.setIpAddress(cmd.getRemoteAddr());
        acsLog.setAccessTime(new Date());

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

            messagingService.send(MessagingParam.builder()
                    .payload(acsLog)
                    .exchange(AuthServiceRoutingInfo.SAVE_ACCESS_LOG_ROUTING.getExchange())
                    .routingKey(AuthServiceRoutingInfo.SAVE_ACCESS_LOG_ROUTING.getRoutingKey())
                    .deliveryMode(MessageDeliveryMode.NON_PERSISTENT)
                    .build());
        } catch (Exception e) {
            log.warn("Unable to save access-log", e);
        }

    }
}
