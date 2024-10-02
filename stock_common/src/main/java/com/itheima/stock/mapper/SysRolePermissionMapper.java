package com.itheima.stock.mapper;

import com.itheima.stock.pojo.entity.SysRolePermission;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

/**
* @author mianbaoren
* @description 针对表【sys_role_permission(角色权限表)】的数据库操作Mapper
* @createDate 2024-08-26 22:52:27
* @Entity com.itheima.stock.pojo.entity.SysRolePermission
*/
public interface SysRolePermissionMapper {

    int deleteByPrimaryKey(Long id);

    int insert(SysRolePermission record);

    int insertSelective(SysRolePermission record);

    SysRolePermission selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(SysRolePermission record);

    int updateByPrimaryKey(SysRolePermission record);

    /**
     * 批量添加角色权限中间表数据
     * @param list
     * @return
     */
    int addRolePermissionInfo(@Param("list") List<SysRolePermission> list);

    /**
     * 根据角色id查找对应的权限id集合
     * @param roleId
     * @return
     */
    Set<String> getPermissionIdByRoleId(@Param("roleId") String roleId);


}
