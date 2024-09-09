package com.itheima.stock.mapper;

import com.itheima.stock.pojo.domain.StockBlockDomain;
import com.itheima.stock.pojo.entity.StockBlockRtInfo;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
* @author mianbaoren
* @description 针对表【stock_block_rt_info(股票板块详情信息表)】的数据库操作Mapper
* @createDate 2024-08-26 22:52:27
* @Entity com.itheima.stock.pojo.entity.StockBlockRtInfo
*/
public interface StockBlockRtInfoMapper {

    int deleteByPrimaryKey(Long id);

    int insert(StockBlockRtInfo record);

    int insertSelective(StockBlockRtInfo record);

    StockBlockRtInfo selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(StockBlockRtInfo record);

    int updateByPrimaryKey(StockBlockRtInfo record);

    /**
     * 根据指定时间点查询板块对应的数据
     * @param curDate 当前时间
     * @return
     */
    List<StockBlockDomain> getBlockInfo(@Param("curDate") Date curDate);

    /**
     * 板块信息批量插入
     * @param list
     * @return
     */
    int insertBatch(@Param("list") List<StockBlockRtInfo> list);


}
