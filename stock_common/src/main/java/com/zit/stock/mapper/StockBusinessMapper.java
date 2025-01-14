package com.zit.stock.mapper;

import com.zit.stock.pojo.domain.StockBusinessDomain;
import com.zit.stock.pojo.entity.StockBusiness;
import io.swagger.annotations.ApiModel;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
* @author mianbaoren
* @description 针对表【stock_business(主营业务表)】的数据库操作Mapper
* @createDate 2024-08-26 22:52:27
* @Entity com.zit.stock.pojo.entity.StockBusiness
*/
@ApiModel(description = "针对表【stock_business(主营业务表)】的数据库操作Mapper")
public interface StockBusinessMapper {

    int deleteByPrimaryKey(String id);

    int insert(StockBusiness record);

    int insertSelective(StockBusiness record);

    StockBusiness selectByPrimaryKey(String id);

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
    StockBusinessDomain getBusinessInfoByCode(@Param("code") String code);


    /**
     * 根据模糊字符串查询股票名字和编码
     * @param searchStr
     * @return
     */
    List<Map> getStockNameAndCodeByStr(@Param("searchStr") String searchStr);
}
