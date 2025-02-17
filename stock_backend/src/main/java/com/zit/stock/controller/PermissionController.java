package com.zit.stock.controller;

import com.zit.stock.log.annotation.StockLog;
import com.zit.stock.pojo.entity.SysPermission;
import com.zit.stock.service.PermissionService;
import com.zit.stock.vo.req.PermissionAddVo;
import com.zit.stock.vo.req.PermissionUpdateVo;
import com.zit.stock.vo.resp.LoginRespPermission;
import com.zit.stock.vo.resp.PermissionTreeNodeVo;
import com.zit.stock.vo.resp.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @ClassName: PermissionController
 * @Description: 有关权限的接口控制器
 * @Author: mianbaoren
 * @Date: 2024/9/25 0:06
 */
@Api(value = "/api", tags = {": 有关权限的接口控制器"})
@RestController
@RequestMapping("/api")
public class PermissionController {
    @Autowired
    private PermissionService permissionService;

    /**
     * 树状结构回显权限集合,递归获取权限数据集合
     * @return
     */
    @ApiOperation(value = "树状结构回显权限集合,递归获取权限数据集合", notes = "树状结构回显权限集合,递归获取权限数据集合", httpMethod = "GET")
    @GetMapping("/permissions/tree/all")
    public R<List<LoginRespPermission>> getPermissionTree(){
        return permissionService.getPermissionTree();
    }

    /**
     * 获取所有权限的全部信息
     * @return
     */
    @ApiOperation(value = "获取所有权限的全部信息", notes = "获取所有权限的全部信息", httpMethod = "GET")
    @GetMapping("/permissions")
    public R<List<SysPermission>> getAllPermission(){
        return permissionService.getAllPermission();
    }

    /**
     * 添加权限时回显权限树,仅仅显示目录和菜单
     * @return
     */
    @ApiOperation(value = "添加权限时回显权限树,仅仅显示目录和菜单", notes = "添加权限时回显权限树,仅仅显示目录和菜单", httpMethod = "GET")
    @GetMapping("/permissions/tree")
    public R<List<PermissionTreeNodeVo>> getPermissionsTreeOnlyMeuns(){
        return permissionService.getPermissionsTreeOnlyMeuns();
    }

    /**
     * 权限添加按钮
     * @param vo
     * @return
     */
    @StockLog("组织管理-菜单权限管理-添加权限按钮")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "body", dataType = "PermissionAddVo", name = "vo", value = "", required = true)
    })
    @ApiOperation(value = "权限添加按钮", notes = "权限添加按钮", httpMethod = "POST")
    @PostMapping("/permission")
    @PreAuthorize("hasAuthority('sys:permission:add')")//权限表示与数据库定义的标识一致
    public R<String> addPermission (@RequestBody PermissionAddVo vo){
        return permissionService.addPermission(vo);
    }

    /**
     * 更新修改权限按钮
     * @param vo
     * @return
     */
    @StockLog("组织管理-菜单权限管理-更新权限按钮")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "body", dataType = "PermissionUpdateVo", name = "vo", value = "", required = true)
    })
    @ApiOperation(value = "更新修改权限按钮", notes = "更新修改权限按钮", httpMethod = "PUT")
    @PutMapping("/permission")
    @PreAuthorize("hasAuthority('sys:permission:update')")//权限表示与数据库定义的标识一致
    public R<String> updatePermission(@RequestBody PermissionUpdateVo vo){
        return permissionService.updatePermission(vo);
    }

    /**
     * 删除权限按钮菜单
     * @param permissionId
     * @return
     */
    @StockLog("组织管理-菜单权限管理-删除权限按钮")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "path", dataType = "long", name = "permissionId", value = "", required = true)
    })
    @ApiOperation(value = "删除权限按钮菜单", notes = "删除权限按钮菜单", httpMethod = "DELETE")
    @DeleteMapping("/permission/{permissionId}")
    @PreAuthorize("hasAuthority('sys:permission:delete')")//权限表示与数据库定义的标识一致
    public R<String> deletePermission(@PathVariable("permissionId") Long permissionId){
    return permissionService.deletePermission(permissionId);
    }

}

