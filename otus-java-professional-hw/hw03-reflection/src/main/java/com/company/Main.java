package com.company;

import com.company.core.TestsRunner;
import com.company.testexamples.TestExample;

public class Main {
    public static void main(String[] args) {
        TestsRunner runner = null;
        try {
            runner = new TestsRunner(TestExample.class);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return;
        }

        runner.run();
        runner.printResults();
    }
}
