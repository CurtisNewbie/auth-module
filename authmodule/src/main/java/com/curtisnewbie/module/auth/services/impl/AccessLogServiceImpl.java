package com.curtisnewbie.module.auth.services.impl;

import com.curtisnewbie.module.auth.dao.AccessLogEntity;
import com.curtisnewbie.module.auth.dao.AccessLogMapper;
import com.curtisnewbie.module.auth.services.api.AccessLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author yongjie.zhuang
 */
@Service
public class AccessLogServiceImpl implements AccessLogService {

    @Autowired
    private AccessLogMapper m;


    @Override
    public void save(AccessLogEntity accessLogEntity) {
        m.insert(accessLogEntity);
    }
}
