package com.rcastro.customer.management.customermanagement.service;

import com.rcastro.customer.management.customermanagement.domain.Product;
import com.rcastro.customer.management.customermanagement.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private ProductRepository repository;

    public Long nextSequence() {
        List<Product> all = repository.findAll();
        int next = 1;
        if (!CollectionUtils.isEmpty(all)) {
            next = all.size() + 1;
        }
        return Long.valueOf(next);
    }

    public List<Product> list(Pageable pageable) {

        List<Product> products = new ArrayList<>();
        Page<Product> all = repository.findAll(pageable);
        if (all.getTotalElements() > 0) {
            products.addAll(all.getContent());
        }

        return products;
    }

    public Optional<Product> findById(Long id) {
        return repository.findById(id);
    }


    public void save(Product product) {
        repository.save(product);
    }

    public void remove(Product product) {
        repository.delete(product);
    }
}
