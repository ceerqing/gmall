package com.atguigu.gmall.seacher.service;

import com.atguigu.gmall.model.list.Goods;
import com.atguigu.gmall.model.vo.search.SearchParamVo;
import com.atguigu.gmall.model.vo.search.SearchResponseVo;

/**
 * Author：张世平
 * Date：2022/9/9 15:57
 */
public interface GoodsService {
    void save(Goods goods);

    void delete(Long  skuId);

    SearchResponseVo search(SearchParamVo searchParamVo)    ;

    void updateSkuHotScore(Long skuId, Long increment);
}
