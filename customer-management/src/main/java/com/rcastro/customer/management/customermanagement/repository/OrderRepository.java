package com.rcastro.customer.management.customermanagement.repository;

import com.rcastro.customer.management.customermanagement.domain.Order;
import com.rcastro.customer.management.customermanagement.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface OrderRepository extends MongoRepository<Order, Long>{

    Page<Order> findByUser(User user, Pageable pageable);
}

