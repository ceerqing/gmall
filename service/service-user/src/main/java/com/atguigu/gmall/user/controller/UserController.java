package com.atguigu.gmall.user.controller;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.common.result.ResultCodeEnum;
import com.atguigu.gmall.model.user.UserInfo;
import com.atguigu.gmall.model.vo.user.LoginSuccessVo;
import com.atguigu.gmall.user.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Author：张世平
 * Date：2022/9/14 11:58
 */
@RestController
@RequestMapping("/api/user")
public class UserController {

    //http://api.gmall.com/api/user/passport/login

    @Autowired
    UserInfoService userInfoService;

    @PostMapping("/passport/login")
    public Result login(@RequestBody UserInfo userInfo){

        LoginSuccessVo successVo=userInfoService.login(userInfo);
        if (successVo!=null){
            return Result.ok(successVo);
        }
        return Result.build("", ResultCodeEnum.LOGIN_FAIL);
    }


    @GetMapping("/passport/logout")
    public Result logout(@RequestHeader("token") String token){

        userInfoService.logout(token);
        return Result.ok();
    }
}
