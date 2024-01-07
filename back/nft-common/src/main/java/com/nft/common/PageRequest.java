package com.nft.common;

import lombok.Data;

import javax.validation.constraints.Min;

/**
 * 分页请求
 */
@Data
public class PageRequest {

    /**
     * 当前页号
     */
    @Min(value = 1,message = "current 最小值为1")
    private long current = 1;

    /**
     * 页面大小
     */
    @Min(value = 1,message = "pageSize 最小值为1")
    private long pageSize = 1;

    /**
     * 排序字段
     */
    private String sortField;

    /**
     * 排序顺序（默认升序）
     */
    private String sortOrder = Constants.SortOrder.SORT_ORDER_ASC;
}
