package com.ilogger.core.services.interfaces;

import java.util.Map;
import java.util.concurrent.Callable;

public interface ILoggerService {

    Object logCall(Callable<Object> operation,
                   String methodName,
                   Map<String, Object> args,
                   String targetService) throws Exception;
}