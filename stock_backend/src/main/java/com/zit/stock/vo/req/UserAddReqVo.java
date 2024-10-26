package com.zit.stock.vo.req;

import lombok.Data;

/**
 * @ClassName: UserAddReqVo
 * @Description: 添加用户请求参数
 * @Author: mianbaoren
 * @Date: 2024/9/26 15:41
 */
@Data

public class UserAddReqVo {
    /**
     * 用户名
     */
    private String username;
    /**
     * 密码
     */
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
    /**
     *创建来源
     */
    private Integer createWhere;
    /**
     * 用户状态，1表示正常，2表示锁定
     */
    private Integer status;

}
