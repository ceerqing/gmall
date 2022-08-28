package com.atguigu.gmall.product.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Author：张世平
 * Date：2022/8/27 19:33
 */
@Configuration
@EnableTransactionManagement//基于注解的事务
public class MybatisPlusConfig {

    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        //分页插件
        PaginationInnerInterceptor page = new PaginationInnerInterceptor();
        page.setOverflow(true);
        interceptor.addInnerInterceptor(page);
        return interceptor;
    }
}
