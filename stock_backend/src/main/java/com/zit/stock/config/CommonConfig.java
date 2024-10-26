package com.zit.stock.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.zit.stock.pojo.vo.StockInfoConfig;
import com.zit.stock.utils.IdWorker;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @ClassName: CommonConfig
 * @Description: 定义公共配置类
 * @Author: mianbaoren
 * @Date: 2024/8/29 14:31
 */
@Configuration
@EnableConfigurationProperties({StockInfoConfig.class}) // 开启对象相关配置对象的加载
public class CommonConfig {
//    /**
//     * 定义密码加密、匹配器bean
//     * @return
//     */
//    @Bean
//    public PasswordEncoder passwordEncoder(){
//
//        return  new BCryptPasswordEncoder();
//    }

    //雪花算法，生成iD唯一性
    @Bean
    public IdWorker idWorker(){
        /*
        参数1 ：机器ID
        参数2：机房ID
        机房和机器编号一般由运维人员进行的唯一性规划
         */
        return  new IdWorker(1l,2l);
    }

    /**
     * 统一定义Long序列化转String设置（所有的Long序列化转成String）
     * @return
     */
    @Bean
    public MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter(){
        //构建http信息封装
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        ObjectMapper objectMapper = new ObjectMapper();
        //反序列化忽略位置属性
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false);
        SimpleModule simpleModule = new SimpleModule();
        //Long |long 类型序列化转String
        simpleModule.addSerializer(Long.class, ToStringSerializer.instance);
        simpleModule.addSerializer(Long.TYPE,ToStringSerializer.instance);
        //注册转化器
        objectMapper.registerModule(simpleModule);
        //设置序列化实现
        converter.setObjectMapper(objectMapper);
        return converter;
    }
}
