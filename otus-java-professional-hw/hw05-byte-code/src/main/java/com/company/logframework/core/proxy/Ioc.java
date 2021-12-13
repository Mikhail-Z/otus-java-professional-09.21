package com.company.logframework.core.proxy;

import com.company.logframework.logger.Logger;
import com.company.logframework.logger.StdoutLogger;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Proxy;

public class Ioc {
    private Ioc() {
    }

    public static <T> T createLoggingProxy(Class<? extends T> interfaceClass, Class<? extends T> implementationClass, Logger logger) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        InvocationHandler handler = new LoggedMethodHandler<>(implementationClass.getConstructor().newInstance(), logger, new MethodInvocator());
        var proxy = Proxy.newProxyInstance(Ioc.class.getClassLoader(),
                new Class<?>[]{interfaceClass}, handler);
        return (T) proxy;
    }
}
