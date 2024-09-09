package com.itheima.stock;

import com.itheima.stock.service.StockTimerTaskService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Service;

/**
 * @ClassName: TestRest
 * @Description: TODO
 * @Author: mianbaoren
 * @Date: 2024/9/5 21:28
 */
@SpringBootTest
public class TestRest {
    @Autowired
    private StockTimerTaskService stockTimerTaskService;

    /**
     * @desc 测试采集国内大盘数据
     */
    @Test
    public void testGetMarketInfo() throws InterruptedException {
//        stockTimerTaskService.getInnerMarketInfo();
        stockTimerTaskService.getStockRtIndex();
        //目的：让主线程休眠，等待子线程执行完任务
        Thread.sleep(5000);
//        stockTimerTaskService.getStockBlockRtIndex();
    }


}
