package com.rcastro.customer.management.customermanagement.repository;

import com.rcastro.customer.management.customermanagement.domain.Item;
import com.rcastro.customer.management.customermanagement.domain.Product;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProductRepository extends MongoRepository<Product, Long>{
}

