package com.company.testframework.core.executioncontext;

import com.company.testframework.annotations.After;
import com.company.testframework.annotations.Before;
import com.company.testframework.annotations.Test;

import static com.company.utils.Utils.getMethodAnnotatedWith;
import static com.company.utils.Utils.getMethodsAnnotatedWith;

public class TestContextBuilder {
    public TestContext build(Class<?> testClass) {
        var tests = getMethodsAnnotatedWith(testClass, Test.class);
        var setUp = getMethodAnnotatedWith(testClass, Before.class);
        var tearDown = getMethodAnnotatedWith(testClass, After.class);
        return new TestContext(tests, setUp, tearDown);
    }
}
