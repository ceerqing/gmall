package com.atguigu.gmall.common.config.threadpoolconfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.*;

/**
 * Author：张世平
 * Date：2022/9/1 20:09
 */

@Configuration
@EnableConfigurationProperties(AppThreadPoolProperties.class)
public class AppThreadPoolAutoConfiguration {
    @Autowired
    AppThreadPoolProperties poolProperties;

    @Value("${spring.application.name}")
    String applicationName;

    @Bean
    public ThreadPoolExecutor getThreadPool(){
        ThreadPoolExecutor executor
                = new ThreadPoolExecutor(
                poolProperties.getCore(),
                poolProperties.getMax(),
                poolProperties.getKeepAliveTime(),
                TimeUnit.SECONDS,
                new LinkedBlockingDeque<>(poolProperties.getQueueSize()),
                new ThreadFactory() {
                    int i=0;
                    @Override//给线程池产生线程的地方
                    public Thread newThread(Runnable r) {
                        Thread thread = new Thread(r);
                        thread.setName(applicationName+"[core-thread-"+ i++ +"]");
                        return thread;
                    }
                },new ThreadPoolExecutor.CallerRunsPolicy());
                //无法存入阻塞队列的线程会用当前线程去执行这个线程任务

        return executor;
    }
}
