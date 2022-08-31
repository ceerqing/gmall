package com.atguigu.gmall.model.to;

import lombok.Data;

import java.util.List;

/**
 * Author：张世平
 * DDD(Domain-Driven Design): 领域驱动设计
 * 三级分类树形结构；
 * 支持无限层级；
 * 当前项目只有三级
 * Date：2022/8/29 10:58
 */

@Data
public class CategoryTreeTo {
    private Long categoryId;
    private String categoryName;
    private List<CategoryTreeTo> categoryChild;
}
