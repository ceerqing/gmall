package com.atguigu.gmall.product.api;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.model.to.CategoryTreeTo;
import com.atguigu.gmall.product.mapper.BaseCategory2Mapper;
import com.atguigu.gmall.product.service.BaseCategory2Service;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.bouncycastle.LICENSE;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Author：张世平
 * Date：2022/8/29 11:09
 */
@RequestMapping("/api/inner/rpc/product")
@Api(tags = "RPC获取分类树api")
@RestController
public class CategoryApiController {

    @Autowired
    BaseCategory2Service baseCategory2Service;

    @ApiOperation("获取所有的分类及其子分类")
    @GetMapping("/category/tree")
    public Result getCategoryTree() {
        List<CategoryTreeTo> treeList = baseCategory2Service.getCategoryTree();
        return Result.ok(treeList);
    }
}
