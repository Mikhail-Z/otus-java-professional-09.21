package com.company.testframework.core;

import com.company.testframework.core.result.TestResult;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
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


    /*public TestResult invoke(Object testClassInstance, Method setUpMethod, Method testMethod, Method tearDownMethod) throws Exception {

        try {
            if (setUpMethod != null) {
                invokeBeforeOrAfterMethod(testClassInstance, setUpMethod);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            try {
                if (tearDownMethod != null) {
                    invokeBeforeOrAfterMethod(testClassInstance, tearDownMethod);
                }
            }
            catch (Exception e2) {
                e2.printStackTrace();
            }

            return TestResult.NOT_STARTED;
        }
        TestResult testResult = null;
        try {
            testMethod.setAccessible(true);
            testMethod.invoke(testClassInstance);
            testResult = TestResult.SUCCESS;

        }
        catch (Exception e) {
           testResult = TestResult.FAILED;
        }
        finally {
            try {
                if (tearDownMethod != null) {
                    invokeBeforeOrAfterMethod(testClassInstance, tearDownMethod);
                }
            }
            catch (Exception e) {
                if (testResult == TestResult.SUCCESS) {
                    testResult = TestResult.SUCCESS_WITH_FAILED_TEAR_DOWN;
                }
            }
        }
        return testResult;
    }

    private boolean isValidTestOrBeforeAfterMethod(Method method) {
        return method.getReturnType() == Void.class || method.getParameterTypes().length == 0;
    }

    private void invokeBeforeOrAfterMethod(final Object testClassInstance, Method method) throws Exception {
        if (!isValidTestOrBeforeAfterMethod(method)) {
            throw new InvalidMethodException(String.format("@Before or @After method %s has invalid signature %n", method.getName()));
        }
        try {
            method.setAccessible(true);
            method.invoke(testClassInstance);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    static class InvalidMethodException extends RuntimeException {
        public InvalidMethodException(String message) {
            super(message);
        }
    }*/
}
