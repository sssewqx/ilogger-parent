package com.ilogger.core.services.senders;

import com.ilogger.core.entities.LogEntry;

/**
 * Интерфейс для отправки лог-записей в различные системы хранения.
 * <p>
 * Для использования необходимо:
 * <ol>
 *   <li>Создать класс-реализацию этого интерфейса</li>
 *   <li>Зарегистрировать его как Spring Bean с помощью @Bean или @Component</li>
 * </ol>
 *
 * <p><b>Примеры возможных реализаций:</b></p>
 * <ul>
 *   <li>JpaLogSender - сохранение логов в реляционную БД через JPA</li>
 *   <li>RestLogSender - отправка логов во внешний сервис через HTTP</li>
 *   <li>QueueLogSender - публикация логов в очередь сообщений (Kafka, RabbitMQ)</li>
 *   <li>FileLogSender - запись логов в файловую систему</li>
 *   <li>CompositeLogSender - отправка в несколько систем одновременно</li>
 * </ul>
 *
 * @see LogEntry Класс лог-записи, содержащий все необходимые данные
 */
public interface LogSender {

    /**
     * @param logEntry Класс лог-записи, содержащий все необходимые данные
     * @implNote В классе-реализации необходимо переопределить этот метод и дописать нужный функционал
     */
    default void send(LogEntry logEntry) {
    }
}
