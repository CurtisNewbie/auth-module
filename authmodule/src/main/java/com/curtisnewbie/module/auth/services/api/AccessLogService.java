package com.curtisnewbie.module.auth.services.api;

import com.curtisnewbie.common.vo.PagingVo;
import com.curtisnewbie.module.auth.dao.AccessLogEntity;
import com.curtisnewbie.module.auth.vo.AccessLogInfoVo;
import com.github.pagehelper.PageInfo;

/**
 * @author yongjie.zhuang
 */
public interface AccessLogService {

    void save(AccessLogEntity accessLogEntity);

    PageInfo<AccessLogInfoVo> findAccessLogInfoByPage(PagingVo paging);
}
