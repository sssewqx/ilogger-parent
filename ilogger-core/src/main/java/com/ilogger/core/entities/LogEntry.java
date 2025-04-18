package com.ilogger.core.entities;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
/**
 * Класс, представляющий запись лога.
 * <p>
 * Содержит информацию о:
 * <ul>
 *   <li>Вызове метода (источник, цель, имя метода)</li>
 *   <li>Переданных параметрах (requestBody)</li>
 *   <li>Результате выполнения (responseBody, responseStatus)</li>
 *   <li>Метаданных (id, timestamp)</li>
 * </ul>
 *
 * <p><b>Формат вывода:</b></p>
 * <pre>
 * id: [UUID]
 * sourceService: [название сервиса-источника]
 * targetService: [название целевого сервиса]
 * methodName: [имя метода]
 * requestBody: [параметры запроса]
 * responseBody: [результат выполнения]
 * responseStatus: [статус выполнения] -> [OK, WARN, ERROR]
 * timestamp: [временная метка в формате dd-MM-yyyy/HH:mm:ss]
 * </pre>
 */
@Getter
@Setter
public class LogEntry {
    private UUID id;
    private String sourceService;
    private String targetService;
    private String methodName;
    private String requestBody; 
    private String responseBody;
    private ResponseStatus responseStatus;
    private LocalDateTime timestamp;

    /**
     * Возвращает строковое представление лог-записи.
     *
     * @return форматированная строка со всеми полями объекта,
     *         где timestamp представлен в формате dd-MM-yyyy/HH:mm:ss
     */
    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy/HH:mm:ss");

        return """
            id: %s
            sourceService: %s
            targetService: %s
            methodName: %s
            requestBody: %s
            responseBody: %s
            responseStatus: %s
            timestamp: %s
            """.formatted(id,
                sourceService,
                targetService,
                methodName,
                requestBody,
                responseBody,
                responseStatus,
                timestamp != null ? timestamp.format(formatter) : null);
    }
}
