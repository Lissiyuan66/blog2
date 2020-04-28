package com.lsy.config;

import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.jms.Queue;


@Configuration
public class MQ_QueueConfig {
    //获取队列名
    @Value("${queue}")
    private String queue;

    //注册bean
    @Bean
    public Queue logQueue() {
        return new ActiveMQQueue(queue);
    }
}

