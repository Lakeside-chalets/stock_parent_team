package com.itheima.stock.service;

import com.itheima.stock.pojo.entity.SysPermission;
import com.itheima.stock.vo.req.PermissionAddVo;
import com.itheima.stock.vo.req.PermissionUpdateVo;
import com.itheima.stock.vo.resp.LoginRespPermission;
import com.itheima.stock.vo.resp.PermissionTreeNodeVo;
import com.itheima.stock.vo.resp.R;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @ClassName: PermissionService
 * @Description: 有关权限的接口
 * @Author: mianbaoren
 * @Date: 2024/9/25 0:07
 */
public interface PermissionService {


    /**
     * 根据用户id查询用户的所有权限
     * @param id 用户id
     * @return
     */
    List<SysPermission> findPermissionsByUserId(@Param("id") Long id);

    /**
     * @param permissions 权限树状集合
     * @param pid 权限父id，顶级权限的pid默认为0
     * @param isOnlyMenuType true:遍历到菜单，  false:遍历到按钮
     * type: 目录1 菜单2 按钮3
     * @return
     */
    List<LoginRespPermission> getTree(List<SysPermission> permissions, long pid, boolean isOnlyMenuType);

    /**
     * 树状结构回显权限集合,递归获取权限数据集合
     * @return
     */
    R<List<LoginRespPermission>> getPermissionTree();

    /**
     * 获取所有权限的全部信息
     * @return
     */
    R<List<SysPermission>> getAllPermission();

    /**
     * 添加权限时回显权限树,仅仅显示目录和菜单
     * @return
     */
    R<List<PermissionTreeNodeVo>> getPermissionsTreeOnlyMeuns();

    /**
     * 权限添加按钮
     * @param vo
     * @return
     */
    R<String> addPermission(@Param("vo") PermissionAddVo vo);

    /**
     * 修改权限按钮
     * @param vo
     * @return
     */
    R<String> updatePermission(@Param("vo") PermissionUpdateVo vo);

    /**
     * 删除权限按钮菜单
     * @param permissionId
     * @return
     */
    R<String> deletePermission(@Param("permissionId") Long permissionId);
}
