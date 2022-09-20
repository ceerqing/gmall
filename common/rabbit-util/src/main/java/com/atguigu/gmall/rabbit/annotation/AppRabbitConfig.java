package com.atguigu.gmall.rabbit.annotation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.autoconfigure.amqp.RabbitTemplateConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.Nullable;
import org.springframework.retry.support.RetryTemplate;

/**
 * Author：张世平
 * Date：2022/9/19 21:08
 */
@Configuration
@EnableRabbit
@Slf4j
public class AppRabbitConfig {
    @Bean
    public RabbitTemplate template(RabbitTemplateConfigurer configurer, ConnectionFactory connectionFactory){

        RabbitTemplate template = new RabbitTemplate();
        configurer.configure(template, connectionFactory);

        //消息是否成功被投递到交换机，
        template.setConfirmCallback((CorrelationData correlationData,
                                     boolean ack,
                                     String cause)->{
            if (!ack){
                Message returnedMessage = correlationData.getReturnedMessage();
                log.error("消息未被投递到服务器{},保存到数据库");
            }

        });
        //消息是否成功被投递到队列
        template.setReturnCallback((Message message,
                                    int replyCode,
                                    String replyText,
                                    String exchange,
                                    String routingKey)->{
            //消息没有被正确投递到队列
            log.error("消息投递到队列失败，保存到数据库,{}",message);

        });
        //消息投递到队列的重试次数,重试三次
        template.setRetryTemplate(new RetryTemplate());
        /*CorrelationData correlationData = new CorrelationData();
        String tag="2";
        correlationData.setId(tag);
        correlationData.setReturnedMessage(null);
        template.convertAndSend("exchange","routingkey","obj",correlationData);*/

        return template;
    }
}
