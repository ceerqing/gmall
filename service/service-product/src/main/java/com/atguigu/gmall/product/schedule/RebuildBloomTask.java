package com.atguigu.gmall.product.schedule;



import com.atguigu.gmall.common.constant.SysRedisConstant;
import com.atguigu.gmall.product.bloom.BloomDataService;
import com.atguigu.gmall.product.bloom.BloomOpsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 * 重建布隆任务
 */
@Service
public class RebuildBloomTask {

    @Autowired
    BloomOpsService bloomOpsService;

    @Autowired
    BloomDataService bloomDataService;

    //周六周日零晨三点执行
    @Scheduled(cron = "0 0 3 ? * 6,7 ")
    public void rebuildSkuBloom(){
        bloomOpsService.rebuildBloom(SysRedisConstant.SKU_BLOOM,bloomDataService);
    }

    //重建订单id布隆过滤器

}
