package ru.otus.crm.model;

import javax.persistence.*;

@Entity
@Table(name = "address")
public class Address implements Cloneable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "street")
    private String street;

    public Address(Long id, String street) {
        this.id = id;
        this.street = street;
    }

    public Address(String street) {
        this.street = street;
    }

    public Address() {
    }

    @Override
    protected Address clone() {
        return new Address(id, street);
    }
}
