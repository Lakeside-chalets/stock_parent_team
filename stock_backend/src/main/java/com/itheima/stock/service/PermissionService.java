package com.itheima.stock.service;

import com.itheima.stock.pojo.entity.SysPermission;
import com.itheima.stock.vo.resp.LoginRespPermission;
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
}
