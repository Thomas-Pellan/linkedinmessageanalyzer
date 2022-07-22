package fr.pellan.api.linkedinmessageanalyzer.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

/**
 * Configuration of the event queue for events.
 */
@EnableAsync
@Configuration
public class AsyncEventConfig implements AsyncConfigurer {

    @Value(value = "${spring.max.concurrent.threads}")
    private int nbConcurrentThreads;

    @Value(value = "${spring.max.concurrent.pool.size}")
    private int poolSize;

    @Value(value = "${spring.max.concurrent.queue.size}")
    private int queueSize;

    @Override
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();

        executor.setCorePoolSize(nbConcurrentThreads);
        executor.setMaxPoolSize(poolSize);
        executor.setQueueCapacity(queueSize);

        executor.setThreadNamePrefix("linkedinmessageanalyzer-executor-");
        executor.initialize();

        return executor;
    }
}
