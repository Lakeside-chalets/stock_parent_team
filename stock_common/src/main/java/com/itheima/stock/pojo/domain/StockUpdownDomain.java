package com.itheima.stock.pojo.domain;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
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
 * @Description 股票涨跌信息
 */
@ApiModel(description = "股票涨跌信息")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StockUpdownDomain {
    /**
     * 股票编码
     */
    @ApiModelProperty(value = "股票编码", position = 1)
    @ExcelProperty(value = {"股票涨幅信息统计表","股票编码"},index = 0)
    private String code;
    /**
     * 股票名称
     */
    @ApiModelProperty(value = "股票名称", position = 2)
    @ExcelProperty(value = {"股票涨幅信息统计表","股票名称"},index = 1)
    private String name;
    /**
     * 前收盘价
     */
    @ApiModelProperty(value = "前收盘价", position = 3)
    @ExcelProperty(value = {"股票涨幅信息统计表","前收盘价格"},index = 2)
    private BigDecimal preClosePrice;
    /**
     * 当前价格
     */
    @ApiModelProperty(value = "当前价格", position = 4)
    @ExcelProperty(value = {"股票涨幅信息统计表","当前价格"},index= 3)
    private BigDecimal tradePrice;
    /**
     * 涨跌值
     */
    @ApiModelProperty(value = "涨跌值", position = 5)
    @ExcelProperty(value = {"股票涨幅信息统计表","涨跌值"},index= 4)
    private BigDecimal increase;
    /**
     * 涨幅
     */
    @ApiModelProperty(value = "涨幅", position = 6)
    @ExcelProperty(value = {"股票涨幅信息统计表","涨幅"},index= 5)
    private BigDecimal upDown;
    /**
     * 振幅
     */
    @ApiModelProperty(value = "振幅", position = 7)
    @ExcelProperty(value = {"股票涨幅信息统计表","振幅"},index= 6)
    private BigDecimal amplitude;
    /**
     * 交易量
     */
    @ApiModelProperty(value = "交易量", position = 8)
    @ExcelProperty(value = {"股票涨幅信息统计表","交易总量"},index = 7)
    private Long tradeAmt;
    /**
     * 交易金额
     */
    @ApiModelProperty(value = "交易金额", position = 9)
    @ExcelProperty(value = {"股票涨幅信息统计表","交易总金额"},index = 8)
    private BigDecimal tradeVol;
    /**
     * 当前日期
     */
    @ApiModelProperty(value = "当前日期", position = 10)
    @ExcelProperty(value = {"股票涨幅信息统计表","日期"},index = 9)
    @DateTimeFormat("yyy-MM-dd HH:mm")//easyExcel的注解-》excel
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")//springmvc支持的注解-》json格式数据
    private Date curDate;
}