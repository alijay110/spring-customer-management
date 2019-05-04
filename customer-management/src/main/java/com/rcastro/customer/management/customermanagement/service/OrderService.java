package com.rcastro.customer.management.customermanagement.service;

import com.rcastro.customer.management.customermanagement.domain.Order;
import com.rcastro.customer.management.customermanagement.domain.User;
import com.rcastro.customer.management.customermanagement.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    @Autowired
    private OrderRepository repository;

    public Long nextSequence() {
        List<Order> all = repository.findAll();
        int next = 1;
        if (!CollectionUtils.isEmpty(all)) {
            next = all.size() + 1;
        }
        return Long.valueOf(next);
    }

    public Page<Order> list(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public List<Order> listOrdersByUser(User user, Pageable pageable) {

        List<Order> orders = new ArrayList<>();
        Page<Order> result = repository.findByUser(user, pageable);
        if (result.getTotalElements() > 0) {
            orders.addAll(result.getContent());
        }

        return orders;
    }

    public Optional<Order> findById(Long id) {
        return repository.findById(id);
    }


    public void save(Order order) {
        repository.save(order);
    }

    public void remove(Order order) {
        repository.delete(order);
    }
}
