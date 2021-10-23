package com.curtisnewbie.module.auth.processing;

/**
 * <p>
 * Recorder of access_log, it's used whenever a user attempt to login
 * </p>
 *
 * @author yongjie.zhuang
 */
public interface AccessLogRecorder {

    /**
     * Record access information
     */
    void recordAccess(RecordAccessCmd cmd);
}
