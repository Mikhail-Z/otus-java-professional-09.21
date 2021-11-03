package com.company;

import com.company.testframework.core.TestInvocator;
import com.company.testframework.core.TestsRunner;
import com.company.testframework.core.executioncontext.TestContext;
import com.company.testframework.core.executioncontext.TestContextBuilder;
import com.company.testframework.print.TestsResultPrinter;

public class TestsExecution {
    public static void execute(Class<?> klass, TestsResultPrinter printer) throws NoSuchMethodException {
        var runner = new TestsRunner(new TestInvocator(), TestContext.builder());
        var results = runner.run(klass);
        printer.print(results);
    }
}
