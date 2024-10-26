package com.zit.stock.mapper;

import com.zit.stock.pojo.domain.*;
import com.zit.stock.pojo.entity.StockRtInfo;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
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
//日K线
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
//周K线

    /**
     * 查询股票每周的基础数据，包含每周的开盘时间点和收盘时间点
     * 但是不包含开盘价格和收盘价格
     * @param code 股票编码
     * @param startDate 开始时间
     * @param endDate 结束时间
     * @return
     */
    List<Stock4EveryWeekDomain> getStockWeekKLineByCode(@Param("code") String code, @Param("startDate") Date startDate, @Param("endDate") Date endDate);

    /**
     * 查询指定股票在指定时间下的数据
     * @param code
     * @param times
     * @return
     *  map接口：
     *      openPrice:xxx
     *      closePrice:xxx
     */
    List<BigDecimal> getStockInfoByCodeAndTimes(@Param("code") String code, @Param("times") List<Date> times);

//
//    /**
//     * 获取指定股票在指定周期下的数据
//     * @param code 股票编码
//     * @param dates 指定日期集合
//     * @return
//     */
//    List<Map<String, Object>> getStockByWeekline(@Param("code") String code, @Param("dates") List<Date> dates);


    /**
     * 批量插入个股数据
     * @param list
     * @return
     */
    int insertBatch(@Param("list") List<StockRtInfo> list);



    /**
     * 获取最新的行情数据到面板上
     * @param code 股票编码
     * @param endTime 截止时间
     * @return
     */
    StockNewPriceDomain getStockNewPriceByCode(@Param("code") String code, @Param("endTime") Date endTime);

    /**
     * 个股交易流水行情数据显示
     * @param code
     * @return
     */
   List<StockNewTransactionDomain> getstockTradingOnScreen(@Param("code") String code);



}
