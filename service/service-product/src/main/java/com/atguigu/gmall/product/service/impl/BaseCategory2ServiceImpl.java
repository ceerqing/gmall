package com.atguigu.gmall.product.service.impl;

import com.atguigu.gmall.model.product.BaseCategory2;
import com.atguigu.gmall.model.to.CategoryTreeTo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.atguigu.gmall.product.service.BaseCategory2Service;
import com.atguigu.gmall.product.mapper.BaseCategory2Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author 张世平哒
* @description 针对表【base_category2(二级分类表)】的数据库操作Service实现
* @createDate 2022-08-26 09:28:02
*/
@Service
public class BaseCategory2ServiceImpl extends ServiceImpl<BaseCategory2Mapper, BaseCategory2>
    implements BaseCategory2Service{


    @Autowired
    BaseCategory2Mapper baseCategory2Mapper;


    @Override
    public List<BaseCategory2> getCategory2ByCate1(Long id) {
        QueryWrapper<BaseCategory2>  queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("category1_id",id);
        List<BaseCategory2> list = baseCategory2Mapper.selectList(queryWrapper);
        return list;
    }

    @Override
    public List<CategoryTreeTo> getCategoryTree() {
        return baseCategory2Mapper.getCategoryTree();
    }
}




