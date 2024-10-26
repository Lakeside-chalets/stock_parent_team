package com.zit.stock.service;

import com.zit.stock.vo.req.RoleAddVo;
import com.zit.stock.vo.req.RolePageVo;
import com.zit.stock.vo.req.RoleUpdateVo;
import com.zit.stock.vo.resp.PageResult;
import com.zit.stock.vo.resp.R;
import org.apache.ibatis.annotations.Param;

import java.util.Set;

/**
 * @ClassName: RoleService
 * @Description: 角色管理相关接口
 * @Author: mianbaoren
 * @Date: 2024/10/1 19:46
 */

public interface RoleService {

    /**
     * 分页查询当前角色信息
     * @param vo
     * @return
     */
    R<PageResult> getRolesAllInfo(@Param("vo") RolePageVo vo);

    /**
     * 添加角色和角色关联权限
     * @param roleAddVo
     * @return
     */
    R<String> addRoleAndPermission(@Param("roleAddVo") RoleAddVo roleAddVo);

    /**
     * 根据角色id查找对应的权限id集合
     * @param roleId
     * @return
     */
    R<Set<String>> getPermissionIdByRoleId(@Param("roleId") String roleId);

    /**
     * 添加角色和角色关联权限,编辑角色信息提交的数据
     * @param vo
     * @return
     */
    R<String> updateRoleAndPermission(@Param("vo") RoleUpdateVo vo);

    /**
     * 根据角色id删除角色
     * @param roleId
     * @return
     */
    R<String> deleteRole(@Param("roleId") Long roleId);

    /**
     * 更新用户的状态信息
     * @param roleId 角色id
     * @param status 状态 1.正常 0：禁用
     * @return
     */
    R<String> updateRoleStatus(@Param("roleId") Long roleId, @Param("status") Integer status);
}
