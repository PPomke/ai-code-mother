package com.griffin.aicodemother.common;

import lombok.Data;

/**
 *
 * @className: PageRequest
 * @author: Griffin Wang
 * @date: 2026/2/11 10:33
 */
@Data
public class PageRequest {

    /**
     * 当前页号
     */
    private int pageNum = 1;

    /**
     * 页面大小
     */
    private int pageSize = 10;

    /**
     * 排序字段
     */
    private String sortField;

    /**
     * 排序顺序（默认降序）
     */
    private String sortOrder = "descend";
}

