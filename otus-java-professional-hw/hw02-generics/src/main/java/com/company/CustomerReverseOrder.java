package com.company;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Stack;

public class CustomerReverseOrder {

    //надо подобрать подходящую структуру данных, тогда решение будет в "две строчки"

    private final Deque<Customer> customers = new ArrayDeque<>();

    public void add(Customer customer) {
        customers.addFirst(customer);
    }

    public Customer take() {
        return customers.pop();
    }
}