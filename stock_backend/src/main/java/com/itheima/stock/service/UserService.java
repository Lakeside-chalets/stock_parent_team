package com.itheima.stock.service;

import com.itheima.stock.pojo.entity.SysUser;
import com.itheima.stock.vo.req.*;
import com.itheima.stock.vo.resp.LoginRespVo;
import com.itheima.stock.vo.resp.PageResult;
import com.itheima.stock.vo.resp.R;
import com.itheima.stock.vo.resp.UserInfoRespVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
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

    /**
     * 获取用户信息分页查询条件包含：分页信息 用户创建日期范围
     * @param userPageReqVo 用户信息请求参数
     * @return
     */
    R<PageResult> getUserListPage(@Param("userPageReqVo") UserPageReqVo userPageReqVo);

    /**
     * 添加用户
     * @param userAddReqVo 请求参数
     * @return
     */
    R<String> addUsers(@Param("userAddReqVo") UserAddReqVo userAddReqVo);

    /**
     * 根据用户id获取角色
     * @param userId 用户id
     * @return
     */
    R<Map<String, List>> getRoleByUserId(@Param("userId") Long userId);

    /**
     * 更新用户角色信息
     * @param vo
     * @return
     */
    R<String> updateUserRolesInfo(@Param("vo") UserOneRoleReqVo vo);

    /**
     * 批量删除用户信息
     * @param userIds
     * @return
     */
    R<String> DeleteByUserid(@Param("userIds") List<Long> userIds);

    /**
     * 获根据用户id获取用户信息，完成个人资料展示
     * @param id 用户id
     * @return
     */
    R<UserInfoRespVo> getUserInfo(@Param("id") Long id);

    /**
     * 据id更新用户基本信息
     * @param vo
     * @return
     */
    R<String> updateUserInfo(@Param("vo") UserUpdateInfoVo vo);
}
