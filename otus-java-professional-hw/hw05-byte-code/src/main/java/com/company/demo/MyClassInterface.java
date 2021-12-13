package com.company.demo;

public interface MyClassInterface {
    void method(String s);
    void method(String s, int n);
    int methodWithAgeReturn(String s, Data data);
    int badMethod(int n) throws Exception;
}
