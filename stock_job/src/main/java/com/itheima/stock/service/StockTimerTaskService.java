package com.itheima.stock.service;

import io.swagger.annotations.ApiModel;

/**
 * @ClassName: StockTimerTaskService
 * @Description: 定义股票采集数据服务接口
 * @Author: mianbaoren
 * @Date: 2024/9/5 17:36
 */
@ApiModel(description = ": 定义股票采集数据服务接口")
public interface StockTimerTaskService {
    /**
     * 获取国内大盘的实时数据信息
     */
    void getInnerMarketInfo();

    /**
     * 定义获取分钟级股票数据
     */
    void getStockRtIndex();

    /**
     * 获取板块数据
     */
    void getStockBlockRtIndex();

}
