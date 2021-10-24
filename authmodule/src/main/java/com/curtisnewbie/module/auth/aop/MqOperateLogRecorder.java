package com.curtisnewbie.module.auth.aop;

import com.curtisnewbie.module.messaging.service.MessagingParam;
import com.curtisnewbie.module.messaging.service.MessagingService;
import com.curtisnewbie.service.auth.messaging.routing.AuthServiceRoutingInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * <p>
 * OperateLog Recorder based on MQ
 * </p>
 *
 * @author yongjie.zhuang
 */
@Slf4j
@Component
public class MqOperateLogRecorder implements OperateLogRecorder {

    @Autowired
    private MessagingService messagingService;

    @Override
    public void recordOperation(RecordOperationCmd cmd) {
        messagingService.send(MessagingParam.builder()
                .payload(cmd.getOperateLogVo())
                .exchange(AuthServiceRoutingInfo.SAVE_OPERATE_LOG_ROUTING.getExchange())
                .routingKey(AuthServiceRoutingInfo.SAVE_OPERATE_LOG_ROUTING.getRoutingKey())
                .deliveryMode(MessageDeliveryMode.NON_PERSISTENT)
                .build());
    }
}
