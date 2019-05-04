package com.rcastro.customer.management.customermanagement.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Document
public class User {

    @Id
    @Field("id")
    private Long id;

    @Field
    private String name;

    @Field
    private LocalDate birthday;

    @Field
    private LocalDateTime created;

    @Field
    private List<CreditCard> wallet;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    public List<CreditCard> getWallet() {
        return wallet;
    }

    public void setWallet(List<CreditCard> wallet) {
        this.wallet = wallet;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name  +
                ", birthday=" + birthday +
                ", created=" + created +
                ", wallet=" + wallet +
                '}';
    }
}
