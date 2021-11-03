package com.company.testframework.core;

import com.company.testframework.core.executioncontext.TestContextBuilder;
import com.company.testframework.core.result.TestResult;
import com.company.testframework.core.result.TestsResult;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

public class TestsRunner {

    private TestInvocator testInvocator;
    private TestContextBuilder testContextBuilder;

    private TestsRunner() {
    }

    public TestsRunner(TestInvocator testInvocator, TestContextBuilder testContextBuilder) {
        this.testInvocator = testInvocator;
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
                resultsMap.put(testMethod.getName(), TestResult.NOT_STARTED);
                var result = testInvocator.invoke(testConstructor, context.getSetUpMethod(), testMethod, context.getTearDownMethod());
                resultsMap.put(testMethod.getName(), result);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        return new TestsResult(resultsMap);
    }
}
