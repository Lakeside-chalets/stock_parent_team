package com.zit.stock.face;

import java.util.List;

/**
 * @ClassName: StockCacheFace
 * @Description: 定义股票缓存层的接口
 * @Author: mianbaoren
 * @Date: 2024/10/25 23:18
 */
public interface StockCacheFace {

    /**
     * 获取所有股票编码，并添加上证或者深证的股票前缀编号：sh sz
     * @return
     */
    List<String> getAllStockCodeWithPredix();

}
