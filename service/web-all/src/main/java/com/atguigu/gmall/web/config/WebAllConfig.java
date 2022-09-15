package com.atguigu.gmall.web.config;

import com.atguigu.gmall.common.constant.SysRedisConstant;
import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.Servlet;
import javax.servlet.http.HttpServletRequest;

/**
 * Author：张世平
 * Date：2022/9/15 9:40
 */
@Configuration
public class WebAllConfig {
    //springmvc会将处理当前处理这个请求的线程和request对象进行绑定
    //RequestContextHolder：存储当前请求线程保存的request对象

    @Bean
    public RequestInterceptor userRequestHeader(){

        RequestInterceptor useIdInter=requestTemplate -> {

            ServletRequestAttributes requestAttributes =
                    (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

            HttpServletRequest request = requestAttributes.getRequest();

            String userId = request.getHeader(SysRedisConstant.USERID_HEADER);
            //给feign的新请求添加一个请求头
            requestTemplate.header(SysRedisConstant.USERID_HEADER,userId);
        };
        return useIdInter;
    }
}
