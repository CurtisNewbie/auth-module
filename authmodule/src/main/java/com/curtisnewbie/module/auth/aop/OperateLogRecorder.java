package com.curtisnewbie.module.auth.aop;

/**
 * <p>
 * Recorder of operate_log, it's used whenever a user send requests to backend
 * </p>
 *
 * @author yongjie.zhuang
 */
public interface OperateLogRecorder {

    /**
     * Record operation information
     */
    void recordOperation(RecordOperationCmd cmd);
}
