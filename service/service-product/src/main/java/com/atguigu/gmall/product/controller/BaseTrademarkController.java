package com.atguigu.gmall.product.controller;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.common.result.ResultCodeEnum;
import com.atguigu.gmall.model.product.BaseTrademark;
import com.atguigu.gmall.product.service.BaseTrademarkService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Author：张世平
 * Date：2022/8/27 19:27
 */
//http://192.168.200.1/admin/product/baseTrademark/1/10
 @Api(tags = "品牌查询api")
@RestController
@RequestMapping("/admin/product")
public class BaseTrademarkController {

     @Autowired
     BaseTrademarkService baseTrademarkService;

     @ApiOperation("分页查询品牌列表")
    @GetMapping("/baseTrademark/{pageNum}/{pageSize}")
    public Result page(@PathVariable("pageNum")Long pageNum,
                       @PathVariable("pageSize") Long pageSize){
        Page<BaseTrademark> pageTrade = new Page<>(pageNum, pageSize);
        baseTrademarkService.page(pageTrade);
        return Result.ok(pageTrade);
    }

    @PostMapping("/baseTrademark/save")
    public Result saveTradeMark(@RequestBody BaseTrademark baseTrademark){
         baseTrademarkService.save(baseTrademark);
         return Result.ok();
    }

    @DeleteMapping("/baseTrademark/remove/{markId}")
    public Result deleteTradeMark(@PathVariable("markId") Long markId){

         baseTrademarkService.removeById(markId);
         return Result.ok();
    }

    @GetMapping("/baseTrademark/get/{markId}")
    public Result getTradeMarkById(@PathVariable("markId")Long markId){
        BaseTrademark byId = baseTrademarkService.getById(markId);
        return Result.ok(byId);
    }

    @PutMapping("/baseTrademark/update")
    public Result updateTradeMark(@RequestBody BaseTrademark baseTrademark){
         baseTrademarkService.updateById(baseTrademark);
         return Result.ok();
    }

    ///baseTrademark/getTrademarkList
    @ApiOperation("查询所有商品的品牌")
    @GetMapping("/baseTrademark/getTrademarkList")
    public Result  getTrademarkList(){
        List<BaseTrademark> list = baseTrademarkService.list();
        return Result.ok(list);
    }

}
