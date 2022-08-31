package com.atguigu.gmall.product.mapper;


import com.atguigu.gmall.model.product.BaseCategory3;
import com.atguigu.gmall.model.to.CategoryViewTo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
* @author 张世平哒
* @description 针对表【base_category3(三级分类表)】的数据库操作Mapper
* @createDate 2022-08-26 09:28:02
* @Entity com.atguigu.gmall.product.domain.BaseCategory3
*/
public interface BaseCategory3Mapper extends BaseMapper<BaseCategory3> {

    /**
     * 根据三级分类Id获取层级详细分类
     * @param category3Id
     * @return
     */
    CategoryViewTo getCategoryView(Long category3Id);
}




