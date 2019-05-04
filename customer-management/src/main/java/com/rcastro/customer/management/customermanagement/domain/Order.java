package com.rcastro.customer.management.customermanagement.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Document
public class Order {

    @Id
    @Field("id")
    private final Long id;

    @Field
    private final User user;

    @Field
    private final List<Item> items;

    @Field
    private final LocalDateTime created;

    @Field
    private final CreditCard creditCard;

    @Field
    private BigDecimal total = BigDecimal.ZERO;

    public Order(Long id, User user, List<Item> items, LocalDateTime created, CreditCard creditCard) {
        this.id = id;
        this.user = user;
        this.items = items;
        this.created = created;
        this.creditCard = creditCard;

        if(!CollectionUtils.isEmpty(items)){
            this.total = items.stream().map(item -> new BigDecimal(item.getQuantity()).multiply(item.getProduct().getPrice())).reduce(BigDecimal::add).get();
        }
    }

    public User getUser() {
        return user;
    }

    public List<Item> getItems() {
        return items;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public Long getId() {
        return id;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public CreditCard getCreditCard() {
        return creditCard;
    }

    @Override
    public String toString() {
        return "OrderApi{" +
                "user=" + user +
                ", items=" + items +
                ", created=" + created +
                ", total=" + total +
                ", creditCard=" + creditCard +
                '}';
    }
}
