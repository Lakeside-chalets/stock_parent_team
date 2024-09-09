package com.itheima.stock;

import com.google.common.collect.Lists;
import com.itheima.stock.mapper.StockBusinessMapper;
import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @ClassName: TestMapper
 * @Description: TODO
 * @Author: mianbaoren
 * @Date: 2024/9/5 23:18
 */
@SpringBootTest
public class TestMapper {
    @Autowired
    private StockBusinessMapper stockBusinessMapper;

    /**
     * 测试所有个股编码集合
     */
    @Test
    public void test01(){
        List<String> allStockCode = stockBusinessMapper.getAllStockCode();
        //添加大盘前缀 sh sz
        allStockCode =  allStockCode.stream().map(code->code.startsWith("6")?"sh"+code:"sz"+code).collect(Collectors.toList());
        System.out.println(allStockCode);
        //将所有股票编码组成的大的集合拆分成若干个小的集合
        Lists.partition(allStockCode,15).forEach(ids->{
            System.out.println(ids);
        });
    }
}
