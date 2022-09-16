package com.atguigu.gmall.common.config.feign;

import com.atguigu.gmall.common.constant.SysRedisConstant;
import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * Author：张世平
 * Date：2022/9/16 16:18
 */
@Configuration
public class FeignInterceptorConfiguration {
    //给feign发起的请求添加一些请求数据
    @Bean
    public RequestInterceptor userRequestHeader(){

        RequestInterceptor useIdInter=requestTemplate -> {

            ServletRequestAttributes requestAttributes =
                    (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

            HttpServletRequest request = requestAttributes.getRequest();

            String userId = request.getHeader(SysRedisConstant.USERID_HEADER);
            String tempUserId = request.getHeader(SysRedisConstant.USERTEMPID_HEADER);

            //给feign的新请求添加请求头，共后面的微服务使用
            requestTemplate.header(SysRedisConstant.USERID_HEADER,userId);
            requestTemplate.header(SysRedisConstant.USERTEMPID_HEADER,tempUserId);
        };
        return useIdInter;
    }
}
