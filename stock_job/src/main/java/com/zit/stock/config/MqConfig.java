package com.zit.stock.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;

@Configuration
public class MqConfig {
    /**
     * 重新定义消息序列化的方式，改为基于json格式序列化和反序列化(避免jdk消息体积多大）
     * @return
     */
    @Bean
    public MessageConverter messageConverter(){
        return new Jackson2JsonMessageConverter();
    }

    /**
     * 国内大盘信息队列
     * @return
     */
    @Bean
    public Queue innerMarQueue(){
        return new Queue("innerMarQueue",true);
    }

    /**
     * 定义路由股票信息的交换机
     * @return
     */
    @Bean
    public TopicExchange innerMarTopicExchange(){
        return new TopicExchange("stockExchange",true,false);
    }

    /**
     * 绑定国内大盘队列到指定交换机
     * @return
     */
    @Bean
    public Binding bindingInnerMarketExchange(){
        return BindingBuilder.bind(innerMarQueue()).to(innerMarTopicExchange())
                .with("inner.mar");
    }


    /**
     * 国外大盘信息队列
     * @return
     */
    @Bean
    public Queue outerMarketQueue(){
        return new Queue("outerMarketQueue",true);
    }

    /**
     * 定义路由国外股票信息的交换机
     * @return
     */
    @Bean
    public TopicExchange outerMarketTopicExchange(){
        return new TopicExchange("stockExchange",true,false);
    }

    /**
     * 绑定国外大盘队列到指定交换机
     * @return
     */
    @Bean
    public Binding bindingouterMarketExchange(){
        return BindingBuilder.bind(outerMarketQueue()).to(outerMarketTopicExchange())
                .with("outer.market");
    }

    /**
     * 个股信息队列
     * @return
     */
    @Bean
    public Queue StockRtInfoQueue(){
        return new Queue("StockRtInfoQueue",true);
    }

    /**
     * 定义路由个股股票信息的交换机
     * @return
     */
    @Bean
    public TopicExchange StockRtInfoTopicExchange(){
        return new TopicExchange("stockExchange",true,false);
    }

    /**
     * 绑定个股股票队列到指定交换机
     * @return
     */
    @Bean
    public Binding bindingStockRtInfoExchange(){
        return BindingBuilder.bind(StockRtInfoQueue()).to(StockRtInfoTopicExchange())
                .with("Stock.rtInfo");
    }

    /**
     * 板块信息队列
     * @return
     */
    @Bean
    public Queue StockBlockQueue(){
        return new Queue("StockBlockQueue",true);
    }

    /**
     * 定义路由个股股票信息的交换机
     * @return
     */
    @Bean
    public TopicExchange StockBlockTopicExchange(){
        return new TopicExchange("stockExchange",true,false);
    }

    /**
     * 绑定个股股票队列到指定交换机
     * @return
     */
    @Bean
    public Binding bindingStockBlockExchange(){
        return BindingBuilder.bind(StockBlockQueue()).to(StockBlockTopicExchange())
                .with("Stock.Block");
    }
}

