package com.opendata.domain.tourspot.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
@EnableAsync
public class AsyncConfig implements AsyncConfigurer {

    @Override
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(20);     // 기존 10 → 30
        executor.setMaxPoolSize(40);     // 기존 50 → 100
        executor.setQueueCapacity(100);
        executor.setThreadNamePrefix("Async-"); // 스레드 이름 설정
        executor.setAllowCoreThreadTimeOut(true);
        executor.initialize();
        return executor;
    }
}
