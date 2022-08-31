package com.atguigu.gmall.web.feign;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.model.to.CategoryTreeTo;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * Author：张世平
 * Date：2022/8/29 11:48
 */
@FeignClient("service-product")
@RequestMapping("/api/inner/rpc/product")
public interface CategoryFeignClient {


    @GetMapping("/category/tree")
    public Result<List<CategoryTreeTo>>  getCategoryTree();
}
