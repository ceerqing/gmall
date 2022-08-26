package com.atguigu.gmall.product.service;


import com.atguigu.gmall.model.product.BaseCategory2;
import com.baomidou.mybatisplus.extension.service.IService;
import org.bouncycastle.LICENSE;

import java.util.List;

/**
* @author 张世平哒
* @description 针对表【base_category2(二级分类表)】的数据库操作Service
* @createDate 2022-08-26 09:28:02
*/
public interface BaseCategory2Service extends IService<BaseCategory2> {

    /**
     * 根据一级分类id获取二级分类
     * @param id
     * @return
     */
    List<BaseCategory2> getCategory2ByCate1(Long id);
}
