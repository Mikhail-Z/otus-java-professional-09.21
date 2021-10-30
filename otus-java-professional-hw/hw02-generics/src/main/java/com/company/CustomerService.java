package com.company;

import java.util.*;

public class CustomerService {

    //важно подобрать подходящую Map-у, посмотрите на редко используемые методы, они тут полезны

    private final TreeMap<Customer, String> customerMap = new TreeMap<>(Comparator.comparingLong(Customer::getScores));

    public Map.Entry<Customer, String> getSmallest() {
        return copyFunc(customerMap.firstEntry());
    }

    public Map.Entry<Customer, String> getNext(Customer customer) {
        return copyFunc(customerMap.higherEntry(customer));
    }

    public void add(Customer customer, String data) {
        customerMap.put(customer, data);
    }

    private static Map.Entry<Customer, String> copyFunc(Map.Entry<Customer, String> entry) {
        if (entry == null) return null;

        var customerCopy = new Customer(entry.getKey());
        return new AbstractMap.SimpleEntry<>(customerCopy, entry.getValue());
    }
}
