package com.zit.stock.vo.resp;

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
    private Integer totalRows;

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
     * //4.组装PageInfo对象，获取分页的具体信息,因为PageInfo包含了丰富的分页信息，而部分分页信息是前端不需要的
     *         //PageInfo<StockUpdownDomain> pageInfo = new PageInfo<>(date);
     *         // 将获取的数据放到PageInfo中，在PageResult中有构造器，将从数据库中获取的数据封装到PageInfo中，
     *         // 统计条数并且将数据封装到rows结果集中
     * //        PageResult<StockUpdownDomain> pageResult = new PageResult<>(pageInfo);
     *         PageResult<StockUpdownDomain> pageResult = new PageResult<>(new PageInfo<>(data));
     */
    public PageResult(PageInfo<T> pageInfo) {
        totalRows = Long.valueOf(pageInfo.getTotal()).intValue();//获取总记录数
        totalPages = pageInfo.getPages();//获取总页数
        pageNum = pageInfo.getPageNum();//获取当前页
        pageSize = pageInfo.getPageSize();//获取每页记录数
        size = pageInfo.getSize();//获取当前页记录数
        rows = pageInfo.getList();//获取当前页数据
    }
}