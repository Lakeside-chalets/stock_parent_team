package com.zit.stock.service;

import com.zit.stock.pojo.domain.*;
import com.zit.stock.vo.resp.PageResult;
import com.zit.stock.vo.resp.R;
import org.apache.ibatis.annotations.Param;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * @ClassName: StockService
 * @Description: 股票服务接口
 * @Author: mianbaoren
 * @Date: 2024/8/30 22:26
 */
public interface StockService {
    /**
     * 获取国内大盘最新的数据
     * @return
     */
    R<List<InnerMarketDomain>> getInnerMarketInfo();
    /**
     * 获取国内板块的最新数据
     * @return
     */
    R<List<StockBlockDomain>> getStockBlockInfo();

    /**
     * 分页的准备数据
     * @return
     */
    List<StockUpdownDomain> getStockInfobycache();
    /**
     * 分页查询最新的股票交易数据
     * @param page 当前页
     * @param pageSize 每页的大小
     * @return
     */
    R<PageResult<StockUpdownDomain>> getStockInfoByPage(Integer page, Integer pageSize);

    /**
     * 公布最新的涨幅榜数据
     * @return
     */
    R<List<StockUpdownDomain>> getStockInfoByPageonBoard();
    /**
     * 统计最新股票日内每分钟涨跌停数量
     * @return
     */
    R<Map<String, List>> getStockUpDownCount();

    /**
     * 导出指定页码的最新股票信息
     * @param page 当前页
     * @param pageSize 每页的大小
     * @param response 响应
     */
    void exportStockUpDownInfo(Integer page, Integer pageSize, HttpServletResponse response);

    /**
     * 统计大盘T日和T-1日每分钟交易量统计
     * @return
     */
    R<Map<String, List>> getCompareStockTradeAmt();

    /**
     * 统计股票各个涨幅区间的数量
     * @return
     */
    R<Map> getIncreaseRangeInfo();
    /**
     * 获取指定股票T日的分时数据
     * @param stockCode 股票编码
     * @return
     */
    R<List<Stock4MinuteDomain>> getStockScreenTimeSharing(@Param("stockCode") String stockCode);
    /**
     * 单个个股日K线数据查询 ，可以根据时间区间查询数日的K线数据 (嵌套查询表查询会产生性能的问题)
     * @param stockCode 股票编码
     */
    R<List<Stock4EvrDayDomain>> getStockByDayKlin(@Param("stockCode") String stockCode);

    /**
     * 单个个股周K线数据查询
     * @param code 股票编码
     */
//    R<List<Map<String, Object>>> getStockByWeeKline(@Param("code") String code);
    R<List<Stock4EveryWeekDomain>> stockScreenWeekKLine(@Param("code") String code);

    /**
     * 获取国外大盘的最新数据，按降序显示前四条
     * @return
     */
//    R<List<OuterMarketDomain>> getOutMarketInfo();
    R<List<OuterMarketDomain>> getOutMarketInfo();
    /**
     * 根据个股编码模糊查询股票信息
     * @param searchStr
     * @return
     */
    R<List<Map>> getStockNameAndCode(@Param("searchStr") String searchStr);



    /**
     * 个股主营业务查询股票信息
     * @param code 股票编码
     * @return
     */
    R<StockBusinessDomain> getBusinessInfoByCode(@Param("code") String code);

    /**
     * 获取最新的行情数据到面板上
     * @param code 股票编码
     * @return
     */
    R<StockNewPriceDomain> getStockNewPriceInfo(String code);

    /**
     * 个股交易流水行情数据显示，按照交易时间降序取前10
     * @param code
     * @return
     */
    R<List<StockNewTransactionDomain>> getstockTradingOnScreen(@Param("code") String code);



}
