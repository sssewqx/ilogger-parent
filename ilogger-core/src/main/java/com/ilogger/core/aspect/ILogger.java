package com.ilogger.core.aspect;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Аннотация-маркер для IAspectLogger
 * <p>
 * Принимает в себя параметр: "targetService",
 * в нём нужно указать название сервиса к которому будет обращаться метод
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ILogger {
    String targetService();
}

