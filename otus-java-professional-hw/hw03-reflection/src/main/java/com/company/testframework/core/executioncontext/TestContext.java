package com.company.testframework.core.executioncontext;

import java.lang.reflect.Method;
import java.util.List;

public class TestContext {

    private final Method setUp;
    private final Method tearDown;
    private final List<Method> tests;

    public TestContext(List<Method> tests, Method setUp, Method tearDown) {
        this.tests = tests;
        this.setUp = setUp;
        this.tearDown = tearDown;
    }

    public Method getSetUpMethod() {
        return setUp;
    }

    public Method getTearDownMethod() {
        return tearDown;
    }

    public List<Method> getTests() {
        return tests;
    }

    public static TestContextBuilder builder() {
        return new TestContextBuilder();
    }
}
