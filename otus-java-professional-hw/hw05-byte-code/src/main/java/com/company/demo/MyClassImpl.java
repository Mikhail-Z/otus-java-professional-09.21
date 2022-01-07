package com.company.demo;

import com.company.logframework.core.annotations.Log;

public class MyClassImpl implements MyClassInterface {

    @Override
    @Log
    public void method(String s) {
        System.out.println("method(String)");
    }

    @Override
    public void method(String s, int n) {
        System.out.println("method(String, Int)");
    }

    @Override
    @Log
    public int methodWithAgeReturn(String s, Data data) {
        System.out.println("method(String, Data)");
        return data.getAge();
    }

    @Override
    @Log
    public int badMethod(int n) throws Exception {
        throw new Exception("bad method exception");
    }
}
