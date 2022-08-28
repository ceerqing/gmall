package com.atguigu.gmall.product.service;

import com.atguigu.gmall.model.product.SpuInfo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author 张世平哒
* @description 针对表【spu_info(商品表)】的数据库操作Service
* @createDate 2022-08-27 09:53:41
*/
public interface SpuInfoService extends IService<SpuInfo> {

    void saveSpuInfo(SpuInfo spuInfo);
}
