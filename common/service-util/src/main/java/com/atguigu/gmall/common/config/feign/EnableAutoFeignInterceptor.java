package com.atguigu.gmall.common.config.feign;

import com.atguigu.gmall.common.handler.GlobalExceptionHandler;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * Author：张世平
 * Date：2022/9/16 16:21
 */

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(FeignInterceptorConfiguration.class)
public @interface EnableAutoFeignInterceptor {

}
