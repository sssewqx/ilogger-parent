package com.ilogger.starter;

import com.ilogger.core.entities.LogEntry;
import com.ilogger.core.services.senders.LogSender;
import lombok.extern.slf4j.Slf4j;

/**
 * Default реализация LogSender, используется по умолчанию если не определен собственный @Bean-реализация
 * @implNote
 * Выводит созданный лог в консоль
 * <p>
 * log.info("Log entry: {}", logEntry);
 */
@Slf4j
public class DefaultLogSender implements LogSender {

    @Override
    public void send(LogEntry logEntry) {
        log.info("Log entry: {}", logEntry);
    }
}
