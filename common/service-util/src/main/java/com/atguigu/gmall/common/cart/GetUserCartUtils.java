package com.atguigu.gmall.common.cart;

import com.atguigu.gmall.common.constant.SysRedisConstant;
import com.atguigu.gmall.model.vo.user.UserAuthInfo;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.Servlet;
import javax.servlet.http.HttpServletRequest;

/**
 * Author：张世平
 * Date：2022/9/15 12:52
 */
public class GetUserCartUtils {

    /**
     * 获去添加购物车user的id
     * @return
     */
    public static UserAuthInfo getCartUser(){

        ServletRequestAttributes requestAttributes =
                (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();

        HttpServletRequest request = requestAttributes.getRequest();

        String userId = request.getHeader(SysRedisConstant.USERID_HEADER);
        String tempUserId = request.getHeader(SysRedisConstant.USERTEMPID_HEADER);

        UserAuthInfo userAuthInfo = new UserAuthInfo();

        if (userId!=null){
            //用户可能没有登录
            userAuthInfo.setUserId(Long.parseLong(userId));
        }
        userAuthInfo.setUserTempId(tempUserId);
        return userAuthInfo;
    }
}
