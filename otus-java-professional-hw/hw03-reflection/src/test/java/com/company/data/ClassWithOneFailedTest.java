package com.company.data;

import com.company.annotations.Test;

public class ClassWithOneFailedTest {
    @Test
    public void failedMethod1() {
        throw new RuntimeException();
    }

    @Test
    public void successMethod2() {

    }
}
