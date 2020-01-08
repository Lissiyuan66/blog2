package com.lsy.util;

import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

@Configuration
public class AsyncTaskConfig implements AsyncConfigurer {
    @Override
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        // 线程池维护线程的最少数量
        taskExecutor.setCorePoolSize(10);
        // 线程池维护线程的最大数量
        taskExecutor.setMaxPoolSize(20);
        // 缓存队列
        taskExecutor.setQueueCapacity(50);
        // 对拒绝task的处理策略
        taskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        // 线程名前缀,方便排查问题
        taskExecutor.setThreadNamePrefix("Email-send-thread-");
        // 初始化
        taskExecutor.initialize();
        return taskExecutor;

    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return null;
    }
}