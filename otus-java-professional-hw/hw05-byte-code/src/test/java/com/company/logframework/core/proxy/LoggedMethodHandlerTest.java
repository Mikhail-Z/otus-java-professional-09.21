package com.company.logframework.core.proxy;


import com.company.logframework.core.proxy.testdata.TestClassImpl;
import com.company.logframework.logger.LogLevel;
import com.company.logframework.logger.Logger;
import com.company.logframework.logger.StdoutLogger;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class LoggedMethodHandlerTest {
    private Logger loggerSpy = spy(new StdoutLogger());
    private TestClassImpl testClassImpl = new TestClassImpl();
    private MethodInvocator methodInvocatorSpy = spy(new MethodInvocator());

    private LoggedMethodHandler<TestClassImpl> handler;

    @BeforeEach
    void setUp() {
        handler = new LoggedMethodHandler<>(testClassImpl, loggerSpy, methodInvocatorSpy);
    }

    @Test
    void handle_methodOverloaded1() throws Throwable {
        var method = testClassImpl.getClass().getMethod("method", String.class);
        String[] args = new String[]{"abc"};
        handler.invoke(testClassImpl, method, args);

        verify(methodInvocatorSpy).invoke(eq(testClassImpl), eq(method), any());
        verify(loggerSpy, times(2)).log(anyString(), eq(LogLevel.INFO));
    }

    @Test
    void handle_methodOverloaded2() throws Throwable {
        var method = testClassImpl.getClass().getMethod("method", int.class);
        Object[] args = new Object[]{1};
        handler.invoke(testClassImpl, method, args);

        verify(methodInvocatorSpy).invoke(eq(testClassImpl), eq(method), any());
        verify(loggerSpy, times(0)).log(anyString(), any(LogLevel.class));
    }

    @Test
    void handle_methodOverloaded3() throws Throwable {
        var method = testClassImpl.getClass().getMethod("method", String.class, int.class);
        Object[] args = new Object[]{"abc", 1};
        var result = handler.invoke(testClassImpl, method, args);

        Assertions.assertEquals(2, result);
        verify(methodInvocatorSpy).invoke(eq(testClassImpl), eq(method), any());
        verify(loggerSpy, times(2)).log(anyString(), eq(LogLevel.INFO));
    }

    @Test
    void handle_badMethod() throws Throwable {
        var method = testClassImpl.getClass().getMethod("badMethod", int.class);
        Object[] args = new Object[]{1};

        Assertions.assertThrows(Exception.class, () -> handler.invoke(testClassImpl, method, args));
        verify(methodInvocatorSpy).invoke(eq(testClassImpl), eq(method), any());
        verify(loggerSpy, times(1)).log(anyString(), eq(LogLevel.INFO));
        verify(loggerSpy, times(1)).log(anyString(), eq(LogLevel.ERROR));
    }
}
