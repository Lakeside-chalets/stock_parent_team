package com.zit.stock.vo.req;

import lombok.Data;

import java.util.List;

/**
 * @ClassName: RoleAddVo
 * @Description: 添加角色信息前端要请求的数据封装在这
 * @Author: mianbaoren
 * @Date: 2024/10/2 11:16
 */
@Data
public class RoleAddVo {
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
