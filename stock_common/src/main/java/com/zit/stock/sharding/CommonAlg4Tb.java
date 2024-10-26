package com.zit.stock.sharding;

import com.google.common.collect.Range;
import org.apache.shardingsphere.api.sharding.standard.PreciseShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.standard.PreciseShardingValue;
import org.apache.shardingsphere.api.sharding.standard.RangeShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.standard.RangeShardingValue;
import org.joda.time.DateTime;

import java.util.Collection;
import java.util.Date;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @ClassName: CommonAlg4Tb
 * @Description: 定义个股流水表的分表算法类：覆盖个股表
 * @Author: mianbaoren
 * @Date: 2024/10/21 13:41
 */
public class CommonAlg4Tb implements PreciseShardingAlgorithm<Date>, RangeShardingAlgorithm<Date> {

    /**
     * 精准查询
     * 分库策略：按月分表
     * 精准查询时走该方法，cur_time条件必须时=，或者in
     * @param tbNames :2023->202301..202312
     * @param shardingValue
     * @return
     */
    @Override
    public String doSharding(Collection<String> tbNames, PreciseShardingValue<Date> shardingValue) {
        //获取逻辑表
        String logicTableName = shardingValue.getLogicTableName();
        //分片键cur_time
        String columnName = shardingValue.getColumnName();
        //获得等值查询的条件值
        Date curTime = shardingValue.getValue();
        //获取条件值对应年月，然后从tb集合中过滤出以该年月结尾的表即可
        String yearMouth = new DateTime(curTime).toString("yyyyMM");
        //过滤满足条件的
        Optional<String> result = tbNames.stream().filter(tbName -> tbName.endsWith(yearMouth)).findFirst();
        if (result.isPresent()) {
            return result.get();
        }
        return null;
    }

    /**
     * 范围查询
     * 分库策略：按年分表
     * 范围查询时走该方法，cur_time条件必须时between and
     * @param tbNames :2023->202301..202312
     * @param shardingValue
     * @return
     */
    @Override
    public Collection<String> doSharding(Collection<String> tbNames, RangeShardingValue<Date> shardingValue) {
        //获取逻辑表
        String logicTableName = shardingValue.getLogicTableName();
        //分片键cur_time
        String columnName = shardingValue.getColumnName();
        //获取范围数据的封装
        Range<Date> valueRange = shardingValue.getValueRange();
        //判断下限
        if (valueRange.hasLowerBound()) {
            //获取范围查询的起始值
            Date startTime = valueRange.lowerEndpoint();
            //获取条件所属年月
            int startyearMonth = Integer.parseInt(new DateTime(startTime).toString("yyyyMM"));
            //过滤出数据源中年份大于等于startYear数据源即可
            tbNames = tbNames.stream()
                    .filter(dsName ->Integer.parseInt(dsName.substring(dsName.lastIndexOf("_") +1)) >= startyearMonth)
                    .collect(Collectors.toList());

        }
        //判断上限
        if (valueRange.hasLowerBound()) {
            //获取范围查询的起始值
            Date endTime = valueRange.upperEndpoint();
            //获取条件所属年月
            int endyearMonth = Integer.parseInt(new DateTime(endTime).toString("yyyyMM"));
            //过滤出数据源中年份大于等于startYear数据源即可
            tbNames = tbNames.stream()
                    .filter(tbName ->Integer.parseInt(tbName.substring(tbName.lastIndexOf("_") +1)) <= endyearMonth)
                    .collect(Collectors.toList());

        }
        return tbNames;
    }
}
