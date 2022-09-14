package com.atguigu.feigin.client.search;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.model.list.Goods;
import com.atguigu.gmall.model.vo.search.SearchParamVo;
import com.atguigu.gmall.model.vo.search.SearchResponseVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.annotation.RequestScope;

/**
 * Author：张世平
 * Date：2022/9/9 16:01
 */
@FeignClient("service-search")
@RequestMapping("/api/inner/rpc/search")
public interface SearchFeignClient {
    @PostMapping("/goods/save")
    public Result saveGoods(@RequestBody Goods goods);


    @DeleteMapping("/goods/delete/{skuId}")
    public void deleteGoods(@PathVariable("skuId") Long skuId);

    @PostMapping("/goods/search")
    Result<SearchResponseVo> search(@RequestBody SearchParamVo searchParamVo);

    @PutMapping("/goods/updatehotscore/{skuId}")
    Result updateSkuHotScore(@PathVariable("skuId") Long skuId,
                           @RequestParam("increment") Long increment);
}
