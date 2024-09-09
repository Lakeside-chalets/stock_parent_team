package com.itheima.stock.mapper;

import com.itheima.stock.pojo.domain.Stock4EvrDayDomain;
import com.itheima.stock.pojo.domain.Stock4MinuteDomain;
import com.itheima.stock.pojo.domain.StockUpdownDomain;
import com.itheima.stock.pojo.entity.StockRtInfo;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
* @author mianbaoren
* @description 针对表【stock_rt_info(个股详情信息表)】的数据库操作Mapper
* @createDate 2024-08-26 22:52:27
* @Entity com.itheima.stock.pojo.entity.StockRtInfo
*/
public interface StockRtInfoMapper {

    int deleteByPrimaryKey(Long id);

    int insert(StockRtInfo record);

    int insertSelective(StockRtInfo record);

    StockRtInfo selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(StockRtInfo record);

    int updateByPrimaryKey(StockRtInfo record);
    /**
     * 分页查询最新的股票交易数据
     * @return
     */
    List<StockUpdownDomain> getStockByTime(@Param("curDate") Date curDate);

    /**
     * 公布最新的涨幅榜数据
     * @return
     */
    List<StockUpdownDomain> getStockByTimeonBoard(@Param("curDate") Date curDate);

    /**
     * 统计最新股票日内每分钟涨跌停数量
     * @param openDate 开始时间，一般指开盘时间
     * @param endDate  截止时间
     * @param flag 1代表涨停，0代表跌停
     * @return
     */
    List<Map> getUpDownCount(@Param("openDate") Date openDate, @Param("endDate") Date endDate, @Param("flag") int flag);

    /**
     * 获取最新时间下的涨跌幅的数量
     * @param curDate 当前时间
     * @return
     */
    List<Map> getIncreaseRangeInfoByDate(@Param("curDate") Date curDate);

    /**
     *根据股票编码，查询指定时间下的分时数据
     * @param openDate 开盘时间
     * @param enDate 截止时间，一般和开盘时间同一天
     * @param stockCode 股票编码
     * @return
     */
    List<Stock4MinuteDomain> getStock4MinuteInfo(@Param("openDate") Date openDate, @Param("enDate") Date enDate, @Param("stockCode") String stockCode);

    /**
     * 获取指定日期范围内的收盘日期
     * @param stockCode 股票编码
     * @param startDate 起始时间
     * @param endDate 结束时间
     * @return
     */
    List<Date> getCloseDates(@Param("stockCode") String stockCode,
                             @Param("startDate") Date startDate,
                             @Param("endDate") Date endDate);
    /**
     * 获取指定股票在指定日期点下的数据
     * @param stockCode 股票编码
     * @param dates 指定日期集合
     * @return
     */
    List<Stock4EvrDayDomain> getStockByDayKlin(@Param("stockCode") String stockCode,
                                                     @Param("dates") List<Date> dates);
    /**
     * 批量插入个股数据
     * @param list
     * @return
     */
    int insertBatch(@Param("list") List<StockRtInfo> list);

}
