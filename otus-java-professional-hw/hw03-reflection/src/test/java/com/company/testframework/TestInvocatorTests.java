package com.company.testframework;


import com.company.data.ClassWithOneFailedTest;
import com.company.data.ClassWithSuccessTestsAndSetupsAndTearDowns;
import com.company.testframework.core.MethodInvocator;
import com.company.testframework.core.result.TestResult;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class TestInvocatorTests {
    private Constructor<?> constructor;
    private Method successMethod1;
    private Method successMethod2;
    private Method successMethod3;
    private Method failureMethod1;
    private Method failureMethod2;
    private Method failureMethod3;

    public TestInvocatorTests() throws NoSuchMethodException {
        constructor = ClassWithSuccessTestsAndSetupsAndTearDowns.class.getDeclaredConstructor();

        successMethod1 = ClassWithSuccessTestsAndSetupsAndTearDowns.class.getDeclaredMethod("setUp");
        successMethod2 = ClassWithSuccessTestsAndSetupsAndTearDowns.class.getDeclaredMethod("successMethod1");
        successMethod3 = ClassWithSuccessTestsAndSetupsAndTearDowns.class.getDeclaredMethod("tearDown");

        failureMethod1 = ClassWithOneFailedTest.class.getDeclaredMethod("failedMethod1");
        failureMethod2 = ClassWithOneFailedTest.class.getDeclaredMethod("failedMethod1");
        failureMethod3 = ClassWithOneFailedTest.class.getDeclaredMethod("failedMethod1");
    }

    /*@org.junit.jupiter.api.Test
    @org.junit.jupiter.api.DisplayName("Метод до теста и метод после тесты есть и успешны, тест тоже успешен")
    public void test_whenSetUpSuccessAndTestSuccessAndTearDownSuccess() throws Exception {
        var spy = spy((ClassWithSuccessTestsAndSetupsAndTearDowns)constructor.newInstance());
        var testResult = new MethodInvocator().invoke(spy, successMethod1, successMethod2, successMethod3);
        verify(spy, times(1)).successMethod1();
        verify(spy, times(1)).setUp();
        verify(spy, times(1)).tearDown();
        assertEquals(TestResult.SUCCESS, testResult);
    }*/

    /*@org.junit.jupiter.api.Test
    @org.junit.jupiter.api.DisplayName("Метод до теста кидает исключение, тест не выполняется, метод после теста успешен")
    public void test_whenSetUpThrowsExceptionAndTestDoesntExecuteAndTearDownSuccess() throws Exception {
        var testResult = new TestInvocator().invoke(constructor.newInstance(), failureMethod1, successMethod2, successMethod3);
        assertEquals(TestResult.NOT_STARTED, testResult);
    }

    @org.junit.jupiter.api.Test
    @org.junit.jupiter.api.DisplayName("Метод до теста кидает исключение, тест не выполняется, метод после теста кидает исключение")
    public void test_whenSetUpThrowsExceptionAndTestDoesntExecuteAndTearDownThrowsException() throws Exception {
        var testResult = new TestInvocator().invoke(constructor.newInstance(), failureMethod1, successMethod2, failureMethod3);
        assertEquals(TestResult.NOT_STARTED, testResult);
    }

    @org.junit.jupiter.api.Test
    @org.junit.jupiter.api.DisplayName("Метод до теста успешен, тест кидает исключение, метод после теста успешен")
    public void test_whenSetUpSuccessAndTestDontExecuteAndTearDownThrowsException() throws Exception {
        var testResult = new TestInvocator().invoke(constructor.newInstance(), successMethod1, failureMethod2, successMethod3);
        assertEquals(TestResult.FAILED, testResult);
    }

    @org.junit.jupiter.api.Test
    @org.junit.jupiter.api.DisplayName("Метод до теста успешен, тест кидает исключение, метод после теста кидает исключение")
    public void test_whenSetUpSuccessAndTestThrowsExceptionAndTearDownThrowsException() throws Exception {
        var testResult = new TestInvocator().invoke(constructor.newInstance(), successMethod1, failureMethod2, failureMethod3);
        assertEquals(TestResult.FAILED, testResult);
    }

    @org.junit.jupiter.api.Test
    @org.junit.jupiter.api.DisplayName("Метод до теста успешен, тест успешен, метод после теста кидает исключение")
    public void test_whenSetUpSuccessAndTestSuccessAndTearDownThrowsException() throws Exception {
        var testResult = new TestInvocator().invoke(constructor.newInstance(), successMethod1, successMethod2, failureMethod3);
        assertEquals(TestResult.SUCCESS_WITH_FAILED_TEAR_DOWN, testResult);
    }

    @org.junit.jupiter.api.Test
    @org.junit.jupiter.api.DisplayName("Метода до теста нет, тест успешен, метод после теста успешен")
    public void test_whenSetUpNotPresentAndTestSuccessAndAndTearDownSuccess() throws Exception {
        var testResult = new TestInvocator().invoke(constructor.newInstance(), null, successMethod2, successMethod3);
        assertEquals(TestResult.SUCCESS, testResult);
    }

    @org.junit.jupiter.api.Test
    @org.junit.jupiter.api.DisplayName("Метод до теста успешен, тест успешен, метода после теста нет")
    public void test_whenSetUpSuccessAndTestSuccessAndAndTearDownNotPresent() throws Exception {
        var testResult = new TestInvocator().invoke(constructor.newInstance(), successMethod1, successMethod2, null);
        assertEquals(TestResult.SUCCESS, testResult);
    }*/
}
