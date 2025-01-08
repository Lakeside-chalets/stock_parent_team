package com.zit.stock.mq;

import com.github.benmanes.caffeine.cache.Cache;
import com.zit.stock.service.StockService;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName: StockMQMsgListener
 * @Description: 定义股票相关mq消息监听
 * @Author: mianbaoren
 * @Date: 2024/9/6 15:24
 */
@Component
@Slf4j
public class StockMQMsgListener {
    @Autowired
    private Cache<String, Object> caffeineCache;
    @Autowired
    private StockService stockService;


    // ScheduledExecutorService的实例
//    private final ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);
//
//    // 确保在应用程序关闭时正确关闭ScheduledExecutorService
//    @PreDestroy
//    public void shutdown() {
//        scheduledExecutorService.shutdown();
//        try {
//            if (!scheduledExecutorService.awaitTermination(60, TimeUnit.SECONDS)) {
//                scheduledExecutorService.shutdownNow();
//            }
//        } catch (InterruptedException ex) {
//            scheduledExecutorService.shutdownNow();
//            Thread.currentThread().interrupt();
//        }
//    }

    /**
     * 国内大盘的消息监听，更新缓存
     *
     * @param startTime
     */
    @Scheduled(fixedRate = 10000)
    @RabbitListener(queues = "innerMarQueue")
    public void refreshInnerMarInfo(Date startTime) {
        //统计当前时间点和发送消息时间点的差值，如果超过1分钟，则告警
        //获取时间毫秒差值
        long diffTime = DateTime.now().getMillis() - new DateTime(startTime).getMillis();
        if (diffTime > 60000l) {
            log.error("采集国内大盘时间点：{},同步超时：{}ms", new DateTime(startTime).toString("yyyy-MM-dd HH:mm:ss"), diffTime);
        }
        //刷新缓存, //将缓存置为失效删除
        caffeineCache.invalidate("innerMarketKey");
        //调用服务更新缓存
        stockService.getInnerMarketInfo();
    }

    /**
     * 国外大盘的消息监听，更新缓存
     *
     * @param startTime
     */
    @RabbitListener(queues = "outerMarketQueue")
    public void refreshOuterMarketInfo(Date startTime) {
        //统计当前时间点和发送消息时间点的差值，如果超过1分钟，则告警
        //获取时间毫秒差值
        long diffTime = DateTime.now().getMillis() - new DateTime(startTime).getMillis();
        if (diffTime > 60000l) {
            log.error("采集国外大盘时间点：{},同步超时：{}ms", new DateTime(startTime).toString("yyyy-MM-dd HH:mm:ss"), diffTime);
        }
        //刷新缓存, //将缓存置为失效删除
        caffeineCache.invalidate("outerMarketKey");
        //调用服务更新缓存
        stockService.getOutMarketInfo();
    }

    /**
     * 国内板块的消息监听，更新缓存
     *
     * @param startTime
     */
    @RabbitListener(queues = "StockBlockQueue")
    public void refreshStockBlockInfo(Date startTime) {
        //统计当前时间点和发送消息时间点的差值，如果超过1分钟，则告警
        //获取时间毫秒差值
        long diffTime = DateTime.now().getMillis() - new DateTime(startTime).getMillis();
        if (diffTime > 60000l) {
            log.error("采集板块数据时间点：{},同步超时：{}ms", new DateTime(startTime).toString("yyyy-MM-dd HH:mm:ss"), diffTime);
        }
        //刷新缓存, //将缓存置为失效删除
        caffeineCache.invalidate("StockBlockKey");
        //调用服务更新缓存
        stockService.getStockBlockInfo();
    }

    /**
     * 国内个股的消息监听，更新缓存
     *
     * @param startTime
     */
    @Scheduled(fixedRate = 10000)
    @RabbitListener(queues = "StockRtInfoQueue")
    public void refreshStockRtInfoOnBoard(Date startTime) {
        //统计当前时间点和发送消息时间点的差值，如果超过1分钟，则告警
        //获取时间毫秒差值
        long diffTime = DateTime.now().getMillis() - new DateTime(startTime).getMillis();
        if (diffTime > 60000l) {
            log.error("采集个股数据时间点：{},同步超时：{}ms", new DateTime(startTime).toString("yyyy-MM-dd HH:mm:ss"), diffTime);
        }

        // 使用ScheduledExecutorService安排一个延迟任务
//        scheduledExecutorService.schedule(() -> {
            //刷新缓存, //将缓存置为失效删除
            caffeineCache.invalidate("StockRtInfoKey");

            //调用服务更新缓存
            stockService.getStockInfoByPageonBoard();
//        }, 20, TimeUnit.SECONDS);
    }


    /**
     * 国内个股的消息监听，更新缓存
     *
     * @param startTime
     */
    @Scheduled(fixedRate = 10000)
    @RabbitListener(queues = "StockRtInfoQueue")
    public void refreshStockRtInfobyPage(Date startTime) {
        //统计当前时间点和发送消息时间点的差值，如果超过1分钟，则告警
        //获取时间毫秒差值
        long diffTime = DateTime.now().getMillis() - new DateTime(startTime).getMillis();
        if (diffTime > 60000l) {
            log.error("采集个股数据时间点：{},同步超时：{}ms", new DateTime(startTime).toString("yyyy-MM-dd HH:mm:ss"), diffTime);
        }

        // 使用ScheduledExecutorService安排一个延迟任务

        //刷新缓存, //将缓存置为失效删除
        caffeineCache.invalidate("StockRtBypageKey");

        //调用服务更新缓存
        stockService.getStockInfobycache();

    }
}
