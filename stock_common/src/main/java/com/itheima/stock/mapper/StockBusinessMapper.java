package com.itheima.stock.mapper;

import com.itheima.stock.pojo.entity.StockBusiness;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
* @author mianbaoren
* @description 针对表【stock_business(主营业务表)】的数据库操作Mapper
* @createDate 2024-08-26 22:52:27
* @Entity com.itheima.stock.pojo.entity.StockBusiness
*/
public interface StockBusinessMapper {

    int deleteByPrimaryKey(Long id);

    int insert(StockBusiness record);

    int insertSelective(StockBusiness record);

    StockBusiness selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(StockBusiness record);

    int updateByPrimaryKey(StockBusiness record);

    /**
     * 获取所有A股个股的编码集合
     * @return
     */
    List<String> getAllStockCode();

    /**
     * 个股主营业务查询股票信息
     * @param code 股票编码
     * @return
     */
    Map<String, Object> getBusinessInfoByCode(@Param("code") String code);
}
