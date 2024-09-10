package com.itheima.stock.service.impl;

import com.alibaba.excel.EasyExcel;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.itheima.stock.mapper.StockBlockRtInfoMapper;
import com.itheima.stock.mapper.StockMarketIndexInfoMapper;
import com.itheima.stock.mapper.StockOuterMarketIndexInfoMapper;
import com.itheima.stock.mapper.StockRtInfoMapper;
import com.itheima.stock.pojo.domain.*;
import com.itheima.stock.pojo.vo.StockInfoConfig;
import com.itheima.stock.service.StockService;
import com.itheima.stock.utils.DateTimeUtil;
import com.itheima.stock.vo.resp.PageResult;
import com.itheima.stock.vo.resp.R;
import com.itheima.stock.vo.resp.ResponseCode;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.*;
import java.util.stream.Collectors;

import static com.itheima.stock.vo.resp.ResponseCode.NO_RESPONSE_DATA;

/**
 * @ClassName: StockServiceImpl
 * @Description: 股票服务实现
 * @Author: mianbaoren
 * @Date: 2024/8/30 22:31
 */
@Service("stockService")
@Slf4j
public class StockServiceImpl implements StockService {
    @Autowired
    private StockInfoConfig stockInfoConfig;
    @Autowired
    private StockMarketIndexInfoMapper stockMarketIndexInfoMapper;
    @Autowired
    private StockBlockRtInfoMapper stockBlockRtInfoMapper;
    @Autowired
    private StockRtInfoMapper stockRtInfoMapper;
    @Autowired
    private Cache<String,Object> caffeineCache;
    @Autowired
    private StockOuterMarketIndexInfoMapper stockOuterMarketIndexInfoMapper;
    /**
     * 获取国内大盘最新的数据
     * @return
     */
    @Override
    public R<List<InnerMarketDomain>> getInnerMarketInfo() {
        //默认从本地缓存加载数据，如果不存在则从数据库加载并同步到本地缓存
            //在开盘周期内，本地缓存默认有效期为1分钟
        R<List<InnerMarketDomain>> result = (R<List<InnerMarketDomain>>) caffeineCache.get("innerMarketKey", key -> {
            //        //1.获取股票的最新交易时间点（精确到分钟，秒和毫秒置为0）
//        DateTime curDateTime = DateTimeUtil.getLastDate4Stock(DateTime.now());
//        //将第三方的时间转换成jdk的日期时间
//        Date curDate = curDateTime.toDate();
            DateTime dateTime = DateTimeUtil.getLastDate4Stock(DateTime.now());
            //TODO mock测试数据，后期数据通过第三方接口动态获取实时数据 可删除
            dateTime=DateTime.parse("2022-07-07 14:52:00", DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss"));
            Date curDate = dateTime.toDate();
            //2.获取大盘编码集合
            List<String> mCodes = stockInfoConfig.getInner();
            //3.调用mapper查询数据
            List<InnerMarketDomain>  data = stockMarketIndexInfoMapper.getMarketInfo(curDate,mCodes);
            //4.封装并响应
            return R.ok(data);
        });
        return result;

    }
    /**
     * 获取国内板块的最新数据
     * @return
     */
    @Override
    public R<List<StockBlockDomain>> getStockBlockInfo() {
        //1.获取最新的交易时间(利用第三方时间工具类，并用toDate转换成jdk时间）
        DateTime dateTime = DateTimeUtil.getLastDate4Stock(DateTime.now());
        //TODO mock测试数据，后期数据通过第三方接口动态获取实时数据 可删除
        dateTime=DateTime.parse("2021-12-27 14:30:00", DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss"));
        Date curDate = dateTime.toDate();
        //调用mapper查询数据
        List<StockBlockDomain> data =  stockBlockRtInfoMapper.getBlockInfo(curDate);
        return R.ok(data);
    }

    /**
     * 分页查询最新的股票交易数据
     * @param page 当前页
     * @param pageSize 每页的大小
     * @return
     */
    @Override
    public R<PageResult<StockUpdownDomain>> getStockInfoByPage(Integer page, Integer pageSize) {
        //1.获取最新的交易时间(利用第三方时间工具类，并用toDate转换成jdk时间）
        DateTime dateTime = DateTimeUtil.getLastDate4Stock(DateTime.now());
        //TODO mock测试数据，后期数据通过第三方接口动态获取实时数据 可删除
        dateTime=DateTime.parse("2021-12-30 09:42:00", DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss"));
        Date curDate = dateTime.toDate();
        //2.设置pageHelper分页参数
        PageHelper.startPage(page,pageSize);
        //3.调用mapper查询数据
        List<StockUpdownDomain> pageData = stockRtInfoMapper.getStockByTime(curDate);
        //4.组装pageResult对象
//        PageInfo<StockUpdownDomain> pageInfo = new PageInfo<>(pageData);
//        PageResult<StockUpdownDomain> pageResult = new PageResult<>(pageInfo);
        PageResult<StockUpdownDomain> pageResult = new PageResult<>(new PageInfo<>(pageData));
        //5.响应数据
        return R.ok(pageResult);
    }

    /**
     * 公布最新的涨幅榜数据
     * @return
     */
    @Override
    public R<List<StockUpdownDomain>> getStockInfoByPageonBoard() {
        //1.获取最新的交易时间(利用第三方时间工具类，并用toDate转换成jdk时间）
        DateTime dateTime = DateTimeUtil.getLastDate4Stock(DateTime.now());
        //TODO mock测试数据，后期数据通过第三方接口动态获取实时数据 可删除
        dateTime=DateTime.parse("2021-12-30 09:42:00", DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss"));
        Date curDate = dateTime.toDate();
        //3.调用mapper查询数据
        List<StockUpdownDomain> data = stockRtInfoMapper.getStockByTimeonBoard(curDate);
        //4.响应数据
        return R.ok(data);
    }

    /**
     * 统计最新股票日内每分钟涨跌停数量
     * @return
     */
    @Override
    public R<Map<String, List>> getStockUpDownCount() {
        //1.获取最新的交易时间(利用第三方时间工具类，并用toDate转换成jdk时间）
        DateTime dateTime = DateTimeUtil.getLastDate4Stock(DateTime.now());
        //TODO mock测试数据，后期数据通过第三方接口动态获取实时数据 可删除
        dateTime=DateTime.parse("2021-12-30 15:01:00", DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss"));
        Date endDate = dateTime.toDate();
        //2.获取最新交易时间点所对应的开盘时间点
        Date openDate = DateTimeUtil.getOpenDate(dateTime).toDate();
        //3.统计涨停数据
        List<Map> upList =  stockRtInfoMapper.getUpDownCount(openDate,endDate,1);
        //4.统计跌停数据
        List<Map> downList =  stockRtInfoMapper.getUpDownCount(openDate,endDate,0);
        //5.组装数据
        HashMap<String, List> data = new HashMap<>();
        data.put("upList",upList);
        data.put("downList",downList);
        //6.响应数据
        return R.ok(data);
    }

    /**
     * 导出指定页码的最新股票信息
     * @param page 当前页
     * @param pageSize 每页的大小
     * @param response 响应
     */
    @Override
    public void exportStockUpDownInfo(Integer page, Integer pageSize, HttpServletResponse response) {
        //1.获取分页数据
        R<PageResult<StockUpdownDomain>> r = this.getStockInfoByPage(page, pageSize);
         List<StockUpdownDomain> rows = r.getData().getRows();
        //2.将数据导出到excel中
        // 这里注意 有同学反应使用swagger 会导致各种问题，请直接用浏览器或者用postman
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        try {
            // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
            String fileName = URLEncoder.encode("股票信息表", "UTF-8");
            response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
            EasyExcel.write(response.getOutputStream(), StockUpdownDomain.class).sheet("股票涨幅信息").doWrite(rows);
        } catch (IOException e) {
            log.error("当前页码：{},每页大小:{} ,异常信息：{}", page, pageSize, DateTime.now().toString("yyyy-MM-dd HH:mm:ss"), e.getMessage());
            //给前端错误响应
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            R<Object> error = R.error(ResponseCode.DATA_ERROR);
            try {
                String jsonData = new ObjectMapper().writeValueAsString(error);
                response.getWriter().write(jsonData);
            } catch (IOException ioException) {
               log.error("exportStockUpDownInfo响应错误信息失败,时间：{}",DateTime.now().toString("yyyy-MM-dd HH:mm:ss"));
            }
        }


    }

    /**
     * 统计大盘T日和T-1日每分钟交易量统计
     * @return
     */
    @Override
    public R<Map<String, List>> getCompareStockTradeAmt() {
        //1.获取T日(最新股票交易日的日期范围）
        DateTime TEndDateTime = DateTimeUtil.getLastDate4Stock(DateTime.now());
        //TODO mock测试数据，后期数据通过第三方接口动态获取实时数据 可删除
        TEndDateTime=DateTime.parse("2021-12-28 14:50:00", DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss"));
        Date tEndDate = TEndDateTime.toDate();

            //开盘时间
        Date tStartDate = DateTimeUtil.getOpenDate(TEndDateTime).toDate();
        //2.获取T-1日(最新股票交易日的日期范围）
        DateTime PreEndDate = DateTimeUtil.getPreviousTradingDay(TEndDateTime);
        //TODO mock测试数据，后期数据通过第三方接口动态获取实时数据 可删除
        PreEndDate=DateTime.parse("2021-12-27 14:50:00", DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss"));;
        Date preTEndDate = PreEndDate.toDate();
            //开盘时间
        Date preTStartDate = DateTimeUtil.getOpenDate(PreEndDate).toDate();
        //调用mapper查询
            //统计T日
         List<Map> tDate =  stockMarketIndexInfoMapper.getSumAmtInfo(tStartDate,tEndDate,stockInfoConfig.getInner());
            //统计T-1日
        List<Map> preTDate =  stockMarketIndexInfoMapper.getSumAmtInfo(preTStartDate,preTEndDate,stockInfoConfig.getInner());
        //组装数据
        HashMap<String , List> data = new HashMap<>();
        data.put("amtList",tDate);
        data.put("yesAmtList",preTDate);
        //响应数据
        return R.ok(data);
    }

    /**
     * 统计股票各个涨幅区间的数量
     * @return
     */
    @Override
    public R<Map> getIncreaseRangeInfo() {
        //1.获取最新的交易时间(利用第三方时间工具类，并用toDate转换成jdk时间）
        DateTime dateTime = DateTimeUtil.getLastDate4Stock(DateTime.now());
        //TODO mock测试数据，后期数据通过第三方接口动态获取实时数据 可删除
        dateTime=DateTime.parse("2021-12-30 09:42:00", DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss"));
        Date curDate = dateTime.toDate();
        //调用mapper查询
        List<Map> data = stockRtInfoMapper.getIncreaseRangeInfoByDate(curDate);
        //获取有序的标题集合
        List<String> upDownRange = stockInfoConfig.getUpDownRange();
        //将顺序的涨幅区间内的每个元素转化成Map对象即可
        //方式一：普通循环
//        List<Map> allInfos = new ArrayList<>();
//        for (String title : upDownRange) {
//            Map tmp = null;
//            for (Map info : data) {
//                if (info.containsValue(title)) {
//                    tmp = info;
//                }
//            }
//            if (tmp == null){
//                tmp = new HashMap();
//                tmp.put("count",0);
//                tmp.put("title",title);
//            }
//            allInfos.add(tmp);
//        }
    //方式二 使用lambda表达式指定
        List<Map> allInfos = upDownRange.stream().map(title -> {
            Optional<Map> result = data.stream().filter(map -> map.containsValue(title)).findFirst();
            if (result.isPresent()) {
                return result.get();
            } else {
                HashMap<Object, Object> tmp = new HashMap<>();
                tmp.put("title", title);
                tmp.put("count", 0);
                return tmp;
            }
        }).collect(Collectors.toList());
        //组装数据
        HashMap<String, Object> RangeMap = new HashMap<>();
        RangeMap.put("infos",allInfos);
        RangeMap.put("time",curDate);
        //响应数据
        return R.ok(RangeMap);
    }

    /**
     * 获取指定股票T日的分时数据
     * @param stockCode 股票编码
     * @return
     */
    @Override
    public R<List<Stock4MinuteDomain>> getStockScreenTimeSharing(String stockCode) {
        //1.获取T日最新交易股票的交易时间点，endTime
        DateTime enDateTime = DateTimeUtil.getLastDate4Stock(DateTime.now());
        //TODO:moctime
        enDateTime = DateTime.parse("2021-12-30 14:32:00",DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss"));
        Date enDate = enDateTime.toDate();
        //开盘时间
        Date openDate = DateTimeUtil.getOpenDate(enDateTime).toDate();
        //2.查询
        List<Stock4MinuteDomain> data =  stockRtInfoMapper.getStock4MinuteInfo(openDate,enDate,stockCode);
        //3.返回响应
        return R.ok(data);
    }

    /**
     * 单个个股日K线数据查询 ，可以根据时间区间查询数日的K线数据
     * @param stockCode 股票编码
     */
    @Override
    public R<List<Stock4EvrDayDomain>> getStockByDayKlin(String stockCode) {
    //获取统计日k线的时间范围
        //1.获取最新的股票交易时间点，截止时间
        DateTime endDateTime = DateTimeUtil.getLastDate4Stock(DateTime.now());
        //TODO:moctime
        endDateTime = DateTime.parse("2022-02-19 09:32:00" ,DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss"));
        Date endDate = endDateTime.toDate();
        //起始时间
        DateTime startDateTime = endDateTime.minusMinutes(1);
        //TODO:moctime
        startDateTime = DateTime.parse("2021-12-19 09:32:00" ,DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss"));
        Date startDate = startDateTime.toDate();
        //根据mapper查询,(方案1：嵌套表查询，会产生性能问题）
//        List<Stock4EvrDayDomain> data = stockRtInfoMapper.getStockByDayKlin(startDate,enDate,stockCode);
        //方案2：先获取指定日期范围内的收盘时间点集合
        List<Date> closeDates = stockRtInfoMapper.getCloseDates(stockCode,startDate,endDate);
        //根据收盘时间获取日K数据
        List<Stock4EvrDayDomain> data = stockRtInfoMapper.getStockByDayKlin(stockCode, closeDates);
        //响应数据
        return R.ok(data);
    }

    /**
     * 获取国外大盘的最新数据，(按降序显示前四条)
     * @return
     */
    @Override
    public R<List<OuterMarketDomain>> getOutMarketInfo() {
        //默认从本地缓存加载数据，如果不存在则从数据库加载并同步到本地缓存
        //在开盘周期内，本地缓存默认有效期为1分钟
        R<List<OuterMarketDomain>> result = (R<List<OuterMarketDomain>>) caffeineCache.get("outerMarketKey", key -> {
            //1.获取最新时间交易时间点
            DateTime dateTime = DateTimeUtil.getLastDate4Stock(DateTime.now());
            //TODO: mock时间
            dateTime = DateTime.parse("2022-01-01 10:57:00",DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss"));
            Date curDate = dateTime.toDate();
//        //2.获取国内大盘集合
            List<String> outerMcodes = stockInfoConfig.getOuter();
            //3.调用mapper查询
            List<OuterMarketDomain> data = stockOuterMarketIndexInfoMapper.getOutMarketInfo(curDate,outerMcodes);
            //4.封装数据并响应
            return R.ok(data);
        });
        return result;

    }

    /**
     * 根据个股编码模糊查询股票信息
     * @param searchStr
     * @return
     */
    @Override
    public R<List<Map<String, Object>>> FuzzySearch(String searchStr) {
        //1.首先进行检验，参数为空的话返回错误
        if (StringUtils.isBlank(searchStr)) {
            return R.error(NO_RESPONSE_DATA);
        }
        //2.对参数进行模糊处理
        String searchStrFuzzy = "%" + searchStr + "%";
        //3.根据股票代进行模糊查询
       List<Map<String,Object>> data = stockRtInfoMapper.getInfobyFuzzySearch(searchStrFuzzy);

        return R.ok(data);
    }


}
