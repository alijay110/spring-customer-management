package com.rcastro.customer.management.customermanagement.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mongodb.lang.NonNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDate;

@Document
public class CreditCard {

    @Id
    @Field("id")
    private Long id;

    @Field
    @NonNull
    private String cardNumber;

    @Field
    @NonNull
    private String cvv;

    @Field
    @NonNull
    private String name;

    @Field
    @NonNull
    private LocalDate expiration;

    public void setId(Long id) {
        this.id = id;
    }

    public void setCardNumber(@NonNull String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public void setCvv(@NonNull String cvv) {
        this.cvv = cvv;
    }

    public void setName(@NonNull String name) {
        this.name = name;
    }

    public void setExpiration(@NonNull LocalDate expiration) {
        this.expiration = expiration;
    }

    public Long getId() {
        return id;
    }

    public String getCardNumber() {
        return cardNumber.replaceAll("-","").trim().replaceAll("\\d(?=\\d{4})", "*");
    }

    public String getCvv() {
        return cvv.replaceAll("\\d", "*");
    }

    public String getName() {
        return name;
    }

    public LocalDate getExpiration() {
        return expiration;
    }

    @Override
    public String toString() {
        return "CreditCard{" +
                "cardNumber='" + getCardNumber() + '\'' +
                ", cvv='" + getCvv() + '\'' +
                ", name='" + getName() + '\'' +
                ", expiration=" + getExpiration() +
                '}';
    }
}
