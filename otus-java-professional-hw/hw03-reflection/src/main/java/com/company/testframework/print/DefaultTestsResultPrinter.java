package com.company.testframework.print;

import com.company.testframework.core.result.TestResult;
import com.company.testframework.core.result.TestsResult;

public class DefaultTestsResultPrinter implements TestsResultPrinter {
    @Override
    public void print(TestsResult testsResult) {
        var successTestsNumber = testsResult.getAll()
                .stream()
                .filter(r -> r.getValue() == TestResult.SUCCESS)
                .count();
        System.out.printf("passed %d/%d tests%n", successTestsNumber, testsResult.getAll().size());
        testsResult.getAll().forEach(entry -> System.out.printf("%-30s %s%n", entry.getKey(), entry.getValue()));
    }
}
