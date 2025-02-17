package com.zit.stock.mapper;

import com.zit.stock.pojo.entity.SysPermission;
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

    /**
     * 获取所有权限集合
     * @return
     */
    List<SysPermission> getAllPermission();

    /**
     * 查询所有的权限集合
     * @return
     */
    List<SysPermission> findAll();

    /**
     * 根据权限父类id查询对应子集权限
     * @param permissionId
     * @return
     */
    int findChildrenCountByParentId(Long permissionId);

    /**
     * 获取父亲权限的所有子权限id号
     * @param permissionId
     * @return
     */
    List<Long>  findChildren(Long permissionId);


    /**
     * 判断是否是目录，如果是目录则跳过循环
     * @param permissionId
     * @return
     */
    Boolean checkIsMenu(@Param("permissionId") Long permissionId);
}
