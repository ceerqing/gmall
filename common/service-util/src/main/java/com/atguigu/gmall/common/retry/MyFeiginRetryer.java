package com.atguigu.gmall.common.retry;

import feign.RetryableException;
import feign.Retryer;
import lombok.extern.slf4j.Slf4j;

/**
 * Author：张世平
 * Date：2022/9/8 15:32
 */

@Slf4j
public class MyFeiginRetryer implements Retryer {
    @Override
    public void continueOrPropagate(RetryableException e) {
        log.error("远程调用超时.....");
        throw e;
    }

    @Override
    public Retryer clone() {
        return new MyFeiginRetryer();
    }
}
