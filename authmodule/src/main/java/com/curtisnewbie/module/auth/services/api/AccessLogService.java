package com.curtisnewbie.module.auth.services.api;

import com.curtisnewbie.module.auth.dao.AccessLogEntity;

/**
 * @author yongjie.zhuang
 */
public interface AccessLogService {

    void save(AccessLogEntity accessLogEntity);
}
