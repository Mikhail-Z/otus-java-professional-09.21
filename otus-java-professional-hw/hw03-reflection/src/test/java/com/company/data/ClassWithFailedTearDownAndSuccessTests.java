package com.company.data;

import com.company.testframework.annotations.After;
import com.company.testframework.annotations.Before;
import com.company.testframework.annotations.Test;

public class ClassWithFailedTearDownAndSuccessTests {
    @Before
    public void setUp() {

    }

    @Test
    public void successMethod1() {

    }


    @After
    public void tearDown() {
        throw new RuntimeException("some exception");
    }
}
