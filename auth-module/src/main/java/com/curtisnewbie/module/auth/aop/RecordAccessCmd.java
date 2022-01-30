package com.curtisnewbie.module.auth.aop;

import com.curtisnewbie.service.auth.remote.vo.UserVo;
import lombok.Data;

/**
 * RecordAccess command object
 *
 * @author yongjie.zhuang
 */
@Data
public class RecordAccessCmd {

    private UserVo user;

    private String remoteAddr;
}
