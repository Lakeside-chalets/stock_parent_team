package com.zit.stock.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zit.stock.mapper.SysLogMapper;
import com.zit.stock.pojo.entity.SysLog;
import com.zit.stock.service.LogService;
import com.zit.stock.vo.req.LogPageReqVo;
import com.zit.stock.vo.resp.PageResult;
import com.zit.stock.vo.resp.R;
import com.zit.stock.vo.resp.ResponseCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @ClassName: LogServiceImpl
 * @Description: 日志的接口实现类
 * @Author: mianbaoren
 * @Date: 2024/10/12 13:51
 */
@Service("logService")
@Slf4j
public class LogServiceImpl implements LogService {
@Autowired
private SysLogMapper sysLogMapper;
    /**
     * 日志信息综合查询
     * @param vo
     * @return
     */
    @Override
    public R<PageResult> logPageQuery(LogPageReqVo vo) {
        if (vo == null) {
            return R.error(ResponseCode.DATA_ERROR.getMessage()) ;
        }
        //组装数据,获取分页数据信息
        PageHelper.startPage(vo.getPageNum(),vo.getPageSize());
        //分页查询
        List<SysLog> logList=this.sysLogMapper.findByCondition(vo.getUsername(),vo.getOperation(),vo.getStartTime(),vo.getEndTime());
        //封装分页数据
        PageResult<SysLog> pageResult = new PageResult<>(new PageInfo<>(logList));
        //响应数据
        return R.ok(pageResult);
    }

    /**
     * 删除日志信息
     * @param logIds
     * @return
     */
    @Override
    public R<String> deleteBatch(List<Long> logIds) {
        if (CollectionUtils.isEmpty(logIds)) {
            return R.error(ResponseCode.DATA_ERROR.getMessage());
        }
        this.sysLogMapper.deleteBatchByLogIds(logIds);
        return R.ok(ResponseCode.SUCCESS.getMessage());
    }

}
