package com.atguigu.gmall.seacher.service.impl;

import com.atguigu.gmall.model.list.Goods;
import com.atguigu.gmall.seacher.repository.GoodsRepository;
import com.atguigu.gmall.seacher.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Author：张世平
 * Date：2022/9/9 15:57
 */
@Service
public class GoodsServiceImpl implements GoodsService {
    @Autowired
    GoodsRepository goodsRepository;
    @Override
    public void save(Goods goods) {
        goodsRepository.save(goods);
    }
}
