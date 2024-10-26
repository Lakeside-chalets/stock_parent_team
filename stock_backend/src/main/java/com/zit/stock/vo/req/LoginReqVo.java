package com.zit.stock.vo.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName: LoginReqVo
 * @Description: 登录请求参数
 * @Author: mianbaoren
 * @Date: 2024/8/29 14:18
 */
@Data
@ApiModel
public class LoginReqVo {
    /**
     * 用户名
     */
    @ApiModelProperty(value = "用户名")
    private String username;
    /**
     * 密码
     */
    @ApiModelProperty(value = "明文密码")
    private String password;
    /**
     * 验证码
     */
    @ApiModelProperty(value = "验证码")
    private String code;

    /**
     * 会话Id
     */
    @ApiModelProperty(value = "会话Id")
    private String sessionId;
}
