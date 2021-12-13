package com.company.logframework.core.proxy.testdata;

import com.company.demo.Data;
import com.company.logframework.core.annotations.Log;

public class TestClassImpl implements TestClassInterface {
    @Override
    @Log
    public void method(String s) {

    }

    @Override
    public void method(int n) {

    }

    @Override
    @Log
    public int method(String s, int n) {
        return n+1;
    }

    @Override
    @Log
    public int badMethod(int n) throws Exception {
        throw new Exception("!!!");
    }
}
