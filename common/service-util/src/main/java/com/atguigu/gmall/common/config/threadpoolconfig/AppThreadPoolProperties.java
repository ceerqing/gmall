package com.atguigu.gmall.common.config.threadpoolconfig;

import io.swagger.models.auth.In;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Author：张世平
 * Date：2022/9/1 20:09
 */

@Data
@ConfigurationProperties(prefix = "app.thread-pool")
public class AppThreadPoolProperties {
    Integer core;
    Integer max;
    Long keepAliveTime;
    Integer queueSize;


}

