package com.itheima.stock.service;

import com.itheima.stock.pojo.entity.SysUser;
import com.itheima.stock.vo.req.LoginReqVo;
import com.itheima.stock.vo.resp.LoginRespVo;
import com.itheima.stock.vo.resp.R;

import java.util.Map;

/**
 * @ClassName: UserService
 * @Description: 定义用户服务接口
 * @Author: mianbaoren
 * @Date: 2024/8/27 14:21
 */
public interface UserService {
    /**
     * 根据用户名称查询用户信息
     * @param userName
     * @return
     */
    SysUser findByUserName(String userName);

    /**
     * 用户登录
     * @param vo
     * @return
     */

    R<LoginRespVo> login(LoginReqVo vo);

    /**
     * 生成图片序列码
     * @return
     */
    R<Map> getCaptchaCode();
}
