package com.zit.stock.vo.resp;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author by itheima
 * @Date 2021/12/24
 * @Description 登录后响应前端的vo
 */
@ApiModel(description = "登录后响应前端的vo")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginRespVo {
    /**
     * 用户ID
     * 将Long类型数字进行json格式转化时，转成String格式类型
     */
    @ApiModelProperty(value = "主键ID", position = 1)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;
    /**
     * 电话
     */
    @ApiModelProperty(value = "电话", position = 2)
    private String phone;
    /**
     * 用户名
     */
    @ApiModelProperty(value = "用户名", position = 3)
    private String username;
    /**
     * 昵称
     */
    @ApiModelProperty(value = "昵称", position = 4)
    private String nickName;
    /**
     * 真实姓名
     */
    @ApiModelProperty(value = "真实姓名", position = 5)
    private String realName;

    /**
     * 性别
     */
    @ApiModelProperty(value = "性别", position = 6)
    private Integer sex;

    /**
     * 状态
     */
    @ApiModelProperty(value = "状态", position = 7)
    private Integer status;

    /**
     * 邮件
     */
    @ApiModelProperty(value = "邮件", position = 8)
    private String email;

    /**
     * 侧边栏权限树（不包含按钮权限）
     */
    @ApiModelProperty(value = "侧边栏权限树（不包含按钮权限）", position = 9)
    private List<LoginRespPermission> menus;

    /**
     * 按钮权限标识
     */
    @ApiModelProperty(value = "按钮权限标识", position = 10)
    private List<String> permissions;

    /**
     * 响应前端token
     */
    @ApiModelProperty(value = "响应前端token", position = 11)
    private String accessToken;

}