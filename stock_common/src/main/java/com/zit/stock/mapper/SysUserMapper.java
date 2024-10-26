package com.zit.stock.mapper;

import com.zit.stock.pojo.domain.UserPageListInfoDomain;
import com.zit.stock.pojo.entity.SysUser;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author mianbaoren
* @description 针对表【sys_user(用户表)】的数据库操作Mapper
* @createDate 2024-08-26 22:52:27
* @Entity com.itheima.stock.pojo.entity.SysUser
*/
public interface SysUserMapper {

    int deleteByPrimaryKey(Long id);

    int insert(SysUser record);

    int insertSelective(SysUser record);

    SysUser selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(SysUser record);

    int updateByPrimaryKey(SysUser record);

    /**
     * 根据用户名称查询用户信息
     * @param userName 用户名称
     * @return
     */
    SysUser findByUserName(@Param("name") String userName);

    /**
     * 多条件查询用户信息
     * @param username 用户名
     * @param nickName 昵称
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return
     */
    List<UserPageListInfoDomain> findUserAllInfoByPage(@Param("userName") String username, @Param("nickName") String nickName, @Param("startTime") String startTime, @Param("endTime") String endTime);

    /**
     * 根据用户Id获取角色Id
     * @param userId 用户Id
     * @return
     */
    List<Long> findRolesIdByUserId(@Param("userId") Long userId);

    /**
     * 逻辑删除用户信息，就是将delete变成为0
     * @param userIds
     * @return
     */
    int DeleteByUserid(@Param("userIds") List<Long> userIds);
}
