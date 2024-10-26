package com.zit.stock.vo.req;

import lombok.Data;

/**
 * @ClassName: RolePageVo
 * @Description: 角色请求参数
 * @Author: mianbaoren
 * @Date: 2024/8/29 14:18
 */

@Data
public class RolePageVo {
    /**
     * 当前页
     */
    private Integer pageNum=1;
    /**
     * 每页大小
     */
    private Integer pageSize=15;
}