package com.itheima.stock.mapper;

import com.itheima.stock.pojo.entity.SysUser;
import org.apache.ibatis.annotations.Param;

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
}
