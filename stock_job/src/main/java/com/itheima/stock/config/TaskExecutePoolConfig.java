package com.itheima.stock.config;

import com.itheima.stock.pojo.vo.TaskThreadPoolInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * @ClassName: TaskExecutePoolConfig
 * @Description: 定义线程池的配置类
 * @Author: mianbaoren
 * @Date: 2024/9/7 23:47
 */

@Configuration
public class TaskExecutePoolConfig {
    @Autowired
    private TaskThreadPoolInfo info;

    /**
     * 构建线程池的Bean对象
     * @return
     */
    @Bean(name = "threadPoolTaskExecutor",destroyMethod = "shutdown")
    public ThreadPoolTaskExecutor threadPoolTaskExecutor(){
        //new一个ThreadPoolTaskExecutor对象
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        //1.设置核心线程数
        executor.setCorePoolSize(info.getCorePoolSize());
        //2.设置最大线程数
        executor.setMaxPoolSize(info.getMaxPoolSize());
        //3.设置空闲线程最大存活时间
        executor.setKeepAliveSeconds(info.getKeepAliveSeconds());
        //4.设置任务对列长度
        executor.setQueueCapacity(info.getQueueCapacity());
        //设置拒绝策略
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy());
       //将设置参数初始化
        executor.initialize();
        return executor;
    }
}
