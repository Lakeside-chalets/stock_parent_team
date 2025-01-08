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
 * @ClassName: StockBlockDomain
 * @Description: 股票板块数据的领域对象
 * @Author: mianbaoren
 * @Date: 2024/8/31 14:54
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

@ApiModel(description = ": 股票板块数据的领域对象")
public class StockBlockDomain {
    /**
     * 公司数量
     */
    @ApiModelProperty(value = "公司数量", position = 1)
    private Integer companyNum;
    /**
     * 交易量
     */
    @ApiModelProperty(value = "交易量", position = 2)
    private Long tradeAmt;
    /**
     * 板块编码
     */
    @ApiModelProperty(value = "板块编码", position = 3)
    private String code;
    /**
     * 平均价格
     */
    @ApiModelProperty(value = "平均价格", position = 4)
    private BigDecimal avgPrice;
    /**
     * 板块名称
     */
    @ApiModelProperty(value = "板块名称", position = 5)
    private String name;
    /**
     * 当前时间
     */
    @ApiModelProperty(value = "当前时间", position = 6)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm" ,timezone = "Asia/Shanghai")
    private Date curDate;
    /**
     * 交易总金额
     */
    @ApiModelProperty(value = "交易总金额", position = 7)
    private BigDecimal tradeVol;
    /**
     * 涨幅
     */
    @ApiModelProperty(value = "涨幅", position = 8)
    private BigDecimal updownRate;
}