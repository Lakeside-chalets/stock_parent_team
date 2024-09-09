package com.itheima.stock.vo.resp;

import com.github.pagehelper.PageInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.io.Serializable;
import java.util.List;

/**
 * 分页实体类
 */
@ApiModel(description = "分页实体类")
@Data
public class PageResult<T> implements Serializable {
    /**
     * 总记录数
     */
    @ApiModelProperty(value = "总记录数", position = 1)
    private Long totalRows;

    /**
     * 总页数
     */
    @ApiModelProperty(value = "总页数", position = 2)
    private Integer totalPages;

    /**
     * 当前第几页
     */
    @ApiModelProperty(value = "当前第几页", position = 3)
    private Integer pageNum;
    /**
     * 每页记录数
     */
    @ApiModelProperty(value = "每页记录数", position = 4)
    private Integer pageSize;
    /**
     * 当前页记录数
     */
    @ApiModelProperty(value = "当前页记录数", position = 5)
    private Integer size;
    /**
     * 结果集
     */
    @ApiModelProperty(value = "结果集", position = 6)
    private List<T> rows;

    /**
     * 分页数据组装
     * @param pageInfo
     * @return
     */
    public PageResult(PageInfo<T> pageInfo) {
        totalRows = pageInfo.getTotal();
        totalPages = pageInfo.getPages();
        pageNum = pageInfo.getPageNum();
        pageSize = pageInfo.getPageSize();
        size = pageInfo.getSize();
        rows = pageInfo.getList();
    }
}