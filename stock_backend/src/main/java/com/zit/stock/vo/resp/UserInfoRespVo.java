package com.zit.stock.vo.resp;

import lombok.Data;

/**
 * @ClassName: UserInfoRespVo
 * @Description: TODO
 * @Author: mianbaoren
 * @Date: 2024/10/1 15:52
 */
@Data
public class UserInfoRespVo {
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
     * 用户状态，1表示正常，2表示锁定
     */
    private Integer status;

}
