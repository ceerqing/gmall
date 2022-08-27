package com.atguigu.gmall.product.service;

import com.atguigu.gmall.model.product.BaseAttrValue;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author 张世平哒
* @description 针对表【base_attr_value(属性值表)】的数据库操作Service
* @createDate 2022-08-27 09:53:41
*/
public interface BaseAttrValueService extends IService<BaseAttrValue> {

    /**
     * 根据属性值的id获取所有的属性
     * @param attrId
     * @return
     */
    List<BaseAttrValue> getAttrValueList(Long attrId);
}
