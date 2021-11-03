package com.company;

import com.company.testexamples.TestExample;
import com.company.testframework.print.DefaultTestsResultPrinter;

public class Main {
    public static void main(String[] args) throws NoSuchMethodException {
        var printer = new DefaultTestsResultPrinter();
        TestsExecution.execute(TestExample.class, printer);
    }
}
