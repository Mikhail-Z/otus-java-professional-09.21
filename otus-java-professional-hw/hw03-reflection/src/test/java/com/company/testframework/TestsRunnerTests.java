package com.company.testframework;

import com.company.data.*;
import com.company.testframework.core.MethodInvocator;
import com.company.testframework.core.TestsRunner;
import com.company.testframework.core.executioncontext.TestContext;
import com.company.testframework.core.result.TestResult;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class TestsRunnerTests {
    @org.junit.jupiter.api.Test
    void test_allSuccessTests() throws NoSuchMethodException {
        var methodInvocatorSpy = spy(new MethodInvocator());
        var runner = new TestsRunner(methodInvocatorSpy, TestContext.builder());
        var successMethod1 = ClassWithAllSuccessTestsWithoutSetUpsOrTearDowns.class.getDeclaredMethod("successMethod1");
        var successMethod2 = ClassWithAllSuccessTestsWithoutSetUpsOrTearDowns.class.getDeclaredMethod("successMethod1");

        var results = runner.run(ClassWithAllSuccessTestsWithoutSetUpsOrTearDowns.class);

        verify(methodInvocatorSpy, times(1)).invoke(any(), eq(successMethod1));
        verify(methodInvocatorSpy, times(1)).invoke(any(), eq(successMethod2));
        assertEquals(TestResult.SUCCESS, results.get("successMethod1"));
        assertEquals(TestResult.SUCCESS, results.get("successMethod2"));
    }

    @org.junit.jupiter.api.Test
    void test_oneFailedSetUpAndNotRunningTests() throws NoSuchMethodException {
        var methodInvocatorSpy = spy(new MethodInvocator());
        var runner = new TestsRunner(methodInvocatorSpy, TestContext.builder());
        var failedSetUpMethod = ClassWithFailedSetUpAndNotRunningTests.class.getDeclaredMethod("setUp");
        var successMethod = ClassWithFailedSetUpAndNotRunningTests.class.getDeclaredMethod("successMethod1");
        var successTearDown = ClassWithFailedSetUpAndNotRunningTests.class.getDeclaredMethod("tearDown");

        var results = runner.run(ClassWithFailedSetUpAndNotRunningTests.class);

        verify(methodInvocatorSpy, times(1)).invoke(any(), eq(failedSetUpMethod));
        verify(methodInvocatorSpy, times(0)).invoke(any(), eq(successMethod));
        verify(methodInvocatorSpy, times(1)).invoke(any(), eq(successTearDown));

        assertEquals(TestResult.NOT_STARTED, results.get("successMethod1"));
    }

    @org.junit.jupiter.api.Test
    void test_oneFailedTearDownAndSuccessTests() throws NoSuchMethodException {
        var methodInvocatorSpy = spy(new MethodInvocator());
        var runner = new TestsRunner(methodInvocatorSpy, TestContext.builder());
        var setUp = ClassWithFailedTearDownAndSuccessTests.class.getDeclaredMethod("setUp");
        var successMethod2 = ClassWithFailedTearDownAndSuccessTests.class.getDeclaredMethod("successMethod1");
        var tearDown = ClassWithFailedTearDownAndSuccessTests.class.getDeclaredMethod("tearDown");

        var results = runner.run(ClassWithFailedTearDownAndSuccessTests.class);

        verify(methodInvocatorSpy, times(1)).invoke(any(), eq(setUp));
        verify(methodInvocatorSpy, times(1)).invoke(any(), eq(successMethod2));
        verify(methodInvocatorSpy, times(1)).invoke(any(), eq(tearDown));

        assertEquals(TestResult.SUCCESS_WITH_FAILED_TEAR_DOWN, results.get("successMethod1"));
    }

    @org.junit.jupiter.api.Test
    void test_oneFailedTest() throws NoSuchMethodException {
        var methodInvocatorSpy = spy(new MethodInvocator());
        var runner = new TestsRunner(methodInvocatorSpy, TestContext.builder());
        var failedMethod1 = ClassWithOneFailedTest.class.getDeclaredMethod("failedMethod1");
        var successMethod2 = ClassWithOneFailedTest.class.getDeclaredMethod("successMethod2");

        var results = runner.run(ClassWithOneFailedTest.class);

        verify(methodInvocatorSpy, times(1)).invoke(any(), eq(failedMethod1));
        verify(methodInvocatorSpy, times(1)).invoke(any(), eq(successMethod2));
        assertEquals(TestResult.FAILED, results.get("failedMethod1"));
        assertEquals(TestResult.SUCCESS, results.get("successMethod2"));
    }

    @org.junit.jupiter.api.Test
    void test_successTestsAndSetupsAndTearDowns() throws NoSuchMethodException {
        var methodInvocatorSpy = spy(new MethodInvocator());
        var runner = new TestsRunner(methodInvocatorSpy, TestContext.builder());
        var setUp = ClassWithSuccessTestsAndSetupsAndTearDowns.class.getDeclaredMethod("setUp");
        var successMethod1 = ClassWithSuccessTestsAndSetupsAndTearDowns.class.getDeclaredMethod("successMethod1");
        var successMethod2 = ClassWithSuccessTestsAndSetupsAndTearDowns.class.getDeclaredMethod("successMethod2");
        var tearDown = ClassWithSuccessTestsAndSetupsAndTearDowns.class.getDeclaredMethod("tearDown");

        var results = runner.run(ClassWithSuccessTestsAndSetupsAndTearDowns.class);

        verify(methodInvocatorSpy, times(2)).invoke(any(), eq(setUp));
        verify(methodInvocatorSpy, times(1)).invoke(any(), eq(successMethod1));
        verify(methodInvocatorSpy, times(1)).invoke(any(), eq(successMethod2));
        verify(methodInvocatorSpy, times(2)).invoke(any(), eq(tearDown));
        assertEquals(TestResult.SUCCESS, results.get("successMethod1"));
        assertEquals(TestResult.SUCCESS, results.get("successMethod2"));
    }
}
