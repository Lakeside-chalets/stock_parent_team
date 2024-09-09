package com.itheima.stock.controller;

import com.itheima.stock.pojo.domain.*;
import com.itheima.stock.service.StockService;
import com.itheima.stock.vo.resp.PageResult;
import com.itheima.stock.vo.resp.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * @ClassName: StockController
 * @Description: 定义股票相关接口控制器
 * @Author: mianbaoren
 * @Date: 2024/8/30 22:14
 */
@Api(value = "/api/quot", tags = {": 定义股票相关接口控制器"})
@RestController
@RequestMapping("/api/quot")
public class StockController {
    @Autowired
    private StockService stockService;


    /**
     * 获取国内大盘最新的数据
     * @return
     */
    @ApiOperation(value = "获取国内大盘最新的数据", notes = "获取国内大盘最新的数据", httpMethod = "GET")
    @GetMapping("/index/all")
    public R<List<InnerMarketDomain>> getInnerMarketInfo() {

        return stockService.getInnerMarketInfo();
    }

    /**
     * 获取国内板块的最新数据
     * @return
     */
    @ApiOperation(value = "获取国内板块的最新数据", notes = "获取国内板块的最新数据", httpMethod = "GET")
    @GetMapping("/sector/all")
    public R<List<StockBlockDomain>> getStockBlockInfo(){
        return stockService.getStockBlockInfo();
    }

    /**
     * 分页查询最新的股票交易数据
     * @param page 当前页
     * @param pageSize 每页的大小
     * @return
     */
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", dataType = "int", name = "page", value = "当前页"),
            @ApiImplicitParam(paramType = "query", dataType = "int", name = "pageSize", value = "每页的大小")
    })
    @ApiOperation(value = "分页查询最新的股票交易数据", notes = "分页查询最新的股票交易数据", httpMethod = "GET")
    @GetMapping("/stock/all")
    public R<PageResult<StockUpdownDomain>> getStockInfoByPage(@RequestParam(value = "page",required = false ,defaultValue = "1") Integer page,
                                                               @RequestParam(value = "pageSize",required = false ,defaultValue = "20")Integer pageSize){
        return  stockService.getStockInfoByPage(page,pageSize);
    }
    /**
     * 公布最新的涨幅榜数据
     * @return
     */

    @ApiOperation(value = "公布最新的涨幅榜数据", notes = "公布最新的涨幅榜数据", httpMethod = "GET")
    @GetMapping("/stock/increase")
    R<List<StockUpdownDomain>> getStockInfoByPageonBoard(){

    return stockService.getStockInfoByPageonBoard();
    }

    /**
     * 统计最新股票日内每分钟涨跌停数量
     * @return
     */
    @ApiOperation(value = "统计最新股票日内每分钟涨跌停数量", notes = "统计最新股票日内每分钟涨跌停数量", httpMethod = "GET")
    @GetMapping("/stock/updown/count")
    public R<Map<String,List>> getStockUpDownCount(){
        return stockService.getStockUpDownCount();
    }


    /**
     * 导出指定页码的最新股票信息
     * @param page 当前页
     * @param pageSize 每页的大小
     * @param response 响应
     * @return
     */

    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", dataType = "int", name = "page", value = "当前页"),
            @ApiImplicitParam(paramType = "query", dataType = "int", name = "pageSize", value = "每页的大小")
    })
    @ApiOperation(value = "导出指定页码的最新股票信息", notes = "导出指定页码的最新股票信息", httpMethod = "GET")
    @GetMapping("/stock/export")
    public void  exportStockUpDownInfo(@RequestParam(value = "page",required = false ,defaultValue = "1") Integer page,
                                                                  @RequestParam(value = "pageSize",required = false ,defaultValue = "20")Integer pageSize,
                                                                  HttpServletResponse response){
      stockService.exportStockUpDownInfo(page,pageSize,response);
    }

    /**
     * 统计大盘T日和T-1日每分钟交易量统计
     * @return
     */
    @ApiOperation(value = "统计大盘T日和T-1日每分钟交易量统计", notes = "统计大盘T日和T-1日每分钟交易量统计", httpMethod = "GET")
    @GetMapping("/stock/tradeAmt")
    public R<Map<String ,List>> getCompareStockTradeAmt (){
        return stockService.getCompareStockTradeAmt();
    }

    /**
     * 统计股票各个涨幅区间的数量
     * @return
     */
    @ApiOperation(value = "统计股票各个涨幅区间的数量", notes = "统计股票各个涨幅区间的数量", httpMethod = "GET")
    @GetMapping("/stock/updown")
    public R<Map> getIncreaseRangeInfo(){
        return stockService.getIncreaseRangeInfo();
    }

    /**
     * 获取指定股票T日的分时数据
     * @param stockCode 股票编码
     * @return
     */
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", dataType = "string", name = "code", value = "股票编码", required = true)
    })
    @ApiOperation(value = "获取指定股票T日的分时数据", notes = "获取指定股票T日的分时数据", httpMethod = "GET")
    @GetMapping("/stock/screen/time-sharing")
    public R<List<Stock4MinuteDomain>>  getStockScreenTimeSharing(@RequestParam(value = "code" ,required = true) String stockCode){

    return  stockService.getStockScreenTimeSharing( stockCode);
    }

    /**
     * 单个个股日K线数据查询 ，可以根据时间区间查询数日的K线数据
     * @param stockCode 股票编码
     */
    @ApiOperation(value = "单个个股日K线数据查询", notes = "单个个股日K线数据查询 ，可以根据时间区间查询数日的K线数据", httpMethod = "GET")
    @GetMapping("/stock/screen/dkline")
    public R<List<Stock4EvrDayDomain>> getStockByDayKlin(@RequestParam(value = "code" ,required = true) String stockCode){
        return stockService.getStockByDayKlin(stockCode);
    }

//    /**
//     * 单个个股周K线数据查询 ，可以根据时间区间查询数日的K线数据
//     * @param stockCode 股票编码
//     */
//    @ApiOperation(value = "单个个股周K线数据查询", notes = "单个个股日K线数据查询 ，可以根据时间区间查询数日的K线数据", httpMethod = "GET")
//    @GetMapping("/stock/screen/dkline")
//    public R<List<Stock4EvrDayDomain>> getStockByWeekKlin(@RequestParam(value = "code" ,required = true) String stockCode){
//        return stockService.getStockByDayKlin(stockCode);
//    }

}
