package com.atguigu.feigin.client.item;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.model.to.SkuDetailTo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Author：张世平
 * Date：2022/8/29 14:48
 */
@FeignClient("service-item")
@RequestMapping("/api/inner/rpc/item")
public interface SkuDetailFeignClient {

    @GetMapping("/skudetail/{skuId}")
    public Result<SkuDetailTo> getSkuDetail(@PathVariable("skuId") Long skuId);
}
