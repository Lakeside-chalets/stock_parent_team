package com.zit.stock.service;

import com.zit.stock.vo.req.LogPageReqVo;
import com.zit.stock.vo.resp.PageResult;
import com.zit.stock.vo.resp.R;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @ClassName: LogService
 * @Description: 日志的接口
 * @Author: mianbaoren
 * @Date: 2024/10/12 13:50
 */
public interface LogService {
    /**
     * 日志信息综合查询
     * @param vo
     * @return
     */
    R<PageResult> logPageQuery(LogPageReqVo vo);

    /**
     * 删除日志信息
     * @param logIds
     * @return
     */
    R<String> deleteBatch(@Param("logIds") List<Long> logIds);
}
