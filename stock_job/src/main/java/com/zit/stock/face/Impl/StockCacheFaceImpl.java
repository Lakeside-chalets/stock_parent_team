package com.zit.stock.face.Impl;

import com.zit.stock.face.StockCacheFace;
import com.zit.stock.mapper.StockBusinessMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @ClassName: StockCacheFaceImpl
 * @Description: 定义股票缓存层的实现
 * @Author: mianbaoren
 * @Date: 2024/10/25 23:19
 */
@Component("stockCacheFace")
public class StockCacheFaceImpl implements StockCacheFace {
    @Autowired
    private StockBusinessMapper stockBusinessMapper;

    @Cacheable(cacheNames="stock" ,key = "'stockCodes'")
    @Override
    public List<String> getAllStockCodeWithPredix() {
        //1.获取所有个股的集合 3000+
        List<String> allStockCode = stockBusinessMapper.getAllStockCode();
        //添加大盘前缀 sh sz
        allStockCode = allStockCode.stream().map(code -> code
                        .startsWith("6") ? "sh" + code : "sz" + code)
                        .collect(Collectors.toList());
        return allStockCode;
    }
}
