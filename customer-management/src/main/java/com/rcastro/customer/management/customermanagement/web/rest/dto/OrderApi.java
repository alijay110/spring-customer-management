package com.rcastro.customer.management.customermanagement.web.rest.dto;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

public class OrderApi {

    private  Long id;

    @NotNull
    private  Long userId;

    @NotNull
    private  List<ItemApi> items;

    @NotNull
    private CreditCardApi creditCard;

    private BigDecimal total = BigDecimal.ZERO;



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public List<ItemApi> getItems() {
        return items;
    }

    public void setItems(List<ItemApi> items) {
        this.items = items;
    }

    public void setCreditCard(CreditCardApi creditCard) {
        this.creditCard = creditCard;
    }

    public CreditCardApi getCreditCard() {
        return creditCard;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public BigDecimal getTotal() {
        return total;
    }
}
