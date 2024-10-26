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
 * @ClassName: CommonAlg4Db
 * @Description: 定义公共的分库算法类：覆盖个股，大盘，板块相关表
 * @Author: mianbaoren
 * @Date: 2024/10/20 16:51
 */
public class CommonAlg4Db implements PreciseShardingAlgorithm<Date>, RangeShardingAlgorithm<Date> {

    /**
     * 精准查询
     * 分库策略：按年分库
     * 精准查询时走该方法，cur_time条件必须时=，或者in
     * @param dsNames :ds-2021,ds-2022,ds-2023,ds-2024
     * @param shardingValue
     * @return
     */
    @Override
    public String doSharding(Collection<String> dsNames, PreciseShardingValue<Date> shardingValue) {
        //获取逻辑表
        String logicTableName = shardingValue.getLogicTableName();
        //分片键cur_time
        String columnName = shardingValue.getColumnName();
        //获得等值查询的条件值
        Date curTime = shardingValue.getValue();
        //获取条件值对应年份，然后从ds集合中过滤出以该年份结尾的数据源即可
        String year = new DateTime(curTime).getYear() + "";
        //过滤满足条件的
        Optional<String> result = dsNames.stream().filter(dsName -> dsName.endsWith(year)).findFirst();
        if (result.isPresent()) {
            return result.get();
        }
        return null;
    }

    /**
     * 范围查询
     * 分库策略：按年分库
     * 范围查询时走该方法，cur_time条件必须时between and
     * @param dsNames :ds-2021,ds-2022,ds-2023,ds-2024
     * @param shardingValue
     * @return
     */
    @Override
    public Collection<String> doSharding(Collection<String> dsNames, RangeShardingValue<Date> shardingValue) {
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
            //获取条件所属年份
            int startyear = new DateTime(startTime).getYear();
            //过滤出数据源中年份大于等于startYear数据源即可
            dsNames = dsNames.stream()
                    .filter(dsName ->Integer.parseInt(dsName.substring(dsName.lastIndexOf("-") +1)) >= startyear)
                    .collect(Collectors.toList());

        }
        //判断上限
        if (valueRange.hasLowerBound()) {
            //获取范围查询的起始值
            Date endTime = valueRange.upperEndpoint();
            //获取条件所属年份
            int endyear = new DateTime(endTime).getYear();
            //过滤出数据源中年份大于等于startYear数据源即可
            dsNames = dsNames.stream()
                    .filter(dsName ->Integer.parseInt(dsName.substring(dsName.lastIndexOf("-") +1)) <= endyear)
                    .collect(Collectors.toList());

        }
        return dsNames;
    }
}
