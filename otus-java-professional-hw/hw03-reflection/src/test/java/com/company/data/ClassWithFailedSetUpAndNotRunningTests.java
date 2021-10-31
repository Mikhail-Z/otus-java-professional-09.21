package com.company.data;

import com.company.annotations.After;
import com.company.annotations.Before;
import com.company.annotations.Test;

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
