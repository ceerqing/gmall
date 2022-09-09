package com.atguigu.gmall.seacher.repository;

import com.atguigu.gmall.model.list.Goods;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

/**
 * Author：张世平
 * Date：2022/9/9 15:00
 */
@Repository
public interface GoodsRepository extends ElasticsearchRepository<Goods,Long> {
}
