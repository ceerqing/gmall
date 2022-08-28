package com.atguigu.gmall.product.controller;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.model.product.BaseAttrInfo;
import com.atguigu.gmall.model.product.BaseAttrValue;
import com.atguigu.gmall.product.service.BaseAttrInfoService;
import com.atguigu.gmall.product.service.BaseAttrValueService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.management.relation.RelationSupport;
import java.util.List;

/**
 * Author：张世平
 * Date：2022/8/27 12:01
 * http://192.168.200.1/admin/product/attrInfoList/4/23/192
 */
@Api(tags = "平台属性crudApi")
@RestController
@RequestMapping("/admin/product")
public class BaseAttrController {
    @Autowired
    BaseAttrInfoService baseAttrInfoService;

    @Autowired
    BaseAttrValueService baseAttrValueService;

    @ApiOperation("查询对应分类的属性")
    @GetMapping("/attrInfoList/{c1Id}/{c2Id}/{c3Id}")
    public Result getAttrInfoList(@PathVariable("c1Id") Long c1Id,
                                  @PathVariable("c2Id") Long c2Id,
                                  @PathVariable("c3Id") Long c3Id){

        List<BaseAttrInfo> infos = baseAttrInfoService.getAttrInfoAndValueById(c1Id, c2Id, c3Id);
        return Result.ok(infos);
    }



    @ApiOperation("给一个属性添加属性值或者修改属性名和属性值")
    @PostMapping("/saveAttrInfo")
    public Result saveInfoAttr(@RequestBody @ApiParam("添加或者修改") BaseAttrInfo baseAttrInfo){
        baseAttrInfoService.saveAttrInfo(baseAttrInfo);
        return Result.ok();
    }

    @ApiOperation("根据属性id获取所有的属性值")
    @GetMapping("/getAttrValueList/{attrId}")
    public Result getAttrValueList(@PathVariable("attrId")
                                    @ApiParam("要查询属性值的属性id") Long attrId){

        List<BaseAttrValue> list= baseAttrValueService.getAttrValueList(attrId);
        return Result.ok(list);

    }
}
