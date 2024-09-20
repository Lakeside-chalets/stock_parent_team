package com.itheima.stock.job;

import com.itheima.stock.service.StockTimerTaskService;
import com.xxl.job.core.handler.annotation.XxlJob;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 定义股票相关数据的定时任务
 * @author laofang
 */
@Component
public class StockJob {

    @Autowired
    private StockTimerTaskService stockTimerTaskService;


    /**
     * 1.简单任务实例（Bean模式）
     */
    @XxlJob("myJobHandler")
    public void demoJobHandler() throws Exception {
        System.out.println("当前时间点为："+ DateTime.now().toString("yyyy-MM-dd HH:mm:ss"));
        // default success
    }

    /**
     * 定时采集A股大盘数据
     * 建议针对不同的股票数据，定义不同的采集任务，解耦，方便维护
     */
    @XxlJob("getInnerMarketInfo")
    public void getStockInfos(){
        stockTimerTaskService.getInnerMarketInfo();
    }

    /**
     * 定时采集A股个股数据
     */
    @XxlJob("getStockRtIndex")
    public void getStockRtIndex(){
        stockTimerTaskService.getStockRtIndex();
    }
    /**
     * 板块定时任务
     */
    @XxlJob("getStockBlockRtIndex")
    public void getStockBlockRtIndex(){
        stockTimerTaskService.getStockBlockRtIndex();
    }

    /**
     * 定时采集国外大盘数据
     */
    @XxlJob("getStockOuterMarketInfo")
    public void getStockOuterMarketInfo(){
        stockTimerTaskService.getStockOuterMarketInfo();
    }
}