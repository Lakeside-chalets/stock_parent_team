package com.itheima.stock.controller;

import com.itheima.stock.service.PermissionService;
import com.itheima.stock.vo.resp.LoginRespPermission;
import com.itheima.stock.vo.resp.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public R<List<LoginRespPermission>> getAllPermission(){
        return permissionService.getAllPermission();
    }

}

