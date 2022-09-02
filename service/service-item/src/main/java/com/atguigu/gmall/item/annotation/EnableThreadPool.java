package com.atguigu.gmall.item.annotation;

import com.atguigu.gmall.common.config.threadpoolconfig.AppThreadPoolAutoConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;

import java.lang.annotation.*;

/**
 * Author：张世平
 * Date：2022/9/1 21:27
 */

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
@Import(AppThreadPoolAutoConfiguration.class)
public @interface EnableThreadPool {
}
