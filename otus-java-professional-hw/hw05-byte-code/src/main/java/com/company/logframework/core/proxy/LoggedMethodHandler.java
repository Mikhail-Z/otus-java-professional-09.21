package com.company.logframework.core.proxy;

import com.company.logframework.core.annotations.Log;
import com.company.logframework.logger.LogLevel;
import com.company.logframework.logger.Logger;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class LoggedMethodHandler<T> implements InvocationHandler {
    private final Logger logger;
    private final T proxiedObject;
    private final List<Method> loggedMethods;
    private final MethodInvocator methodInvocator;

    public LoggedMethodHandler(T proxiedObject, Logger logger, MethodInvocator methodInvocator) {
        this.proxiedObject = proxiedObject;
        this.loggedMethods = Arrays.stream(proxiedObject.getClass().getMethods())
                .filter(m -> m.isAnnotationPresent(Log.class))
                .toList();
        this.logger = logger;
        this.methodInvocator = methodInvocator;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Optional<Method> targetMethodOptional = findLoggedMethod(method);
        if (targetMethodOptional.isPresent()) {
            Method targetMethod = targetMethodOptional.get();
            var logAnnotation = targetMethod.getAnnotation(Log.class);
            return logAndInvokeMethod(method, args, logAnnotation.logLevel());
        }


        return methodInvocator.invoke(proxiedObject, method, args);
    }

    private Object logAndInvokeMethod(Method targetMethod, Object[] args, LogLevel logLevel) throws Throwable {
        Object result;
        logger.log(String.format("%s.%s args: %s", targetMethod.getClass().getSimpleName(), targetMethod.getName(), Arrays.toString(args)), logLevel);
        try {
            result = methodInvocator.invoke(proxiedObject, targetMethod, args);
        }
        catch (Exception e) {
            logger.log(String.format("%s.%s throws exception: %s", targetMethod.getClass().getSimpleName(), targetMethod.getName(), e.getCause()), LogLevel.ERROR);
            throw e;
        }
        logger.log(String.format("%s.%s returns: %s", targetMethod.getClass().getSimpleName(), targetMethod.getName(), result), logLevel);
        return result;
    }

    private Optional<Method> findLoggedMethod(Method interfaceMethod) {
        return loggedMethods.stream()
                .filter(m -> m.getName().equals(interfaceMethod.getName())
                        && Arrays.equals(m.getParameterTypes(), interfaceMethod.getParameterTypes()))
                .findFirst();
    }

    @Override
    public String toString() {
        return "AnotherHandler{" +
                "myClass=" + proxiedObject +
                '}';
    }
}
