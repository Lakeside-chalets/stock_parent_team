package com.itheima.stock.config;


import com.itheima.stock.pojo.vo.StockInfoConfig;
import com.itheima.stock.utils.IdWorker;
import com.itheima.stock.utils.ParserStockInfoUtil;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
/**
 * @ClassName: CommonConfig
 * @Description: 定义公共配置类
 * @Author: mianbaoren
 * @Date: 2024/8/29 14:31
 */

@Configuration
@EnableConfigurationProperties(StockInfoConfig.class) //开启对象相关配置对象的加载
public class CommonConfig {
    /**
     * 配置基于雪花算法生成全局唯一id
     *   参与元算的参数： 时间戳 + 机房id + 机器id + 序列号
     *   保证id唯一
     * @return
     */
    @Bean
    public IdWorker idWorker(){
        //指定当前为1号机房的2号机器生成
        return new IdWorker(2L,1L);
    }

    /**
     * 定义解析股票 大盘，外股 个股 板块相关信息的工具类Bean
     * @return
     */
    @Bean
    public ParserStockInfoUtil parserStockInfoUtil(IdWorker idWorker){
        return new ParserStockInfoUtil(idWorker);
    }
}  