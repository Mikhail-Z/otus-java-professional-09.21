package com.company.core;

import com.company.annotations.After;
import com.company.annotations.Before;
import com.company.annotations.Test;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.company.utils.Utils.getMethodAnnotatedWith;
import static com.company.utils.Utils.getMethodsAnnotatedWith;



public class TestsRunner {

    private final Map<String, TestResult> testResults = new HashMap<>();
    private Object testClassInstance;

    private TestsRunner() {
    }

    public TestsRunner(Object testClassInstance) {
        this.testClassInstance = testClassInstance;
    }

    public Map<String, TestResult> getTestResults() {
        return testResults;
    }

    public void run() {
        if (testClassInstance == null) throw new IllegalStateException("test class is not set");

        var setUpMethod = getMethodAnnotatedWith(testClassInstance.getClass(), Before.class);
        var testMethods = getMethodsAnnotatedWith(testClassInstance.getClass(), Test.class);
        var tearDownMethod = getMethodAnnotatedWith(testClassInstance.getClass(), After.class);


        testMethods.forEach(m -> testResults.put(m.getName(), TestResult.NOT_STARTED));
        testMethods.forEach(m -> {
            try {
                if (setUpMethod != null) invokeBeforeOrAfterMethod(testClassInstance, setUpMethod);
            }
            catch (Exception e) {
                e.printStackTrace();
                return;
            }

            try {
                m.setAccessible(true);
                m.invoke(testClassInstance);
                testResults.put(m.getName(), TestResult.SUCCESS);
            } catch (Exception e) {
                testResults.put(m.getName(), TestResult.FAILED);
                e.printStackTrace();
            } finally {
                try {
                    if (tearDownMethod != null) invokeBeforeOrAfterMethod(testClassInstance, tearDownMethod);
                } catch (Exception e) {
                    testResults.put(m.getName(), TestResult.SUCCESS_WITH_FAILED_TEAR_DOWN);
                    e.printStackTrace();
                }
            }
        });
    }

    public void printResults() {
        System.out.printf("All tests passed: %b%n", areAllTestsSuccess());
        testResults.forEach((key, value) -> System.out.printf("%-30s %s%n", key, value));
    }

    public boolean areAllTestsSuccess() {
        return testResults.values()
                .stream()
                .filter(x -> x == TestResult.SUCCESS)
                .count() == testResults.values().size();
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
    }
}
