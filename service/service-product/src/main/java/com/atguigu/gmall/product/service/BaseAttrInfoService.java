package com.atguigu.gmall.product.service;

import com.atguigu.gmall.model.product.BaseAttrInfo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author 张世平哒
* @description 针对表【base_attr_info(属性表)】的数据库操作Service
* @createDate 2022-08-27 09:53:41
*/
public interface BaseAttrInfoService extends IService<BaseAttrInfo> {

    List<BaseAttrInfo> getAttrInfoAndValueById(Long c1Id, Long c2Id, Long c3Id);

    /**
     * 给一个分类添加属性和属性值
     * @param baseAttrInfo
     */
    void saveAttrInfo(BaseAttrInfo baseAttrInfo);
}
