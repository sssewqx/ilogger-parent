package com.ilogger.starter;

import com.ilogger.core.services.senders.LogSender;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LogSenderAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public LogSender logSender() {
        return new DefaultLogSender();
    }
}
