package com.zit.stock.controller;

import com.zit.stock.service.LogService;
import com.zit.stock.vo.req.LogPageReqVo;
import com.zit.stock.vo.resp.PageResult;
import com.zit.stock.vo.resp.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @ClassName: LogController
 * @Description: 日志的接口控制器
 * @Author: mianbaoren
 * @Date: 2024/10/12 13:43
 */
@Api(value = "/api", tags = {": 日志的接口控制器"})
@RestController
@RequestMapping("/api")
public class LogController {


    @Autowired
    private LogService logService;

    /**
     * 日志信息综合查询
     * @param vo
     * @return
     */


    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "body", dataType = "LogPageReqVo", name = "vo", value = "", required = true)
    })
    @ApiOperation(value = "日志信息综合查询", notes = "日志信息综合查询", httpMethod = "POST")
    @PostMapping("/logs")
    @PreAuthorize("hasAuthority('sys:log:list')")
    public R<PageResult> logPageQuery(@RequestBody LogPageReqVo vo){
        return logService.logPageQuery(vo);
    }


    /**
     * 批量删除日志信息功能
     * @param logIds
     * @return
     */

    //    @StockLog("日志删除")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "body", dataType = "List<Long>", name = "logIds", value = "", required = true)
    })
    @ApiOperation(value = "批量删除日志信息功能", notes = "@StockLog(", httpMethod = "DELETE")
    @DeleteMapping("/log")
    @PreAuthorize("hasAuthority('sys:log:delete')")//权限表示与数据库定义的标识一致
    public R<String> deleteBatch(@RequestBody List<Long> logIds){
        return this.logService.deleteBatch(logIds);
    }

}
