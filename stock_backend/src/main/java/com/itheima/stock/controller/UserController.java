package com.itheima.stock.controller;

import com.itheima.stock.pojo.entity.SysUser;
import com.itheima.stock.service.UserService;
import com.itheima.stock.vo.req.LoginReqVo;
import com.itheima.stock.vo.req.UserAddReqVo;
import com.itheima.stock.vo.req.UserPageReqVo;
import com.itheima.stock.vo.resp.LoginRespVo;
import com.itheima.stock.vo.resp.PageResult;
import com.itheima.stock.vo.resp.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @ClassName: UserController
 * @Description: 定义用户web层接口资源bean
 *
 * @Author: mianbaoren
 * @Date: 2024/8/27 14:26
 */
@Api(value = "/api", tags = {": 定义用户web层接口资源bean"})
@RestController
@RequestMapping("/api")
public class UserController {

    /**
     * 注入用户服务bean
     */
    @Autowired
    private UserService userService;

    /**
     * 根据用户名称查询用户信息
     * @param name
     * @return
     */
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "path", dataType = "string", name = "userName", value = "", required = true)
    })
    @ApiOperation(value = "根据用户名查询用户信息", notes = "根据用户名称查询用户信息", httpMethod = "GET")
    @GetMapping("/user/{userName}")
    public SysUser getUserByUserName (@PathVariable("userName")  String name){
        return userService.findByUserName(name);
    }

    /**
     * 用户登录功能
     * @param vo
     * @return
     */

    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "body", dataType = "LoginReqVo", name = "vo", value = "", required = true)
    })
    @ApiOperation(value = "用户登录功能", notes = "用户登录功能", httpMethod = "POST")
    @PostMapping("/login")
    public R<LoginRespVo> login(@RequestBody LoginReqVo vo){
        return  userService.login(vo);
    }

    /**
     * 获取验证码
     * @return
     */
    @ApiOperation(value = "获取验证码", notes = "获取验证码", httpMethod = "GET")
    @GetMapping("/captcha")
    public R<Map> getCaptchaCode(){
        return userService.getCaptchaCode();
    }

    /**
     * 多条件查询,查询用户所有信息
     * @param userPageReqVo
     * @return
     */
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "body", dataType = "UserPageReqVo", name = "userPageReqVo", value = "", required = true)
    })
    @ApiOperation(value = "多条件查询,查询用户所有信息", notes = "多条件查询,查询用户所有信息", httpMethod = "POST")
    @PostMapping("/users")
    public R<PageResult> getUserListPage(@RequestBody UserPageReqVo userPageReqVo) {
        return userService.getUserListPage(userPageReqVo);
    }

    /**
     * 添加用户
     * @param userAddReqVo
     * @return
     */
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "body", dataType = "UserAddReqVo", name = "userAddReqVo", value = "", required = true)
    })
    @ApiOperation(value = "添加用户", notes = "添加用户", httpMethod = "POST")
    @PostMapping("/user")
    public R<String> addUsers (@RequestBody UserAddReqVo userAddReqVo){
        return userService.addUsers(userAddReqVo);
    }

    /**
     * 根据用户id获取角色
     * @param userId
     * @return
     */
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "path", dataType = "long", name = "userId", value = "", required = true)
    })
    @ApiOperation(value = "根据用户id获取角色", notes = "根据用户id获取角色", httpMethod = "GET")
    @GetMapping("/user/roles/{userId}")//路径传参数
    public R<Map<String, List>> getRoleByUserId(@PathVariable ("userId") Long userId){
        return userService.getRoleByUserId(userId);
    }

}
