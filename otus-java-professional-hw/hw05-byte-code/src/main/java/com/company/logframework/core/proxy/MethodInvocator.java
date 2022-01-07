package com.company.logframework.core.proxy;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class MethodInvocator {
    public Object invoke(Object object, Method method, Object... args) throws InvocationTargetException, IllegalAccessException {
        return method.invoke(object, args);
    }
}
