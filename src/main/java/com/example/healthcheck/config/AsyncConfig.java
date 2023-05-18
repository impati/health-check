package com.example.healthcheck.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@EnableAsync
@Configuration
public class AsyncConfig {

    @Bean(name = "healthTargetCheckTaskExecutor",destroyMethod = "shutdown")
    public ThreadPoolTaskExecutor healthTargetCheckTaskExecutor(){
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(1);
        executor.setMaxPoolSize(3);
        executor.setQueueCapacity(Integer.MAX_VALUE);
        return executor;
    }

    @Bean(name = "healthCheckRequesterTaskExecutor",destroyMethod = "shutdown")
    public ThreadPoolTaskExecutor healthCheckRequesterTaskExecutor(){
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5);
        executor.setMaxPoolSize(20);
        executor.setQueueCapacity(Integer.MAX_VALUE);
        return executor;
    }
}
