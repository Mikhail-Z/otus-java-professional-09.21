package com.company.servlet.dto;

import java.util.List;

public class ClientInfoDTO {
    private String name;
    private String address;
    private List<String> phones;

    public ClientInfoDTO() {
    }

    public ClientInfoDTO(String name, String address, List<String> phones) {
        this.name = name;
        this.address = address;
        this.phones = phones;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public List<String> getPhones() {
        return phones;
    }
}