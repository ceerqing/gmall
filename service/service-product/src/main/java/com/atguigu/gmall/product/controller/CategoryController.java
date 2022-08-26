package com.atguigu.gmall.product.controller;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.model.product.BaseCategory1;
import com.atguigu.gmall.model.product.BaseCategory2;
import com.atguigu.gmall.product.service.BaseCategory1Service;
import com.atguigu.gmall.product.service.BaseCategory2Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Author：张世平
 * Date：2022/8/26 0:25
 */
@RestController
@RequestMapping("/admin/product")
public class CategoryController {

    @Autowired
    BaseCategory1Service baseCategory1Service;
    @Autowired
    BaseCategory2Service baseCategory2Service;
    /**
     * 获取一级分类
     * @return
     */
    @GetMapping("/getCategory1")
    public Result getCategory1(){
        List<BaseCategory1> list = baseCategory1Service.list();
        return Result.ok(list);
    }

    /**
     * 根据一级分类的id获取二级分类
     * @param id
     * @return
     */
    @GetMapping("/getCategory2/{id}")
    public Result getCategory2(@PathVariable("id") Long id){
        List<BaseCategory2> category2ByCate1 = baseCategory2Service.getCategory2ByCate1(id);
        return Result.ok(category2ByCate1 );
    }



}
