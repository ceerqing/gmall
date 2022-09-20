package com.atguigu.gmall.rabbit.annotation;


import com.atguigu.gmall.rabbit.config.AppRabbitConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * Author：张世平
 * Date：2022/9/20 8:35
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Import(AppRabbitConfiguration.class)
public @interface EnableAppRabbitConfig {
}
