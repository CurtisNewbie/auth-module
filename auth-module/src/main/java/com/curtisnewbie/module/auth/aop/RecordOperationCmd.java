package com.curtisnewbie.module.auth.aop;

import com.curtisnewbie.service.auth.remote.vo.OperateLogVo;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 *
 * @author yongjie.zhuang
 */
@Data
@AllArgsConstructor
public class RecordOperationCmd {

    private OperateLogVo operateLogVo;
}
