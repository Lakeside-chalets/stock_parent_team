package com.zit.stock.vo.req;

import lombok.Data;

import java.util.List;

/**
 * @author mianbaoren
 * @date 2024/7/22 15:08
 * @description :修改角色请求参数
 */
@Data
public class UserOneRoleReqVo {

    /**
     * 用户的ID
     */
    private Long userId;

    /**
     * 要添加的角色id
     */
    private List<Long> roleIds;
}
