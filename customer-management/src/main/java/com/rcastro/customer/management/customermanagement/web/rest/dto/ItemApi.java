package com.rcastro.customer.management.customermanagement.web.rest.dto;

import org.springframework.lang.NonNull;

import java.math.BigDecimal;

public class ItemApi {

    private Long id;

    @NonNull
    private ProductApi productApi;

    @NonNull
    private Integer quantity;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @NonNull
    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(@NonNull Integer quantity) {
        this.quantity = quantity;
    }

    @NonNull
    public ProductApi getProductApi() {
        return productApi;
    }

    public void setProductApi(@NonNull ProductApi productApi) {
        this.productApi = productApi;
    }
}
