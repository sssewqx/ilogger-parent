package com.ilogger.core.aspect;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.LinkedHashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import com.ilogger.core.services.interfaces.ILoggerService;

/**
 * Аспект для автоматического логирования вызовов методов.
 * <p>
 * Основные функции:
 * <ul>
 *   <li>Перехват методов, аннотированных @ILogger</li>
 *   <li>Сбор параметров вызова метода</li>
 *   <li>Делегирование логирования в ILoggerService</li>
 * </ul>
 */
@Aspect
@Component
@RequiredArgsConstructor
public class ILoggerAspect {
    private final ILoggerService loggerService;
    /**
     * Перехватывает вызовы методов, помеченных аннотацией @ILogger.
     *
     * @param loggerAnnotation аннотация @ILogger на целевом методе
     *
     * @implNote Последовательность действий:
     * <ol>
     *   <li>Получает информацию о вызываемом методе</li>
     *   <li>Собирает параметры метода в Map</li>
     *   <li>Создает обертку для выполнения оригинального метода</li>
     *   <li>Передает собранные параметры(args) в ILoggerService</li>
     *   <li>Делегирует логирование в ILoggerService</li>
     * </ol>
     */
    @Around("@annotation(loggerAnnotation)")
    public Object logIntegrationCall(ProceedingJoinPoint joinPoint, ILogger loggerAnnotation)
                                                                                    throws Throwable {

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        Object[] args = joinPoint.getArgs();

        Map<String, Object> params = new LinkedHashMap<>();
        Parameter[] parameters = method.getParameters();
        for (int i = 0; i < parameters.length; i++) {
            params.put(parameters[i].getName(), args[i]);
        }

        return loggerService.logCall(
            () -> {
                try {
                    return joinPoint.proceed();
                } catch(Exception e) {
                    throw new Exception(e.getMessage());
                }
                catch (Throwable e) {
                    throw new RuntimeException(e.getMessage());
                }
            },
            method.getName(),
            params,
            loggerAnnotation.targetService()
        );
    }
}
