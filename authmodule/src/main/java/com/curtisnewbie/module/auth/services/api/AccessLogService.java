package com.curtisnewbie.module.auth.services.api;

import com.curtisnewbie.common.vo.PagingVo;
import com.curtisnewbie.module.auth.dao.AccessLogEntity;
import com.curtisnewbie.module.auth.dao.AccessLogInfo;
import com.github.pagehelper.PageInfo;

/**
 * @author yongjie.zhuang
 */
public interface AccessLogService {

    void save(AccessLogEntity accessLogEntity);

    PageInfo<AccessLogInfo> findAccessLogInfoByPage(PagingVo paging);
}
