package com.zit.stock.vo.req;

import lombok.Data;

/**
 * @ClassName: UserUpdatePassword
 * @Description: TODO
 * @Author: mianbaoren
 * @Date: 2025/1/6 14:58
 */
@Data
public class UserUpdatePassword {

    /**
     * 用户id
     */
    private Long id;

    /**
     * 用户名
     */
    private String username;

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
    /**
     *创建来源
     */
    private Integer createWhere;
    /**
     * 用户状态，1表示正常，2表示锁定
     */
    private Integer status;

    /**
     * 旧密码
     */
    private String oldPwd;

    /**
     *新密码
     */
    private String newPwd;
}
