package com.atguigu.feigin.client.user;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.model.user.UserAddress;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * Author：张世平
 * Date：2022/9/17 13:44
 */
@FeignClient("service-user")
@RequestMapping("/api/inner/rpc/user")
public interface UserFeignClient {

    @GetMapping("/getuseralladdress")
    Result<List<UserAddress>> getUserAddByUserId();
}
