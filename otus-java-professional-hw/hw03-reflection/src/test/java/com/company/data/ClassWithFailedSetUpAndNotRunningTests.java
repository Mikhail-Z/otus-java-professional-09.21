package com.company.data;

import com.company.testframework.annotations.After;
import com.company.testframework.annotations.Before;
import com.company.testframework.annotations.Test;

public class ClassWithFailedSetUpAndNotRunningTests {
    @Before
    public void setUp() {
        throw new RuntimeException("some exception");
    }

    @Test
    public void successMethod1() {

    }

    @After
    public void tearDown() {

    }
}
