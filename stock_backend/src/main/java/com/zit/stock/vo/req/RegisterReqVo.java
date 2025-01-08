package com.zit.stock.vo.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName: LoginReqVo
 * @Description: 注册请求参数
 * @Author: mianbaoren
 * @Date: 2024/8/29 14:18
 */
@Data
@ApiModel
public class RegisterReqVo {
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
     *电话
     */
    private String phone;
    /**
     * 邮箱
     */
    private String email;
    /**
     * 昵称
     */
    private String nickName;
    /**
     * 真实姓名
     */
    private String realName;
    /**
     * 性别1表示男，0表示女
     */
    private Integer sex;




}
