package com.rcastro.customer.management.customermanagement.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.math.BigDecimal;

@Document
public class Product {

    @Id
    @Field("id")
    private final Long id;

    @Field
    private final String description;

    @Field
    private final BigDecimal price;

    public Product(Long id, String description, BigDecimal price) {
        this.id = id;
        this.description = description;
        this.price = price;
    }

    public Long getId() {
        return id;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return "Item{" +
                "description='" + description + '\'' +
                ", price=" + price +
                '}';
    }
}
