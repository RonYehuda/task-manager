package com.ron.taskmanager.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
public class AsyncConfig implements AsyncConfigurer {
    private final Logger logger = LoggerFactory.getLogger(AsyncConfig.class);

    //Creating a new Thread Pool
    @Bean(name = "taskExecutor")
    public Executor threadPoolTaskExecutor(){
        logger.info("Creating new thread pool");
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(3); //Processes that open immediately(Always runs)
        executor.setMaxPoolSize(10); // Maximum threads that can be opened
        executor.setQueueCapacity(50); //Number of tasks that can wait in the queue
        executor.setThreadNamePrefix("TaskAsync");
        executor.initialize();
        logger.info("Created new thread pool successfully");
        return executor;
    }

    // Global handler to capture and log exceptions thrown by @Async methods
    // that do not return a Future (void return type).
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler(){
        return (ex, method, params) -> {
            logger.error("Error in async method: {}", method.getName());
            logger.error("Exception: {}", ex.getMessage());
        };
    }
}
