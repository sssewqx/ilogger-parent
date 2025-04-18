package com.ilogger.starter;

import com.ilogger.core.aspect.ILoggerAspect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;
import com.ilogger.core.services.interfaces.ILoggerService;
import com.ilogger.core.services.ILoggerServiceImpl;

@Configuration
@EnableAspectJAutoProxy
@Import(ILoggerServiceImpl.class)
public class ILoggerAutoConfiguration {

    @Bean
    public ILoggerAspect iLoggerAspect(ILoggerService loggerService) {
        return new ILoggerAspect(loggerService);
    }
}
