package com.curtisnewbie.module.auth.aop;

import com.curtisnewbie.service.auth.messaging.services.AuthMessageDispatcher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * <p>
 * OperateLog Recorder based on MQ
 * </p>
 *
 * @author yongjie.zhuang
 */
@Slf4j
public class MqOperateLogRecorder implements OperateLogRecorder {

    @Autowired
    private AuthMessageDispatcher dispatcher;

    @Override
    public void recordOperation(RecordOperationCmd cmd) {
        dispatcher.dispatchOperateLog(cmd.getOperateLogVo());
    }
}
