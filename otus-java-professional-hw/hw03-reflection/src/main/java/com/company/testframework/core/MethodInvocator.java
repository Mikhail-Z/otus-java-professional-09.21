package com.company.testframework.core;

import java.lang.reflect.Method;

public class MethodInvocator {
    public boolean invoke(Object testClassInstance, Method method) {
        method.setAccessible(true);
        try {
            method.invoke(testClassInstance);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
