package com.itheima.stock.mq;

import com.github.benmanes.caffeine.cache.Cache;
import com.itheima.stock.service.StockService;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

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
    private Cache<String,Object> caffeineCache;
    @Autowired
    private StockService stockService;

    @RabbitListener(queues = "innerMarketQueue")
    public  void refreshInnerMarketInfo(Date startTime){
        //统计当前时间点和发送消息时间点的差值，如果超过1分钟，则告警
        //获取时间毫秒差值
        long diffTime= DateTime.now().getMillis()-new DateTime(startTime).getMillis();
        if (diffTime > 60000l) {
            log.error("采集国内大盘时间点：{},同步超时：{}ms",new DateTime(startTime).toString("yyyy-MM-dd HH:mm:ss"),diffTime);
        }
        //刷新缓存, //将缓存置为失效删除
        caffeineCache.invalidate("innerMarketKey");
        //调用服务更新缓存
        stockService.getInnerMarketInfo();
    }

}
