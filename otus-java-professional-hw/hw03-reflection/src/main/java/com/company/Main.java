package com.company;

import com.company.core.TestsRunner;
import com.company.testexamples.TestExample;

public class Main {
    public static void main(String[] args) {
        var runner = new TestsRunner(new TestExample());
        runner.run();
        runner.printResults();
    }
}
