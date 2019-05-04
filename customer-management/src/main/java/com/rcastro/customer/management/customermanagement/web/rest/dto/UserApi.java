package com.rcastro.customer.management.customermanagement.web.rest.dto;

import java.time.LocalDate;
import java.util.List;

public class UserApi {

    private Long id;
    private String name;
    private LocalDate birthday;
    private List<CreditCardApi> wallet;

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

    public List<CreditCardApi> getWallet() {
        return wallet;
    }

    public void setWallet(List<CreditCardApi> wallet) {
        this.wallet = wallet;
    }
}
