package com.itheima.stock.mapper;

import com.itheima.stock.pojo.entity.SysPermission;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author mianbaoren
* @description 针对表【sys_permission(权限表（菜单）)】的数据库操作Mapper
* @createDate 2024-08-26 22:52:27
* @Entity com.itheima.stock.pojo.entity.SysPermission
*/
public interface SysPermissionMapper {

    int deleteByPrimaryKey(Long id);

    int insert(SysPermission record);

    int insertSelective(SysPermission record);

    SysPermission selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(SysPermission record);

    int updateByPrimaryKey(SysPermission record);

    /**
     * 根据用户id查询用户的所有权限
     * @param id
     * @return
     */
    List<SysPermission> findPermissionsByUserId(@Param("id") Long id);
}
