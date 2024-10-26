package com.zit.stock.pojo.domain;

import com.zit.stock.pojo.entity.SysUser;
import lombok.Data;

@Data
public class UserPageListInfoDomain extends SysUser {

    /**
     * 创建人姓名
     */
    private String createUserName;

    /**
     * 更新人姓名
     */
    private String updateUserName;
}