package com.atguigu.gmall.user.api;

import com.atguigu.gmall.common.cart.GetUserCartUtils;
import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.model.user.UserAddress;
import com.atguigu.gmall.model.vo.user.UserAuthInfo;
import com.atguigu.gmall.user.service.UserAddressService;
import com.atguigu.gmall.user.service.UserInfoService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Author：张世平
 * Date：2022/9/17 14:03
 */
@RestController
@RequestMapping("/api/inner/rpc/user")
public class ApiUserController {
    @Autowired
    UserAddressService userAddressService;

    @GetMapping("/getuseralladdress")
    Result<List<UserAddress>> getUserAddByUserId() {
        UserAuthInfo cartUser = GetUserCartUtils.getCartUser();
        //查询用户的收获列表
        QueryWrapper<UserAddress> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", cartUser.getUserId());
        return Result.ok(userAddressService.list(wrapper));

    }
}
