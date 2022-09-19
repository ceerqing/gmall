package com.atguigu.feigin.client.ware;

import com.atguigu.gmall.common.result.Result;
import io.swagger.models.auth.In;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Author：张世平
 * Date：2022/9/17 15:33
 */
@FeignClient(value = "ware-manage",url = "http://localhost:9001/")
public interface WareManageFeignClient {

    @GetMapping("/hasStock")
    String hasStock(@RequestParam("skuId")Long skuId,
                    @RequestParam("num")Integer num);

}
