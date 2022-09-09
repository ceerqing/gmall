package com.atguigu.gmall.seacher.api;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.model.list.Goods;
import com.atguigu.gmall.seacher.service.GoodsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Author：张世平
 * Date：2022/9/9 15:55
 */
@RequestMapping("/api/inner/rpc/search")
@RestController
@Api(tags = "将sku存在es的api")
public class SearchApiController {


    @Autowired
    GoodsService goodsService;

    @ApiOperation("将sku商品的平台属性放在es中")
    @PostMapping("/save")
    public Result saveGoods(@RequestBody Goods goods){
        goodsService.save(goods);
        return Result.ok();
    }
}
