package com.atguigu.gmall.web.controller;

import com.atguigu.feigin.client.product.SkuFeignDetail;
import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.model.to.CategoryTreeTo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
 * Author：张世平
 * Date：2022/8/29 11:01
 */

@Controller
public class IndexController {

    @Autowired
    SkuFeignDetail skuFeignDetail;

    @GetMapping({"/","/index","/index.html"})
    public String getIndexCategoryTree(Model model){
        //远程调用查询所有的分类
        Result<List<CategoryTreeTo>> result = skuFeignDetail.getCategoryTree();

        if(result.isOk()){
            //远程成功。 强类型语言
            List<CategoryTreeTo> data = result.getData();
            model.addAttribute("list",data);
        }
        return "index/index";
    }
}
