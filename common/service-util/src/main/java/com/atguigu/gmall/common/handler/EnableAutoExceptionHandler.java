package com.atguigu.gmall.common.handler;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * Author：张世平
 * Date：2022/9/16 11:19
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(GlobalExceptionHandler.class)
public @interface EnableAutoExceptionHandler {
}
