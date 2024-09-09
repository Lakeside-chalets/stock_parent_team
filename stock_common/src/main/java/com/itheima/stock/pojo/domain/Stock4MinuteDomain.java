package com.itheima.stock.pojo.domain;

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
 * @author by itheima
 * @Date 2022/2/28
 * @Description 个股分时数据封装
 */
@ApiModel(description = "个股分时数据封装")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Stock4MinuteDomain {
    /**
     * 日期，eg:202201280809
     */
   @ApiModelProperty(value = "日期，eg:202201280809", position = 1)
   @JsonFormat(pattern = "yyyy-MM-dd HH:mm",timezone = "Asia/Shanghai")
   private Date date;
    /**
     * 交易量
     */
   @ApiModelProperty(value = "交易量", position = 2)
   private Long tradeAmt;
    /**
     * 股票编码
     */
   @ApiModelProperty(value = "股票编码", position = 3)
   private String code;
    /**
     * 最低价
     */
   @ApiModelProperty(value = "最低价", position = 4)
   private BigDecimal lowPrice;
    /**
     * 前收盘价
     */
   @ApiModelProperty(value = "前收盘价", position = 5)
   private BigDecimal preClosePrice;
    /**
     * 股票名称
     */
   @ApiModelProperty(value = "股票名称", position = 6)
   private String name;
    /**
     * 最高价
     */
   @ApiModelProperty(value = "最高价", position = 7)
   private BigDecimal highPrice;
    /**
     * 开盘价
     */
   @ApiModelProperty(value = "开盘价", position = 8)
   private BigDecimal openPrice;

    /**
     * 当前交易总金额
     */
   @ApiModelProperty(value = "当前交易总金额", position = 9)
   private BigDecimal tradeVol;
    /**
     * 当前价格
     */
   @ApiModelProperty(value = "当前价格", position = 10)
   private BigDecimal tradePrice;
}