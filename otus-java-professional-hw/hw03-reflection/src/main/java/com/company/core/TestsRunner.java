package com.company.core;

import com.company.annotations.After;
import com.company.annotations.Before;
import com.company.annotations.Test;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import static com.company.utils.Utils.getMethodAnnotatedWith;
import static com.company.utils.Utils.getMethodsAnnotatedWith;

public class TestsRunner {

    private final Map<String, TestResult> testResults = new HashMap<>();
    private List<Object> testClassInstances = new ArrayList<>();
    private List<Method> testMethods;
    private Class<?> testClass;
    private TestsRunner() {
    }

    public TestsRunner(Class<?> testClass) throws ClassNotFoundException {
        this.testClass = testClass;
        testMethods = getMethodsAnnotatedWith(testClass, Test.class);
        IntStream.range(0, testMethods.size())
                .forEach(i -> {
                    try {
                        Object testClassInstance = testClass.getDeclaredConstructor().newInstance();
                        testClassInstances.add(testClassInstance);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    testResults.put(testMethods.get(i).getName(), TestResult.NOT_STARTED);
                });
    }

    public TestsRunner(String fullClassName) throws ClassNotFoundException {
        this(Class.forName(fullClassName));
    }

    //for using mock test class instances
    public void setTestClassInstances(Object... testClassInstances) {
        if (testClassInstances.length != this.testClassInstances.size())
            throw new IllegalArgumentException("number of test class instances is not equal to number of test methods");
        IntStream.range(0, this.testClassInstances.size())
                .forEach(i -> {
                    this.testClassInstances.set(i, testClassInstances[i]);
                });
    }

    public Map<String, TestResult> getTestResults() {
        return testResults;
    }

    public void run() {
        var setUpMethod = getMethodAnnotatedWith(testClass, Before.class);
        var tearDownMethod = getMethodAnnotatedWith(testClass, After.class);

        for (int i = 0; i < testMethods.size(); i++) {
            try {
                if (setUpMethod != null) invokeBeforeOrAfterMethod(testClassInstances.get(i), setUpMethod);
            }
            catch (Exception e) {
                e.printStackTrace();
                return;
            }

            var method = testMethods.get(i);
            try {
                method.setAccessible(true);
                method.invoke(testClassInstances.get(i));
                testResults.put(method.getName(), TestResult.SUCCESS);
            } catch (Exception e) {
                testResults.put(method.getName(), TestResult.FAILED);
                e.printStackTrace();
            } finally {
                try {
                    if (tearDownMethod != null) invokeBeforeOrAfterMethod(testClassInstances.get(i), tearDownMethod);
                } catch (Exception e) {
                    testResults.put(method.getName(), TestResult.SUCCESS_WITH_FAILED_TEAR_DOWN);
                    e.printStackTrace();
                }
            }
        }
    }

    public void printResults() {
        var allTestsNumber = testResults.values().size();
        var successTestsNumber = testResults.values()
                .stream()
                .filter(r -> r == TestResult.SUCCESS)
                .count();
        System.out.printf("passed %d/%d tests%n", successTestsNumber, allTestsNumber);
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
