package com.rcastro.customer.management.customermanagement.domain;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document
public class Item {

    @Field
    private final Product product;

    @Field
    private final Integer quantity;

    public Item( Product product, Integer quantity) {
        this.product = product;
        this.quantity = quantity;
    }


    public Integer getQuantity() {
        return quantity;
    }

    public Product getProduct() {
        return product;
    }

    @Override
    public String toString() {
        return "Item{" +
                ", product=" + product +
                ", quantity=" + quantity +
                '}';
    }
}
