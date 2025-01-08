package com.zit.stock.mapper;

import com.zit.stock.pojo.domain.OuterMarketDomain;
import com.zit.stock.pojo.entity.StockOuterMarketIndexInfo;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
* @author mianbaoren
* @description 针对表【stock_outer_market_index_info(外盘详情信息表)】的数据库操作Mapper
* @createDate 2024-08-26 22:52:27
* @Entity com.itheima.stock.pojo.entity.StockOuterMarketIndexInfo
*/
public interface StockOuterMarketIndexInfoMapper {

    int deleteByPrimaryKey(Long id);

    int insert(StockOuterMarketIndexInfo record);

    int insertSelective(StockOuterMarketIndexInfo record);

    StockOuterMarketIndexInfo selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(StockOuterMarketIndexInfo record);

    int updateByPrimaryKey(StockOuterMarketIndexInfo record);

    /**
     *获取国外大盘的最新数据，(按降序显示前四条)
//     * @param curDate 当前时间
//     * @param outerMcodes 外盘编码
     * @return
     */
    List<OuterMarketDomain> getOutMarketInfo(@Param("curTime") Date curDate, @Param("outerMcodes") List<String> outerMcodes);
    List<OuterMarketDomain> getOutMarketInfoByDate();
    /**
     * 批量插入国外大盘数据
     * @param entities
     * @return
     */
    int insertBatch(@Param("infos") List<StockOuterMarketIndexInfo> entities);
}
