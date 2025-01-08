package com.zit.stock.service.impl;

import com.google.common.collect.Lists;
import com.zit.stock.constant.ParseType;
import com.zit.stock.face.StockCacheFace;
import com.zit.stock.mapper.*;
import com.zit.stock.pojo.entity.StockBlockRtInfo;
import com.zit.stock.pojo.entity.StockMarketIndexInfo;
import com.zit.stock.pojo.entity.StockOuterMarketIndexInfo;
import com.zit.stock.pojo.entity.StockRtInfo;
import com.zit.stock.pojo.vo.StockInfoConfig;
import com.zit.stock.service.StockTimerTaskService;
import com.zit.stock.utils.DateTimeUtil;
import com.zit.stock.utils.IdWorker;
import com.zit.stock.utils.ParserStockInfoUtil;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @ClassName: StockTimerTaskServiceImpl
 * @Description: TODO
 * @Author: mianbaoren
 * @Date: 2024/9/5 17:42
 */
@Service
@Slf4j
public class StockTimerTaskServiceImpl implements StockTimerTaskService {
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private StockInfoConfig stockInfoConfig;
    @Autowired
    private IdWorker idWorker;
    @Autowired
    private StockMarketIndexInfoMapper stockMarketIndexInfoMapper;
    @Autowired
    private StockBusinessMapper stockBusinessMapper;
    @Autowired
    private ParserStockInfoUtil parserStockInfoUtil;
    @Autowired
    private StockRtInfoMapper stockRtInfoMapper;
    @Autowired
    private StockBlockRtInfoMapper stockBlockRtInfoMapper;
    @Autowired
    private StockOuterMarketIndexInfoMapper stockOuterMarketIndexInfoMapper;
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;
    @Autowired
    private StockCacheFace stockCacheFace;

    private HttpEntity<Object> httpEntity;

    /**
     * bean生命周期的初始化回调方法
     * TODO:将http请求对象封装起来
     */
    @PostConstruct
    public void initHttpHeader() {
        //1.2维护请求头，添加防盗链和用户标识
        HttpHeaders headers = new HttpHeaders();
        headers.add("Referer", "https://finance.sina.com.cn/stock/");
        //用户客户端标识
        headers.add("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/128.0.0.0 Safari/537.36");
        //维护http请求实体对象
        httpEntity = new HttpEntity<>(headers);
    }

    /**
     * 获取国内大盘的数据信息
     */
    @Override
    public void getInnerMarketInfo() {
        //1.采集原始数据
        //1.1组装url地址
//        String url = "http://hq.sinajs.cn/list=sh601003,sh601001";
        String url = stockInfoConfig.getMarketUrl() + String.join(",", stockInfoConfig.getInner());
//            //1.2维护请求头，添加防盗链和用户标识
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Referer" ,"https://finance.sina.com.cn/stock/");
//            //用户客户端标识
//        headers.add("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/128.0.0.0 Safari/537.36");
//            //维护http请求实体对象
//        HttpEntity<Object> httpEntity = new HttpEntity<>(headers);
        //发起请求
        ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.GET, httpEntity, String.class);
        int statusCodeValue = responseEntity.getStatusCodeValue();
        if (statusCodeValue != 200) {
            //当前请求失败
            log.error("当前时间点:{},采集数据失败，http状态码:{}", DateTime.now().toString("yyyy-MM-dd HH:mm:ss"), statusCodeValue);
            //其他（发送邮件……）
            return;
        }
        //获取js格式的数据
        String jsData = responseEntity.getBody();
        log.info("当前时间点:{},采集原始数据内容：{}", DateTime.now().toString("yyyy-MM-dd HH:mm:ss"), jsData);
        //2.java正则解析原始数据
        //2.1定义正则表达式
        String reg = "var hq_str_(.+)=\"(.+)\";";
        //2.2表达式编译
        Pattern pattern = Pattern.compile(reg);
        //2.3匹配字符串
        Matcher matcher = pattern.matcher(jsData);
        List<StockMarketIndexInfo> entities = new ArrayList<>();
        while (matcher.find()) {
            //1.获取大盘的编码
            String marketCode = matcher.group(1);
            //2.获取其他信息
            String otherInfo = matcher.group(2);
            //将other数据用 , 切割，获取大盘的数据信息
            String[] splitArr = otherInfo.split(",");
            //大盘名称
            String marketName = splitArr[0];
            //获取当前大盘的开盘点数
            BigDecimal openPoint = new BigDecimal(splitArr[1]);
            //前收盘点
            BigDecimal preClosePoint = new BigDecimal(splitArr[2]);
            //获取大盘的当前点数
            BigDecimal curPoint = new BigDecimal(splitArr[3]);
            //获取大盘最高点
            BigDecimal maxPoint = new BigDecimal(splitArr[4]);
            //获取大盘的最低点
            BigDecimal minPoint = new BigDecimal(splitArr[5]);
            //获取成交量
            Long tradeAmt = Long.valueOf(splitArr[8]);
            //获取成交金额
            BigDecimal tradeVol = new BigDecimal(splitArr[9]);
            //时间
            Date curTime = DateTimeUtil.getDateTimeWithoutSecond(splitArr[30] + " " + splitArr[31]).toDate();
            //3.解析的数据封装entity
            StockMarketIndexInfo entity = StockMarketIndexInfo.builder()
                    .id(idWorker.nextId())
                    .marketName(marketName)
                    .openPoint(openPoint)
                    .preClosePoint(preClosePoint)
                    .curPoint(curPoint)
                    .maxPoint(maxPoint)
                    .minPoint(minPoint)
                    .tradeAmount(tradeAmt)
                    .tradeVolume(tradeVol)
                    .marketCode(marketCode)
                    .curTime(curTime)
                    .build();
            entities.add(entity);
        }
        log.info("解析数据完毕!");
        //4.调用mybatis批量入库
        int count = stockMarketIndexInfoMapper.insertBatch(entities);
        if (count > 0) {
            //大盘采集完毕后，通知backend工程刷新缓存
            //发送日期对象，接收方通过接收的日期与当前日期对比，能判断出数据延迟的时常问题
            rabbitTemplate.convertAndSend("stockExchange", "inner.mar", new Date());
            log.info("当前时间点:{},插入大盘数据:{}成功", DateTime.now().toString("yyyy-MM-dd HH:mm:ss"), entities);
        } else {
            log.info("当前时间点:{},插入大盘数据:{}失败", DateTime.now().toString("yyyy-MM-dd HH:mm:ss"), entities);
        }

    }

    /**
     * 定义获取分钟级股票数据
     */
    @Override
    public void getStockRtIndex() {
//        //1.获取所有个股的集合 3000+
//        List<String> allStockCode = stockBusinessMapper.getAllStockCode();
//        //
//        添加大盘前缀 sh sz
//        allStockCode = allStockCode.stream().map(code -> code.startsWith("6") ? "sh" + code : "sz" + code).collect(Collectors.toList());
//        System.out.println(allStockCode);

        //通过缓存获取数据，（第一次还是从数据库获取）
        List<String> allStockCode = stockCacheFace.getAllStockCodeWithPredix();
        //将所有股票编码组成的大的集合拆分成若干个小的集合
        Lists.partition(allStockCode, 15).forEach(codes -> {
//            //方案1
//            //分批次采集 ,拼接url地址
//            String url = stockInfoConfig.getMarketUrl() + String.join(",", codes);
//            ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.GET, httpEntity, String.class);
//            int statusCodeValue = responseEntity.getStatusCodeValue();
//            if (statusCodeValue!=200) {
//                //当前请求失败
//                log.error("当前时间点:{},采集数据失败，http状态码:{}", DateTime.now().toString("yyyy-MM-dd HH:mm:ss"),statusCodeValue);
//                //其他（发送邮件……）
//                return;
//            }
//            //获取原始js格式数据
//            String jsData = responseEntity.getBody();
//            //利用工具类解析
//            List<StockRtInfo> list = parserStockInfoUtil.parser4StockOrMarketInfo(jsData, ParseType.ASHARE);
//            log.error("采集数据{}", list);
//            //批量保存数据
//            int count = stockRtInfoMapper.insertBatch(list);
//            if(count > 0){
//                log.info("当前时间点:{},插入个股数据:{}成功", DateTime.now().toString("yyyy-MM-dd HH:mm:ss"),list);
//            }else {
//                log.info("当前时间点:{},插入个股数据:{}失败", DateTime.now().toString("yyyy-MM-dd HH:mm:ss"),list);
//            }
            //方案二：引入线程池(提高线程的服用，超过线程池的压入任务对列）
            threadPoolTaskExecutor.execute(() -> {
                //分批次采集 ,拼接url地址
                String url = stockInfoConfig.getMarketUrl() + String.join(",", codes);
                ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.GET, httpEntity, String.class);
                int statusCodeValue = responseEntity.getStatusCodeValue();
                if (statusCodeValue != 200) {
                    //当前请求失败
                    log.error("当前时间点:{},采集数据失败，http状态码:{}", DateTime.now().toString("yyyy-MM-dd HH:mm:ss"), statusCodeValue);
                    //其他（发送邮件……）
                    return;
                }
                //获取原始js格式数据
                String jsData = responseEntity.getBody();
                //利用工具类解析
                List<StockRtInfo> list = parserStockInfoUtil.parser4StockOrMarketInfo(jsData, ParseType.ASHARE);
                log.error("采集数据{}", list);
                //批量保存数据
                int count = stockRtInfoMapper.insertBatch(list);
                if (count > 0) {
                    //个股采集完毕后，通知backend工程刷新缓存
                    //发送日期对象，接收方通过接收的日期与当前日期对比，能判断出数据延迟的时常问题
                    rabbitTemplate.convertAndSend("stockExchange", "Stock.rtInfo", new Date());
                    log.info("当前时间点:{},插入个股数据:{}成功", DateTime.now().toString("yyyy-MM-dd HH:mm:ss"), list);
                } else {
                    log.info("当前时间点:{},插入个股数据:{}失败", DateTime.now().toString("yyyy-MM-dd HH:mm:ss"), list);
                }
            });
        });
        log.info("采集成功");
    }

    /**
     * 定义获取板块数据
     */
    @Override
    public void getStockBlockRtIndex() {
        //发送板块数据请求
        String result = restTemplate.getForObject(stockInfoConfig.getBlockUrl(), String.class);
        //响应返回的结果
        List<StockBlockRtInfo> infos = parserStockInfoUtil.parse4StockBlock(result);
        log.info("板块数据量：{}", infos.size());
        //数据分片保存到数据库下 行业板块类目大概50个，可每小时查询一 次即可
        Lists.partition(infos, 20).forEach(list -> {
            //20个一组，批量插入
            int count = stockBlockRtInfoMapper.insertBatch(list);
            if (count>0) {
                //板块采集完毕后，通知backend工程刷新缓存
                //发送日期对象，接收方通过接收的日期与当前日期对比，能判断出数据延迟的时常问题
                rabbitTemplate.convertAndSend("stockExchange", "Stock.Block", new Date());
                log.info("当前时间点:{},插入板块数据:{}成功", DateTime.now().toString("yyyy-MM-dd HH:mm:ss"), list);
            } else {
                log.info("当前时间点:{},插入板块数据:{}失败", DateTime.now().toString("yyyy-MM-dd HH:mm:ss"), list);
            }

        });
    }

    /**
     * 获取国外大盘的实时数据信息
     */
    //方案一
//    @Override
//    public void getStockOuterMarketInfo() {
//    //1.采集原始数据
//        //1.1组装url地址
//        String url = stockInfoConfig.getMarketUrl() + String.join(",", stockInfoConfig.getOuter());
//        //发起请求
//        ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.GET, httpEntity, String.class);
//        int statusCodeValue = responseEntity.getStatusCodeValue();
//        if (statusCodeValue != 200) {
//            //当前请求失败
//            log.error("当前时间点:{},采集数据失败，http状态码:{}", DateTime.now().toString("yyyy-MM-dd HH:mm:ss"), statusCodeValue);
//            //其他（发送邮件……）
//            return;
//        }
//        //获取js格式的数据
//        String jsData = responseEntity.getBody();
//        log.info("当前时间点:{},采集原始数据内容：{}", DateTime.now().toString("yyyy-MM-dd HH:mm:ss"), jsData);
//    //2.java正则解析原始数据
//        //2.1定义正则表达式
//        String reg = "var hq_str_(.+)=\"(.+)\";";
//        //2.2表达式编译
//        Pattern pattern = Pattern.compile(reg);
//        //2.3匹配字符串
//        Matcher matcher = pattern.matcher(jsData);
//        List<StockOuterMarketIndexInfo> entities = new ArrayList<>();
//        while (matcher.find()) {
//            //1.获取大盘的编码
//            String marketCode = matcher.group(1);
//            //2.获取其他信息
//            String otherInfo = matcher.group(2);
//            //将other数据用 , 切割，获取大盘的数据信息
//            String[] splitArr = otherInfo.split(",");
//            //大盘名称
//            String marketName = splitArr[0];
//            //获取大盘的当前点数
//            BigDecimal curPoint = new BigDecimal(splitArr[1]);
//            //获取大盘涨跌值
//            BigDecimal updown = new BigDecimal(splitArr[2]);
//            //获取大盘的涨幅
//            BigDecimal rose = new BigDecimal(splitArr[3]);
//            //时间
//            Date curTime = DateTimeUtil.getDateTimeWithoutSecond(DateTime.now()) .toDate();
////            3.解析的数据封装entity
//            StockOuterMarketIndexInfo entity = StockOuterMarketIndexInfo.builder()
//                    .id(idWorker.nextId())
//                    .marketName(marketName)
//                    .curPoint(curPoint)
//                    .marketCode(marketCode)
//                    .updown(updown)
//                    .rose(rose)
//                    .curTime(curTime)
//                    .build();
//            entities.add(entity);
//        }
//        log.info("解析数据完毕!");
//        //4.调用mybatis批量入库
//        int count = stockOuterMarketIndexInfoMapper.insertBatch(entities);
//        if (count > 0) {
//            //大盘采集完毕后，通知backend工程刷新缓存
//            //发送日期对象，接收方通过接收的日期与当前日期对比，能判断出数据延迟的时常问题
//            rabbitTemplate.convertAndSend("stockExchange", "inner.market", new Date());
//            log.info("当前时间点:{},插入大盘数据:{}成功", DateTime.now().toString("yyyy-MM-dd HH:mm:ss"), entities);
//        } else {
//            log.info("当前时间点:{},插入大盘数据:{}失败", DateTime.now().toString("yyyy-MM-dd HH:mm:ss"), entities);
//        }
//    }
    @Override
    public void getStockOuterMarketInfo() {
        //获取国外大盘名称集合
        List<String> outerMarket = stockInfoConfig.getOuter();
        //获取请求url地址
        String url = stockInfoConfig.getMarketUrl();
        //将url拼接起来
        String marketUrl = url+String.join(",",outerMarket);
        //发起请求
        ResponseEntity<String> responseEntity = restTemplate.exchange(marketUrl, HttpMethod.GET, httpEntity, String.class);
        //获取响应的js数据
        String body = responseEntity.getBody();
        //调用工具类解析获取各个数据
        List<StockOuterMarketIndexInfo> list = parserStockInfoUtil.parser4StockOrMarketInfo(body, ParseType.OUTER);
        log.info("获取外盘数据:{}",list);
        //将调用mapper接口数据保存到数据库中  //批量插入
        int row = stockOuterMarketIndexInfoMapper.insertBatch(list);
        if (row>0) {
            //外盘采集完毕后，通知backend工程刷新缓存
            //发送日期对象，接收方通过接收的日期与当前日期对比，能判断出数据延迟的时常问题
            rabbitTemplate.convertAndSend("stockExchange", "outer.market", new Date());
            log.info("当前时间点:{},插入国外大盘数据:{}成功",DateTime.now().toString("yyyy-MM-dd HH:mm:ss"),list);
        }else {
            log.info("当前时间点:{},插入国外大盘数据:{}失败",DateTime.now().toString("yyyy-MM-dd HH:mm:ss"),list);
        }

    }
}