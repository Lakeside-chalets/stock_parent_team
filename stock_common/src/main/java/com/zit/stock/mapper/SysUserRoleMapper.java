package com.zit.stock.mapper;

import com.zit.stock.pojo.entity.SysUserRole;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author mianbaoren
* @description 针对表【sys_user_role(用户角色表)】的数据库操作Mapper
* @createDate 2024-08-26 22:52:27
* @Entity com.itheima.stock.pojo.entity.SysUserRole
*/
public interface SysUserRoleMapper {

    int deleteByPrimaryKey(Long id);

    int insert(SysUserRole record);

    int insertSelective(SysUserRole record);

    SysUserRole selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(SysUserRole record);

    int updateByPrimaryKey(SysUserRole record);

    /**
     * 根据用户的id删除用户对应的角色信息
     * @param userId
     */
    void deleteByUserId(@Param("userId") Long userId);


    /**
     * 批量插入用户角色信息
     * @param list
     * @return
     */
    int insertUserRoleBatch(@Param("list") List<SysUserRole> list);
}
