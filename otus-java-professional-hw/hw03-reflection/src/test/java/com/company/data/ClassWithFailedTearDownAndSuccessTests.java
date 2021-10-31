package com.company.data;

import com.company.annotations.After;
import com.company.annotations.Before;
import com.company.annotations.Test;

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
