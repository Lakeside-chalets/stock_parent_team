package com.zit.stock.controller;

import com.zit.stock.service.RoleService;
import com.zit.stock.vo.req.RoleAddVo;
import com.zit.stock.vo.req.RolePageVo;
import com.zit.stock.vo.req.RoleUpdateVo;
import com.zit.stock.vo.resp.PageResult;
import com.zit.stock.vo.resp.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

/**
 * @ClassName: RoleController
 * @Description: 角色管理相关接口控制器
 * @Author: mianbaoren
 * @Date: 2024/10/1 19:43
 */
@Api(value = "/api", tags = {": 角色管理相关接口控制器"})
@RestController
@RequestMapping("/api")
public class RoleController {
    @Autowired
    private RoleService roleService;

    /**
     * 分页查询当前角色信息
     * @param vo
     * @return
     */
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "body", dataType = "RolePageVo", name = "vo", value = "", required = true)
    })
    @ApiOperation(value = "分页查询当前角色信息", notes = "分页查询当前角色信息", httpMethod = "POST")
    @PostMapping("/roles")
    public R<PageResult>  getRolesAllInfo(@RequestBody RolePageVo vo){
        return roleService.getRolesAllInfo(vo);
    }

    /**
     * 添加角色和角色关联权限
     * @param roleAddVo
     * @return
     */
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "body", dataType = "RoleAddVo", name = "roleAddVo", value = "", required = true)
    })
    @ApiOperation(value = "添加角色和角色关联权限", notes = "添加角色和角色关联权限", httpMethod = "POST")
    @PostMapping("/role")
    public R<String> addRoleAndPermission(@RequestBody RoleAddVo roleAddVo) {
        return roleService.addRoleAndPermission(roleAddVo);
    }

    /**
     * 根据角色id查找对应的权限id集合
     * @param roleId
     * @return
     */
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "path", dataType = "string", name = "roleId", value = "", required = true)
    })
    @ApiOperation(value = "根据角色id查找对应的权限id集合", notes = "根据角色id查找对应的权限id集合", httpMethod = "GET")
    @GetMapping("/role/{roleId}")
    public R<Set<String>> getPermissionIdByRoleId(@PathVariable("roleId") String roleId){
        return roleService.getPermissionIdByRoleId(roleId);
    }

    /**
     * 添加角色和角色关联权限,编辑角色信息提交的数据
     * @param vo
     * @return
     */
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "body", dataType = "RoleUpdateVo", name = "vo", value = "", required = true)
    })
    @ApiOperation(value = "添加角色和角色关联权限,编辑角色信息提交的数据", notes = "添加角色和角色关联权限,编辑角色信息提交的数据", httpMethod = "PUT")
    @PutMapping("/role")
    public R<String> updateRoleAndPermission (@RequestBody RoleUpdateVo vo){
        return roleService.updateRoleAndPermission(vo);
    }

    /**
     * 根据角色id删除角色
     * @param roleId
     * @return
     */
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "path", dataType = "long", name = "roleId", value = "", required = true)
    })
    @ApiOperation(value = "根据角色id删除角色", notes = "根据角色id删除角色", httpMethod = "DELETE")
    @DeleteMapping("/role/{roleId}")
    public R<String> deleteRole(@PathVariable("roleId") Long roleId){
        return roleService.deleteRole(roleId);
    }

    /**
     * 更新用户的状态信息
     * @param roleId 角色id
     * @param status 状态 1.正常 0：禁用
     * @return
     */
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "path", dataType = "long", name = "roleId", value = "角色id", required = true),
            @ApiImplicitParam(paramType = "path", dataType = "int", name = "status", value = "状态 1.正常 0：禁用", required = true)
    })
    @ApiOperation(value = "更新用户的状态信息", notes = "更新用户的状态信息", httpMethod = "POST")
    @PostMapping("/role/{roleId}/{status}")
    public R<String> updateRoleStatus (@PathVariable("roleId") Long roleId ,@PathVariable("status") Integer status){
        return roleService.updateRoleStatus(roleId,status);
    }
}
