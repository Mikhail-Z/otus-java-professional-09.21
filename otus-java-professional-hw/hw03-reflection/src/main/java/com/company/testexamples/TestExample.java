package com.company.testexamples;

import com.company.testframework.annotations.After;
import com.company.testframework.annotations.Before;
import com.company.testframework.annotations.Test;
import static org.assertj.core.api.AssertionsForClassTypes.*;

public class TestExample {
    @Before
    void setUp() {
        System.out.println("set up");
    }

    @Test
    void equalsMethod() {
        System.out.println("equalsMethod");
        assertThat("").isEqualTo("");
    }

    @Test
    void nonEqualsMethod() {
        System.out.println("nonEqualsMethod");
        assertThat("abc").isEqualTo("");
    }

    @After
    void tearDown() {
        System.out.println("tear down");
    }

}
