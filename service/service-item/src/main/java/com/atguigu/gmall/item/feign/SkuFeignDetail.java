package com.atguigu.gmall.item.feign;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.model.to.SkuDetailTo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Author：张世平
 * Date：2022/8/29 15:20
 */
@FeignClient("service-product")
@RequestMapping("/api/inner/rpc/product")
public interface SkuFeignDetail {


   @GetMapping("/skuDetail/{skuId}")  //不要用这个超级接口
   Result<SkuDetailTo> getSkuDetail(@PathVariable("skuId") Long skuId);
}
