package com.zit.stock.vo.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author mianbaoren
 * @date 2024/7/22 22:30
 * @description : 权限添加的请求参数
 */
@ApiModel(description = ": 权限添加的请求参数")
@Data
public class PermissionAddVo {
    /**
     * 菜单等级 0 顶级目录 1.目录 2 菜单 3 按钮
     */
    @ApiModelProperty(value = "菜单等级 0 顶级目录 1.目录 2 菜单 3 按钮", position = 1)
    private Integer type;
    /**
     * 标题
     */
    @ApiModelProperty(value = "标题", position = 2)
    private String title;
    /**
     * pid
     */
    @ApiModelProperty(value = "pid", position = 3)
    private Long pid;
    /**
     * 对应资源路径
     *  1.如果类型是目录，则url为空
     *  2.如果类型是菜单，则url对应路由地址
     *  3.如果类型是按钮，则url对应是访问接口的地址
     */
    @ApiModelProperty(value = "对应资源路径  1.如果类型是目录，则url为空  2.如果类型是菜单，则url对应路由地址  3.如果类型是按钮，则url对应是访问接口的地址", position = 4)
    private String url;
    /**
     * 只有菜单类型有名称，默认是路由的名称
     */
    @ApiModelProperty(value = "只有菜单类型有名称，默认是路由的名称", position = 5)
    private String name;
    /**
     * 图标
     */
    @ApiModelProperty(value = "图标", position = 6)
    private String icon;
    /**
     * 1.基于springSecrutiry约定的权限过滤便是
     */
    @ApiModelProperty(value = "1.基于springSecrutiry约定的权限过滤便是", position = 7)
    private String perms;
    /**
     * 请求方式：get put delete post等
     */
    @ApiModelProperty(value = "请求方式：get put delete post等", position = 8)
    private String method;
    /**
     * vue按钮回显控制辨识
     */
    @ApiModelProperty(value = "vue按钮回显控制辨识", position = 9)
    private String code;
    /**
     * 排序
     */
    @ApiModelProperty(value = "排序", position = 10)
    private Integer orderNum;
}
