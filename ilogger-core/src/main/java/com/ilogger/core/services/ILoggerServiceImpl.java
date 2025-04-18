package com.ilogger.core.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ilogger.core.services.interfaces.ILoggerService;
import com.ilogger.core.services.senders.LogSender;
import com.ilogger.core.entities.LogEntry;
import com.ilogger.core.entities.ResponseStatus;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Callable;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ILoggerServiceImpl implements ILoggerService {

    private final LogSender logSender;
    private final Environment env;
    private static final ObjectMapper mapper = new ObjectMapper();

    /**
     * Выполняет переданную операцию с логированием параметров и результата.
     *
     * @param operation     выполняемая операция (оборачиваемый метод)
     * @param methodName    название логируемого метода
     * @param args          аргументы метода в виде ключ-значение, может быть null
     * @param targetService название сервиса, к которому обращается метод
     * @return Результат выполнения операции (operation, а не готовый лог)
     * @throws Exception исключение, возникшее при выполнении операции
     * @implNote Последовательность действий:
     * <ol>
     *   <li>Создает запись лога с параметрами вызова</li>
     *   <li>Выполняет операцию</li>
     *   <li>В случае успеха - фиксирует успешный статус</li>
     *   <li>В случае ошибки - фиксирует тип и сообщение ошибки</li>
     *   <li>В любом случае отправляет запись лога через logSender</li>
     * </ol>
     */
    public Object logCall(Callable<Object> operation,
        String methodName,
        Map<String, Object> args,
        String targetService) throws Exception {

        LogEntry logEntry = createLogEntry(methodName, args, targetService);

        try {
            Object result = operation.call();
            logEntry.setResponseStatus(ResponseStatus.OK);
            logEntry.setResponseBody(formatResponse(result));
            return result;
        } catch (Throwable e) {
            handleException(logEntry, e);
            throw e;
        } finally {
            logSender.send(logEntry);
        }
    }

    /**
     * Создает новую запись лога на основе параметров вызова.
     *
     * @param methodName название вызываемого метода
     * @param args аргументы метода
     * @param targetService название сервиса, к которому обращается метод
     * @return Объект LogEntry без responseStatus(статус будет задан после выполнения операции)
     */
    private LogEntry createLogEntry(String methodName,
        Map<String, Object> args,
        String targetService) {

        LogEntry logEntry = new LogEntry();

        logEntry.setId(UUID.randomUUID());
        logEntry.setSourceService(getApplicationName());
        logEntry.setTargetService(targetService);
        logEntry.setTimestamp(LocalDateTime.now());
        logEntry.setMethodName(methodName);
        logEntry.setRequestBody(formatArguments(args));

        return logEntry;
    }

    /**
     * Обрабатывает исключение, возникшее при выполнении операции.
     * Автоматически определяет уровень серьезности ошибки на основе:
     * <ul>
     *   <li>Ответы 400/Bad Request как предупреждение (WARN)</li>
     *   <li>Все остальные ошибки как ошибки (ERROR)</li>
     * </ul>
     * @param logEntry запись лога для обновления
     * @param e возникшее исключение
     */
    private void handleException(LogEntry logEntry, Throwable e) {
        String errorMessage = e.getMessage();

        if (errorMessage != null &&
            (errorMessage.contains("400") ||
                errorMessage.contains("Bad Request"))) {
            logEntry.setResponseStatus(ResponseStatus.WARN);
        } else {
            logEntry.setResponseStatus(ResponseStatus.ERROR);
        }

        logEntry.setResponseBody(e.getClass().getSimpleName() + ": " + e.getMessage());
    }

    /**
     * Получает название текущего сервиса из конфигурации Spring.
     * Использует свойство 'spring.application.name'.
     *
     * @return название сервиса из конфигурации или "unknown-service",
     *         если свойство не задано
     */
    private String getApplicationName() {
        return env.getProperty("spring.application.name", "unknown-service");
    }

    /**
     * Форматирует аргументы метода в JSON строку.
     *
     * @param args аргументы метода
     * @return
     * <ol>
     *     <li>JSON-строка с аргументами</li>
     *     <li>"no-args" если аргументов нет</li>
     *     <li>"args-formatting-failed" в случае ошибки при форматировании</li>
     * </ol>
     */
    private String formatArguments(Map<String, Object> args) {
        if (args == null || args.isEmpty()) {
            return "no-args";
        }

        try {
            return mapper.writeValueAsString(args);
        } catch (JsonProcessingException e) {
            return "args-formatting-failed";
        }
    }
    /**
     * Форматирует ответ сервиса в строку.
     *
     * @param response объект ответа от сервиса
     * @return
     * <ol>
     *     <li>JSON-строка с ответом, если ответ не null</li>
     *     <li>"no-data" если ответ null</li>
     *     <li>"response-formatting-failed" в случае ошибки при форматировании</li>
     * </ol>
     */
    private String formatResponse(Object response) {
        if (response == null) {
            return "no-data";
        }

        try {
            return mapper.writeValueAsString(response);
        } catch (JsonProcessingException e) {
            return "response-formatting-failed";
        }
    }
}
