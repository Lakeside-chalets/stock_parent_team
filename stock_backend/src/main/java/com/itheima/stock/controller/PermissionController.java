package com.itheima.stock.controller;

import com.itheima.stock.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName: PermissionController
 * @Description: 有关权限的接口控制器
 * @Author: mianbaoren
 * @Date: 2024/9/25 0:06
 */
@RestController
@RequestMapping("/api")
public class PermissionController {
    @Autowired
    private PermissionService permissionService;

}

