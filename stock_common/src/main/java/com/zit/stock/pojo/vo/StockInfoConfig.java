package com.zit.stock.pojo.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * @ClassName: StockInfoConfig
 * @Description: 定义股票相关的值对象封装
 * @Author: mianbaoren
 * @Date: 2024/8/30 21:41
 */
@ApiModel(description = ": 定义股票相关的值对象封装")
@Data
@ConfigurationProperties(prefix = "stock")
//@Component //作为配置类一直开启，谁都可以引用浪费资源
public class StockInfoConfig {
    /**
     * 封装国内A股大盘编码集合
     */
    @ApiModelProperty(value = "封装国内A股大盘编码集合", position = 1)
    private List<String> inner;
    /**
     * 外盘编码集合
     */
    @ApiModelProperty(value = "外盘编码集合", position = 2)
    private List<String> outer;
    /**
     * 股票涨幅区间标题集合
     */
    @ApiModelProperty(value = "股票涨幅区间标题集合", position = 3)
    private List<String> upDownRange;

    /**
     * 大盘参数获取url
     */
    @ApiModelProperty(value = "大盘参数获取url", position = 4)
    private String marketUrl;
    /**
     * 板块参数获取url
     */
    @ApiModelProperty(value = "板块参数获取url", position = 5)
    private String blockUrl;
}
