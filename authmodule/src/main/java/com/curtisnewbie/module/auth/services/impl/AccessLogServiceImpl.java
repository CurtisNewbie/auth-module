package com.curtisnewbie.module.auth.services.impl;

import com.curtisnewbie.common.util.BeanCopyUtils;
import com.curtisnewbie.common.vo.PagingVo;
import com.curtisnewbie.module.auth.dao.AccessLogEntity;
import com.curtisnewbie.module.auth.dao.AccessLogInfo;
import com.curtisnewbie.module.auth.dao.AccessLogMapper;
import com.curtisnewbie.module.auth.services.api.AccessLogService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

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

    @Override
    public PageInfo<AccessLogInfo> findAccessLogInfoByPage(PagingVo paging) {
        Objects.requireNonNull(paging);
        PageHelper.startPage(paging.getPage(), paging.getLimit());
        List<AccessLogEntity> list = m.selectAllBasicInfo();
        return BeanCopyUtils.toPageList(PageInfo.of(list), AccessLogInfo.class);
    }
}
