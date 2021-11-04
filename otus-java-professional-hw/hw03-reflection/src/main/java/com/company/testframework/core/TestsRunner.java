package com.company.testframework.core;

import com.company.testframework.core.executioncontext.TestContextBuilder;
import com.company.testframework.core.result.TestResult;
import com.company.testframework.core.result.TestsResult;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class TestsRunner {

    private MethodInvocator methodInvocator;
    private TestContextBuilder testContextBuilder;

    private TestsRunner() {
    }

    public TestsRunner(MethodInvocator methodInvocator, TestContextBuilder testContextBuilder) {
        this.methodInvocator = methodInvocator;
        this.testContextBuilder = testContextBuilder;
    }

    public TestsResult run(Class<?> klass) {
        Constructor<?> testConstructor;
        try {
            testConstructor = klass.getConstructor();
        }
        catch (Exception e) {
            e.printStackTrace();
            return TestsResult.empty();
        }

        Map<String, TestResult> resultsMap = new HashMap<>();
        var context = testContextBuilder.build(klass);
        for (var testMethod : context.getTests()) {
            try {
                var result = executeOneTest(testConstructor, context.getSetUpMethod(), testMethod, context.getTearDownMethod());
                resultsMap.put(testMethod.getName(), result);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        return new TestsResult(resultsMap);
    }

    private TestResult executeOneTest(Constructor<?> constructor, Method setUp, Method test, Method tearDown) throws InvocationTargetException, InstantiationException, IllegalAccessException {
        var testClassInstance = constructor.newInstance();
        boolean setUpResult = false;
        boolean tearDownResult = false;
        boolean testResult = false;

        if (setUp == null) {
            setUpResult = true;
        } else {
            setUpResult = methodInvocator.invoke(testClassInstance, setUp);
        }

        if (setUpResult) {
            testResult = methodInvocator.invoke(testClassInstance, test);
        }

        if (tearDown == null) {
            tearDownResult = true;
        } else {
            tearDownResult = methodInvocator.invoke(testClassInstance, tearDown);
        }

        if (!setUpResult) {
            return TestResult.NOT_STARTED;
        } else {
            if (!testResult) {
                return TestResult.FAILED;
            } else {
                if (!tearDownResult) {
                    return TestResult.SUCCESS_WITH_FAILED_TEAR_DOWN;
                } else {
                    return TestResult.SUCCESS;
                }
            }
        }
    }
}
