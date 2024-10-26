package com.zit.stock.vo.req;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName: UserPageReqVo
 * @Description: 前端对于分页查询传递的请求参数
 * @Author: mianbaoren
 * @Date: 2024/9/26 13:32
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserPageReqVo {

    /**
     * 当前页
     */
    public Integer pageNum;
    /**
     * 当前页面显示了多少条数据
     */
    public Integer pageSize;
    /**
     * 用户名
     */
    public String username;

    /**
     * 昵称
     */
    public String nickName;

    /**
     * 开始创建日期
     */
    //@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public  String startTime;
    /**
     * 结束创建日期
     */
    //@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public  String endTime;



}

