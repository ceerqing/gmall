package com.atguigu.gmall.seacher.repository;

import com.atguigu.gmall.seacher.bean.Person;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

/**
 * Author：张世平
 * Date：2022/9/9 9:13
 */
@Repository
public interface PersonRepository extends ElasticsearchRepository<Person,Long> {
}
