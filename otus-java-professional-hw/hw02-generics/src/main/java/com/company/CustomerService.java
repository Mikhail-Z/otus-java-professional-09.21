package com.company;

import java.util.*;

public class CustomerService {

    //важно подобрать подходящую Map-у, посмотрите на редко используемые методы, они тут полезны

    private final Map<Customer, String> customerMap = new TreeMap<>();

    public Map.Entry<Customer, String> getSmallest() {
        return customerMap.entrySet()
                .stream()
                .min(Map.Entry.comparingByKey())
                .map(CustomerService::copyFunc)
                .orElse(null);
    }

    public Map.Entry<Customer, String> getNext(Customer customer) {
        return customerMap.entrySet()
                .stream()
                .filter(e -> e.getKey().getScores() > customer.getScores())
                .findFirst()
                .map(CustomerService::copyFunc)
                .orElse(null);
    }

    public void add(Customer customer, String data) {
        customerMap.put(customer, data);
    }

    private static Map.Entry<Customer, String> copyFunc(Map.Entry<Customer, String> entry) {
        var customerCopy = new Customer(entry.getKey());
        return new AbstractMap.SimpleEntry<>(customerCopy, entry.getValue());
    }
}
