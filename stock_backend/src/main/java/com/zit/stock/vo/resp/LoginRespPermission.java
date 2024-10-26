package com.zit.stock.vo.resp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @ClassName: LoginRespPermission
 * @Description: 用户返回权限数据
 * @Author: mianbaoren
 * @Date: 2024/9/25 0:17
 */
@ApiModel(description = ": 用户返回权限数据")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginRespPermission {

    /**
     * 角色ID
     */
    @ApiModelProperty(value = "角色ID", position = 1)
    private Long id;
    /**
     * 角色标题
     */
    @ApiModelProperty(value = "角色标题", position = 2)
    private String title;
    /**
     * 角色图标
     */
    @ApiModelProperty(value = "角色图标", position = 3)
    private String icon;
    /**
     * 路由地址URL
     */
    @ApiModelProperty(value = "路由地址URL", position = 4)
    private String path;

    /**
     * 路由名称
     */
    @ApiModelProperty(value = "路由名称", position = 5)
    private String name;

    /**
     * 菜单数结构
     */
    @ApiModelProperty(value = "菜单数结构", position = 6)
    private List<LoginRespPermission> children;
}
