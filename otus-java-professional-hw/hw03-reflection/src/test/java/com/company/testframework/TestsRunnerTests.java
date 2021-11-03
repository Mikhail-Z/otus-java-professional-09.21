package com.company.testframework;

import com.company.data.*;
import com.company.testframework.core.TestInvocator;
import com.company.testframework.core.TestsRunner;
import com.company.testframework.core.executioncontext.TestContext;
import com.company.testframework.core.result.TestResult;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestsRunnerTests {
    @org.junit.jupiter.api.Test
    void test_allSuccessTests() {
        var runner = new TestsRunner(new TestInvocator(), TestContext.builder());
        var results = runner.run(ClassWithAllSuccessTestsWithoutSetUpsOrTearDowns.class);
        assertEquals(TestResult.SUCCESS, results.get("successMethod1"));
        assertEquals(TestResult.SUCCESS, results.get("successMethod2"));
    }

    @org.junit.jupiter.api.Test
    void test_oneFailedSetUpAndNotRunningTests() {
        var runner = new TestsRunner(new TestInvocator(), TestContext.builder());
        var results = runner.run(ClassWithFailedSetUpAndNotRunningTests.class);
        assertEquals(TestResult.NOT_STARTED, results.get("successMethod1"));
    }

    @org.junit.jupiter.api.Test
    void test_oneFailedTearDownAndSuccessTests() {
        var runner = new TestsRunner(new TestInvocator(), TestContext.builder());
        var results = runner.run(ClassWithFailedTearDownAndSuccessTests.class);
        assertEquals(TestResult.SUCCESS_WITH_FAILED_TEAR_DOWN, results.get("successMethod1"));
    }

    @org.junit.jupiter.api.Test
    void test_oneFailedTest() {
        var runner = new TestsRunner(new TestInvocator(), TestContext.builder());
        var results = runner.run(ClassWithOneFailedTest.class);
        assertEquals(TestResult.FAILED, results.get("failedMethod1"));
        assertEquals(TestResult.SUCCESS, results.get("successMethod2"));
    }

    @org.junit.jupiter.api.Test
    void test_successTestsAndSetupsAndTearDowns() {
        var runner = new TestsRunner(new TestInvocator(), TestContext.builder());
        var results = runner.run(ClassWithSuccessTestsAndSetupsAndTearDowns.class);
        assertEquals(TestResult.SUCCESS, results.get("successMethod1"));
        assertEquals(TestResult.SUCCESS, results.get("successMethod2"));
    }
}
