# iLogger: Быстрый старт

## Описание

**iLogger** — модуль для централизованного логирования интеграционных вызовов между сервисами.  
Автоматически собирает параметры, результат и статус выполнения метода, отправляет лог-записи в выбранное хранилище (БД, очередь, внешний сервис и т.д.).

---

## Подключение

Добавьте зависимость в ваш проект:

```xml
<dependency>
    <groupId>com.example</groupId>
    <artifactId>ilogger-starter</artifactId>
    <version>0.0.1-SNAPSHOT</version>
</dependency>
```
---
## Настройка LogSender
### Своя реализация LogSender:
1. В вашем приложении создайте класс, реализующий интерфейс LogSender, и переопределите метод send(LogEntry logEntry):

```java
public class MyLogSender implements LogSender {
    @Override
    public void send(LogEntry logEntry) {
        // Ваша логика отправки лога
    }
}
```
2. Зарегистрируйте реализацию как @Bean
```java
@Bean
public LogSender logSender() {
  return new MyLogSender();
}
```
### Реализация по умолчанию
Если не определена своя реализация, будет использоваться DefaultLogSender — он выводит лог в консоль через log.info():
```java
@Slf4j
public class DefaultLogSender implements LogSender {
    @Override
    public void send(LogEntry logEntry) {
        log.info("Log entry: {}", logEntry);
    }
}
```
## Использование логгера
Аннотируйте метод, который вызывает другой сервис, аннотацией @ILogger:
```java
@ILogger(targetService = "service-name")
public Response callExternalService(Request request) {
    // Ваш код вызова внешнего сервиса
}
```
targetService — название сервиса, к которому идет обращение.

Логгер автоматически:
- Соберет параметры вызова и результат
- Зафиксирует статус (OK, WARN, ERROR)
- Отправит лог через ваш LogSender

## Формат логов
```
id: [UUID]
sourceService: [название вашего сервиса]
targetService: [название целевого сервиса]
methodName: [имя метода]
requestBody: [аргументы]
responseBody: [результат или ошибка]
responseStatus: [OK, WARN, ERROR]
timestamp: [дата/время]
```
