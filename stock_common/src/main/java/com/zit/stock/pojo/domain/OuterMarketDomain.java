package com.zit.stock.pojo.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @ClassName: OuterMarketDomain
 * @Description: 定义国外大盘数据的领域对象
 * @Author: mianbaoren
 * @Date: 2024/9/9 21:55
 */
@ApiModel(description = ": 定义国外大盘数据的领域对象")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OuterMarketDomain {
    /**
     * 大盘名称
     */
     @ApiModelProperty(value = "大盘名称", position = 1)
     private String name;
    /**
     * 当前大盘点
     */
    @ApiModelProperty(value = "当前大盘点", position = 2)
    private BigDecimal curPoint;
    /**
     * 涨跌值
     */
     @ApiModelProperty(value = "涨跌值", position = 3)
     private BigDecimal upDown;
    /**
     * 涨幅
     */
    @ApiModelProperty(value = "涨幅", position = 4)
    private BigDecimal rose;
    /**
     * 当前时间
     */
     @ApiModelProperty(value = "当前时间", position = 5)
     @JsonFormat(pattern = "yyyy-MM-dd HH:mm",timezone = "Asia/Shanghai")
     private Date curTime;
}
