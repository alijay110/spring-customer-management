package com.rcastro.customer.management.customermanagement.web.rest.dto;

import com.rcastro.customer.management.customermanagement.utils.Luhn;
import org.springframework.lang.NonNull;

import java.time.LocalDate;

public class CreditCardApi {

    private Long id;

    @NonNull
    private String cardNumber;

    private String cvv;

    private String name;

    private LocalDate expiration;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @NonNull
    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(@NonNull String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getExpiration() {
        return expiration;
    }

    public void setExpiration(LocalDate expiration) {
        this.expiration = expiration;
    }

    public boolean isValid() {
        if (!cardNumber.contains("*")) {
            return Luhn.check(cardNumber.replace("-", ""));
        }

        return false;
    }
}
