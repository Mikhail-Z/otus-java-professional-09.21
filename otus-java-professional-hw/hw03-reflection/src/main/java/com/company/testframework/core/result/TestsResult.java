package com.company.testframework.core.result;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class TestsResult {
    private final Map<String, TestResult> results;
    private final boolean testsWereInvoked;

    public TestsResult(Map<String, TestResult> results) {
        this.results = results;
        testsWereInvoked = true;
    }

    private TestsResult(Map<String, TestResult> results, boolean testsWereInvoked) {
        this.results = results;
        this.testsWereInvoked = testsWereInvoked;
    }

    public static TestsResult empty() {
        return new TestsResult(Collections.emptyMap(), false);
    }

    public TestResult get(String methodName) {
        return results.get(methodName);
    }

    public Set<Map.Entry<String, TestResult>> getAll() {
        return results.entrySet();
    }

    public boolean areAllTestsSuccess() {
        return results.values()
                .stream()
                .filter(x -> x == TestResult.SUCCESS)
                .count() == results.values().size();
    }
}
