package com.zit.stock.vo.req;

import lombok.Data;

import java.util.List;

/**
 * @ClassName: RoleUpdateVo
 * @Description: 角色信息编辑请求参数表
 * @Author: mianbaoren
 * @Date: 2024/10/2 12:01
 */
@Data
public class RoleUpdateVo {
    /**
     * 角色id
     */
    private Long id;
    /**
     * 角色名
     */
    private String name;
    /**
     * 角色描述
     */
    private String description;
    /**
     * 权限集合
     */
    private List<Long> permissionsIds;
}
