package com.company.logframework.core.proxy.testdata;

import com.company.demo.Data;

public interface TestClassInterface {
    void method(String s);
    void method(int n);
    int method(String s, int n);
    int badMethod(int n) throws Exception;
}
