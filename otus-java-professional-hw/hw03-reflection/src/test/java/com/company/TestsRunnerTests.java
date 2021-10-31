package com.company;

import com.company.core.TestResult;
import com.company.core.TestsRunner;
import com.company.data.*;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TestsRunnerTests {
    @org.junit.jupiter.api.Test
    void test_allSuccessTests() throws ClassNotFoundException {
        var runner = new TestsRunner(ClassWithAllSuccessTestsWithoutSetUpsOrTearDowns.class);
        var instanceWithSuccessMethod1 = spy(new ClassWithAllSuccessTestsWithoutSetUpsOrTearDowns());
        var instanceWithSuccessMethod2 = spy(new ClassWithAllSuccessTestsWithoutSetUpsOrTearDowns());

        runner.setTestClassInstances(instanceWithSuccessMethod1, instanceWithSuccessMethod2);
        runner.run();

        verify(instanceWithSuccessMethod1, times(1)).successMethod1();
        verify(instanceWithSuccessMethod2, times(0)).successMethod1();
        verify(instanceWithSuccessMethod1, times(0)).successMethod2();
        verify(instanceWithSuccessMethod2, times(1)).successMethod2();
        Map<String, TestResult> results = runner.getTestResults();
        assertEquals(2, results.size());
        assertEquals(TestResult.SUCCESS, results.get("successMethod1"));
        assertEquals(TestResult.SUCCESS, results.get("successMethod2"));
        assertTrue(runner.areAllTestsSuccess());
    }

    @org.junit.jupiter.api.Test
    void test_oneFailedSetUpAndNotRunningTests() throws ClassNotFoundException {
        var runner = new TestsRunner(ClassWithFailedSetUpAndNotRunningTests.class.getName());
        var instanceWithNotRunningMethod = spy(new ClassWithFailedSetUpAndNotRunningTests());

        runner.setTestClassInstances(instanceWithNotRunningMethod);
        runner.run();

        verify(instanceWithNotRunningMethod, times(1)).setUp();
        verify(instanceWithNotRunningMethod, times(0)).successMethod1();
        verify(instanceWithNotRunningMethod, times(0)).tearDown();

        Map<String, TestResult> results = runner.getTestResults();
        assertEquals(1, results.size());
        assertEquals(TestResult.NOT_STARTED, results.get("successMethod1"));
        assertFalse(runner.areAllTestsSuccess());
    }

    @org.junit.jupiter.api.Test
    void test_oneFailedTearDownAndSuccessTests() throws ClassNotFoundException {
        var runner = new TestsRunner(ClassWithFailedTearDownAndSuccessTests.class.getName());
        var instanceWithSuccessMethod = spy(new ClassWithFailedTearDownAndSuccessTests());
        runner.setTestClassInstances(instanceWithSuccessMethod);
        runner.run();

        verify(instanceWithSuccessMethod, times(1)).setUp();
        verify(instanceWithSuccessMethod, times(1)).successMethod1();
        verify(instanceWithSuccessMethod, times(1)).tearDown();
        Map<String, TestResult> results = runner.getTestResults();
        assertEquals(1, results.size());
        assertEquals(TestResult.SUCCESS_WITH_FAILED_TEAR_DOWN, results.get("successMethod1"));
        assertFalse(runner.areAllTestsSuccess());
    }

    @org.junit.jupiter.api.Test
    void test_oneFailedTest() throws ClassNotFoundException {
        var runner = new TestsRunner(ClassWithOneFailedTest.class.getName());
        var instanceWithSuccessMethod = spy(new ClassWithOneFailedTest());
        var instanceWithFailedMethod = spy(new ClassWithOneFailedTest());
        runner.setTestClassInstances(instanceWithSuccessMethod, instanceWithFailedMethod);
        runner.run();

        verify(instanceWithSuccessMethod, times(1)).successMethod2();
        verify(instanceWithSuccessMethod, times(0)).failedMethod1();
        verify(instanceWithFailedMethod, times(1)).failedMethod1();
        verify(instanceWithFailedMethod, times(0)).successMethod2();
        Map<String, TestResult> results = runner.getTestResults();
        assertEquals(2, results.size());
        assertEquals(TestResult.FAILED, results.get("failedMethod1"));
        assertEquals(TestResult.SUCCESS, results.get("successMethod2"));
        assertFalse(runner.areAllTestsSuccess());
    }



    @org.junit.jupiter.api.Test
    void test_successTestsAndSetupsAndTearDowns() throws ClassNotFoundException {
        var runner = new TestsRunner(ClassWithSuccessTestsAndSetupsAndTearDowns.class.getName());
        var instanceWithSuccessMethod1 = spy(new ClassWithSuccessTestsAndSetupsAndTearDowns());
        var instanceWithSuccessMethod2 = spy(new ClassWithSuccessTestsAndSetupsAndTearDowns());
        runner.setTestClassInstances(instanceWithSuccessMethod1, instanceWithSuccessMethod2);
        runner.run();

        verify(instanceWithSuccessMethod1, times(1)).setUp();
        verify(instanceWithSuccessMethod2, times(1)).setUp();
        verify(instanceWithSuccessMethod1, times(1)).successMethod1();
        verify(instanceWithSuccessMethod2, times(1)).successMethod2();
        verify(instanceWithSuccessMethod1, times(1)).tearDown();
        verify(instanceWithSuccessMethod2, times(1)).tearDown();
        Map<String, TestResult> results = runner.getTestResults();
        assertEquals(2, results.size());
        assertEquals(TestResult.SUCCESS, results.get("successMethod1"));
        assertEquals(TestResult.SUCCESS, results.get("successMethod2"));
        assertTrue(runner.areAllTestsSuccess());
    }
}
