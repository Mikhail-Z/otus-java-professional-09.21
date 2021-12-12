package com.company.demo;

public class Data {
    private final int age;
    private final String name;

    public Data(int age, String name) {
        this.age = age;
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Data{" +
                "age=" + age +
                ", name='" + name + '\'' +
                '}';
    }
}
